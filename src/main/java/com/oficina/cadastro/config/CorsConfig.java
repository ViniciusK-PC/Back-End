package com.oficina.cadastro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Origens permitidas (Frontend Vercel + localhost para desenvolvimento)
        configuration.setAllowedOriginPatterns(Arrays.asList(
                "https://front-end-five-sable.vercel.app",
                "https://*.vercel.app",
                "http://localhost:*",
                "http://127.0.0.1:*"));

        // Permitir métodos HTTP
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"));

        // Permitir todos os headers
        configuration.setAllowedHeaders(Arrays.asList("*"));

        // Permitir credenciais
        configuration.setAllowCredentials(true);

        // Headers expostos
        configuration.setExposedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "Access-Control-Allow-Origin",
                "Access-Control-Allow-Credentials"));

        // Cache de preflight
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
