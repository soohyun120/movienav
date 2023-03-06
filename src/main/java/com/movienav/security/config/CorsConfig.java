package com.movienav.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);   //내 서버가 응답할 때 json 자바스크립트 처리 허용
        config.addAllowedOrigin("*");   //모든 ip에 응답 허용
        config.addAllowedHeader("*");   //모든 헤더에 응답 허용
        config.addAllowedMethod("*");   //모든 메서드 응답 허용

       source.registerCorsConfiguration("/api/**", config);
        return new CorsFilter(source);
    }
}
