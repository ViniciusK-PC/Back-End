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

                // Usar setAllowedOriginPatterns é mais flexível e seguro com credenciais do que
                // setAllowedOrigins
                // Adicionando explicitamente o frontend informado e wildcards para garantir
                // acesso
                configuration.setAllowedOriginPatterns(Arrays.asList(
                                "https://front-end-five-sable.vercel.app",
                                "https://*.vercel.app",
                                "https://*.railway.app",
                                "http://localhost:*",
                                "http://127.0.0.1:*",
                                "*")); // Fallback para garantir que funcione se o header vier diferente

                // Permitir todos os métodos HTTP
                configuration.setAllowedMethods(Arrays.asList(
                                "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"));

                // Permitir todos os headers
                configuration.setAllowedHeaders(Arrays.asList("*"));

                // Permitir credenciais
                configuration.setAllowCredentials(true);

                // Expor headers
                configuration.setExposedHeaders(Arrays.asList(
                                "Authorization",
                                "Content-Type",
                                "X-Total-Count"));

                configuration.setMaxAge(3600L);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);

                return source;
        }
}
