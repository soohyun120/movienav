package com.movienav.security.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 인증 되지 않은 유저가 요청했을 때 동작
 */

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.sendRedirect("/exception/entryPoint");

        /**
         * 1. 토큰 없는 경우
         */

        /**
         * 2. 토큰 만료된 경우
         */

        /**
         * 3. 토큰 시그니처가 다른 경우
         */

    }
}
