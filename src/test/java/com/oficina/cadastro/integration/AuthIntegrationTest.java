package com.oficina.cadastro.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oficina.cadastro.web.dto.LoginRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

/**
 * Testes de integração para o fluxo de autenticação
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @Test
        public void deveRealizarLoginComSucesso() throws Exception {
                // Arrange
                LoginRequest loginRequest = new LoginRequest();
                loginRequest.setEmail("Mauricio@oficina.com");
                loginRequest.setSenha("admin123");

                // Act & Assert
                mockMvc.perform(post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginRequest)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.token").exists())
                                .andExpect(jsonPath("$.token").isNotEmpty())
                                .andExpect(jsonPath("$.email").value("Mauricio@oficina.com"));
        }

        @Test
        public void deveFalharLoginComCredenciaisInvalidas() throws Exception {
                // Arrange
                LoginRequest loginRequest = new LoginRequest();
                loginRequest.setEmail("usuario@invalido.com");
                loginRequest.setSenha("senhaerrada");

                // Act & Assert
                mockMvc.perform(post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginRequest)))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        public void deveFalharLoginComEmailVazio() throws Exception {
                // Arrange
                LoginRequest loginRequest = new LoginRequest();
                loginRequest.setEmail("");
                loginRequest.setSenha("senha123");

                // Act & Assert
                mockMvc.perform(post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginRequest)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        public void deveFalharLoginComSenhaVazia() throws Exception {
                // Arrange
                LoginRequest loginRequest = new LoginRequest();
                loginRequest.setEmail("usuario@teste.com");
                loginRequest.setSenha("");

                // Act & Assert
                mockMvc.perform(post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginRequest)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        public void deveRetornarErroAoUsarGetNoLogin() throws Exception {
                // Act & Assert
                mockMvc.perform(get("/api/auth/login"))
                                .andExpect(status().isMethodNotAllowed())
                                .andExpect(jsonPath("$.error").value("Método não permitido"))
                                .andExpect(jsonPath("$.method").value("POST"));
        }

        @Test
        public void deveValidarTokenJWTEmEndpointProtegido() throws Exception {
                // Arrange - Fazer login para obter token
                LoginRequest loginRequest = new LoginRequest();
                loginRequest.setEmail("Mauricio@oficina.com");
                loginRequest.setSenha("admin123");

                MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginRequest)))
                                .andExpect(status().isOk())
                                .andReturn();

                String responseBody = loginResult.getResponse().getContentAsString();
                String token = objectMapper.readTree(responseBody).get("token").asText();

                // Act & Assert - Usar token para acessar endpoint protegido (clientes)
                mockMvc.perform(get("/api/clientes")
                                .header("Authorization", "Bearer " + token))
                                .andExpect(status().isOk());
        }

        @Test
        public void deveFalharAoAcessarEndpointProtegidoSemToken() throws Exception {
                // Act & Assert
                mockMvc.perform(get("/api/clientes"))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        public void deveFalharComTokenInvalido() throws Exception {
                // Act & Assert
                mockMvc.perform(get("/api/clientes")
                                .header("Authorization", "Bearer token_invalido_123"))
                                .andExpect(status().isUnauthorized());
        }
}
