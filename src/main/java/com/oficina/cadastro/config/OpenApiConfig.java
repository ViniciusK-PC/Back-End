package com.oficina.cadastro.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.servers.Server;
import java.util.List;

@Configuration
public class OpenApiConfig {

        @Bean
        public OpenAPI customOpenAPI() {
                final String securitySchemeName = "bearerAuth";

                Server localServer = new Server();
                localServer.setUrl("http://localhost:8080");
                localServer.setDescription("Servidor Local");

                Server prodServer = new Server();
                prodServer.setUrl("/"); // Referência relativa para funcionar em qualquer host de deploy
                prodServer.setDescription("Servidor de Produção");

                return new OpenAPI()
                                .servers(List.of(prodServer, localServer))
                                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                                .components(new Components()
                                                .addSecuritySchemes(securitySchemeName,
                                                                new SecurityScheme()
                                                                                .name(securitySchemeName)
                                                                                .type(SecurityScheme.Type.HTTP)
                                                                                .scheme("bearer")
                                                                                .bearerFormat("JWT")))
                                .info(new Info()
                                                .title("API Cadastro de Clientes - Oficina")
                                                .version("1.0")
                                                .description("API para gerenciamento de clientes, equipamentos e ordens de serviço.")
                                                .contact(new Contact()
                                                                .name("Suporte")
                                                                .email("suporte@oficina.com")));
        }
}
