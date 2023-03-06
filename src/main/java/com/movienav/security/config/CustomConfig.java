package com.movienav.security.config;

import com.movienav.security.jwt.JwtAuthenticationFilter;
import com.movienav.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class CustomConfig extends AbstractHttpConfigurer<CustomConfig, HttpSecurity> {

    private final CorsConfig corsConfig;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate redisTemplate;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        http
                .addFilter(corsConfig.corsFilter())
                .addFilterBefore(
                        new JwtAuthenticationFilter(authenticationManager, jwtTokenProvider, redisTemplate),
                        UsernamePasswordAuthenticationFilter.class);
    }
}
