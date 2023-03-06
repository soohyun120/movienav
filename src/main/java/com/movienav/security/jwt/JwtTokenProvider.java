package com.movienav.security.jwt;

import com.movienav.domain.entity.Member;
import com.movienav.security.config.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import static com.movienav.security.jwt.JwtProperties.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final UserDetailsService userDetailsService;

    @Value("${jwt.secret}")
    private String secretKey;

    private Key key;

    @PostConstruct
    protected  void init() {
        byte[] KeyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        this.key = Keys.hmacShaKeyFor(KeyBytes);
    }

    public String generateRefreshToken(Member member) {
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam("typ", "REFRESH_TOKEN")
                .setHeaderParam("alg", "HS512")
                .setSubject(member.getUsername())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_VALID_TIME))
                .claim("role", member.getRole())
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateAccessToken(Member member) {
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam("typ", "REFRESH_TOKEN")
                .setHeaderParam("alg", "HS512")
                .setSubject(member.getUsername())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_VALID_TIME))
                .claim("role", member.getRole())
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String resolveToken(HttpServletRequest request) {
        String header = request.getHeader(HEADER_STRING);
        if (header != null && header.startsWith(TOKEN_PREFIX)) {
            return header.replace(TOKEN_PREFIX, "");
        }
        return null;
    }

    public Authentication getAuthentication(String token) {
        UserDetailsImpl userDetails =
                (UserDetailsImpl) userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return !claims.getExpiration().before(new Date());
        } catch (SignatureException e) {
            log.error("Invalid JWT signature", e);
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token", e);
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired", e);
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported", e);
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty", e);
        }
        return false;
    }

    public long getExpiration(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        long currentTime = new Date().getTime();

        return expiration.getTime() - currentTime;
    }
}
