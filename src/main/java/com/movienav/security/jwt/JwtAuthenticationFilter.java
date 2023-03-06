package com.movienav.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    private JwtTokenProvider jwtTokenProvider;
    private RedisTemplate redisTemplate;

    public JwtAuthenticationFilter(
            AuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider,
            RedisTemplate redisTemplate) {

        super(authenticationManager);
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String accessToken = jwtTokenProvider.resolveToken(request);

        //access token 유효 검증
        if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {

            //Redis 에서 해당 토큰 logout 검증
            checkLogout(accessToken);

            Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }

    private void checkLogout(String accessToken) {
        String isLogout = (String) redisTemplate.opsForValue().get(accessToken);
        if (isLogout != null) {
            throw new IllegalArgumentException("이미 로그아웃된 회원입니다.");
        }
    }
}
