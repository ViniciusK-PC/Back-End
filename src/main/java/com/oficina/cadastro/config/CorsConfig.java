package com.oficina.cadastro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();

                // Origens permitidas - Frontend Vercel + Railway + localhost
                configuration.setAllowedOrigins(Arrays.asList(
                                "https://front-end-five-sable.vercel.app",
                                "https://back-end-production-1d27.up.railway.app",
                                "http://localhost:4200",
                                "http://localhost:4201",
                                "http://127.0.0.1:4200"));

                // Também permitir padrões para preview deploys do Vercel e subdomínios Railway
                configuration.setAllowedOriginPatterns(Arrays.asList(
                                "https://*.vercel.app",
                                "https://*.railway.app",
                                "http://localhost:*",
                                "http://127.0.0.1:*"));

                // Permitir todos os métodos HTTP
                configuration.setAllowedMethods(Arrays.asList(
                                "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"));

                // Permitir todos os headers
                configuration.setAllowedHeaders(Arrays.asList(
                                "Authorization",
                                "Content-Type",
                                "X-Requested-With",
                                "Accept",
                                "Origin",
                                "Access-Control-Request-Method",
                                "Access-Control-Request-Headers",
                                "Cache-Control"));

                // Permitir credenciais (cookies, authorization headers)
                configuration.setAllowCredentials(true);

                // Headers expostos na resposta
                configuration.setExposedHeaders(Arrays.asList(
                                "Authorization",
                                "Content-Type",
                                "Access-Control-Allow-Origin",
                                "Access-Control-Allow-Credentials",
                                "X-Total-Count"));

                // Cache de preflight (1 hora)
                configuration.setMaxAge(3600L);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);

                return source;
        }
}
