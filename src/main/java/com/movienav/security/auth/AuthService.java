package com.movienav.security.auth;

import com.movienav.domain.entity.Member;
import com.movienav.exception.CustomException;
import com.movienav.security.auth.dto.LoginRequest;
import com.movienav.security.auth.dto.TokenDto;
import com.movienav.security.config.UserDetailsImpl;
import com.movienav.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

import static com.movienav.exception.error.AuthErrorCode.*;
import static com.movienav.security.jwt.JwtProperties.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final RedisTemplate<String, String> redisTemplate;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public ResponseEntity<TokenDto> login(LoginRequest loginRequest) {
        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()));

            //토큰 발급
            Member member = getMemberFromAuthenticate(authenticate);
            String accessToken = jwtTokenProvider.generateAccessToken(member);
            String refreshToken = jwtTokenProvider.generateRefreshToken(member);
            TokenDto tokenDto = new TokenDto(accessToken, refreshToken);

            //Redis 에 refresh token 저장
            redisTemplate.opsForValue().set(
                    authenticate.getName(),
                    refreshToken,
                    REFRESH_TOKEN_VALID_TIME,
                    TimeUnit.MILLISECONDS);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(HEADER_STRING, TOKEN_PREFIX + accessToken);

            return new ResponseEntity<>(tokenDto, httpHeaders, HttpStatus.OK);

        } catch (AuthenticationException e) {
            throw new CustomException(INVALID_CREDENTIAL);
        }
    }

    @Transactional
    public ResponseEntity<Object> logout(TokenDto tokenDto) {
        String accessToken = tokenDto.getAccessToken();

        //access token 검증
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new IllegalArgumentException();
        }

        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

        //Redis 에 해당 아이디로 저장된 refresh token 있을 경우 삭제
        if (redisTemplate.opsForValue().get(authentication.getName()) != null) {
            redisTemplate.delete(authentication.getName());
        }

        //해당 access token BlackList 에 저장
        long expiration = jwtTokenProvider.getExpiration(accessToken);
        redisTemplate.opsForValue().set(
                accessToken, "logout", expiration, TimeUnit.MILLISECONDS);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * refresh token 유효하면
     * access token 과 refresh token 모두 재발급
     */
    @Transactional
    public ResponseEntity<TokenDto> reissue(String refreshToken) {
        try {
            //refresh token 검증
            if (!jwtTokenProvider.validateToken(refreshToken)) {
                throw new IllegalArgumentException();
            }

            Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);

            //Redis 에 저장된 refresh token 과 비교
            String redisRefreshToken = redisTemplate.opsForValue().get(authentication.getName());
            if (!redisRefreshToken.equals(refreshToken)) {
                throw new IllegalArgumentException();
            }

            //토큰 재발행
            Member member = getMemberFromAuthenticate(authentication);
            String new_accessToken = jwtTokenProvider.generateAccessToken(member);
            String new_refreshToken = jwtTokenProvider.generateRefreshToken(member);
            TokenDto tokenDto = new TokenDto(new_accessToken, new_refreshToken);

            //Redis 에 새로운 refresh token 업데이트
            redisTemplate.opsForValue().set(
                    authentication.getName(),
                    new_refreshToken,
                    REFRESH_TOKEN_VALID_TIME,
                    TimeUnit.MILLISECONDS);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(HEADER_STRING, TOKEN_PREFIX + new_accessToken);

            return new ResponseEntity<>(tokenDto, httpHeaders, HttpStatus.OK);

        } catch (AuthenticationException e) {
            throw new IllegalArgumentException();
        }
    }

    private Member getMemberFromAuthenticate(Authentication authenticate) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authenticate.getPrincipal();
        return userDetails.getMember();
    }
}
