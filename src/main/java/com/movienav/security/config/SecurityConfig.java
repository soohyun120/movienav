package com.movienav.security.config;

import com.movienav.domain.entity.enumPackage.UserRole;
import com.movienav.security.jwt.JwtAccessDeniedHandler;
import com.movienav.security.jwt.JwtAuthenticationEntryPoint;
import com.movienav.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsConfig corsConfig;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate redisTemplate;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()

                .formLogin().disable()
                .httpBasic().disable()
                .apply(new CustomConfig(corsConfig, jwtTokenProvider, redisTemplate))

                .and()
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(HttpMethod.POST, "/api/member").permitAll()
                        .requestMatchers("/api/member/username", "/api/member/password").permitAll()
                        .requestMatchers("/api/admin/**").hasRole(UserRole.ADMIN.getValue())
                        .requestMatchers("/api/auth/login", "/api/movie/**").permitAll()
                        .requestMatchers( "/api/movie/{movieId}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/review").permitAll()
                        .requestMatchers("/api/review/{reviewId}", "/api/review/{movieId}", "/api/review/all").permitAll()
                        .anyRequest().authenticated()
                )

                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                .and()

                .logout().disable()
                .build();
    }
}
