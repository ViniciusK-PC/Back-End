package com.oficina.cadastro.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oficina.cadastro.web.dto.ClienteRequest;
import com.oficina.cadastro.web.dto.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de integração para operações CRUD de clientes
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ClienteIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        private String authToken;

        @BeforeEach
        public void setup() throws Exception {
                // Fazer login para obter token de autenticação
                LoginRequest loginRequest = new LoginRequest();
                loginRequest.setEmail("Mauricio@oficina.com");
                loginRequest.setSenha("admin123");

                MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginRequest)))
                                .andExpect(status().isOk())
                                .andReturn();

                String responseBody = loginResult.getResponse().getContentAsString();
                authToken = objectMapper.readTree(responseBody).get("token").asText();
        }

        @Test
        public void deveCriarClienteComSucesso() throws Exception {
                // Arrange
                String email = "cliente.teste" + System.currentTimeMillis() + "@teste.com";
                ClienteRequest clienteRequest = new ClienteRequest(
                                "Cliente Teste",
                                "12345678901",
                                email,
                                "11987654321",
                                "11987654321",
                                "Rua Teste, 123, São Paulo - SP, 01234-567",
                                "Cliente de teste",
                                true);

                // Act & Assert
                mockMvc.perform(post("/api/clientes")
                                .header("Authorization", "Bearer " + authToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(clienteRequest)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.nome").value(clienteRequest.nome()))
                                .andExpect(jsonPath("$.documento").value(clienteRequest.documento()))
                                .andExpect(jsonPath("$.email").value(clienteRequest.email()))
                                .andExpect(jsonPath("$.id").exists());
        }

        @Test
        public void deveListarClientesComSucesso() throws Exception {
                // Act & Assert
                mockMvc.perform(get("/api/clientes")
                                .header("Authorization", "Bearer " + authToken))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$").isArray());
        }

        @Test
        public void deveBuscarClientePorIdComSucesso() throws Exception {
                // Arrange - Criar um cliente primeiro
                String email = "cliente.busca" + System.currentTimeMillis() + "@teste.com";
                ClienteRequest clienteRequest = new ClienteRequest(
                                "Cliente Busca",
                                "98765432101",
                                email,
                                "11987654321",
                                "11987654321",
                                "Rua Busca, 456",
                                null,
                                true);

                MvcResult createResult = mockMvc.perform(post("/api/clientes")
                                .header("Authorization", "Bearer " + authToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(clienteRequest)))
                                .andExpect(status().isCreated())
                                .andReturn();

                String responseBody = createResult.getResponse().getContentAsString();
                String clienteId = objectMapper.readTree(responseBody).get("id").asText();

                // Act & Assert
                mockMvc.perform(get("/api/clientes/" + clienteId)
                                .header("Authorization", "Bearer " + authToken))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(clienteId))
                                .andExpect(jsonPath("$.nome").value(clienteRequest.nome()));
        }

        @Test
        public void deveAtualizarClienteComSucesso() throws Exception {
                // Arrange - Criar um cliente primeiro
                String email = "cliente.original" + System.currentTimeMillis() + "@teste.com";
                ClienteRequest clienteRequest = new ClienteRequest(
                                "Cliente Original",
                                "11122233344",
                                email,
                                "11987654321",
                                "11987654321",
                                "Rua Original, 789",
                                null,
                                true);

                MvcResult createResult = mockMvc.perform(post("/api/clientes")
                                .header("Authorization", "Bearer " + authToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(clienteRequest)))
                                .andExpect(status().isCreated())
                                .andReturn();

                String responseBody = createResult.getResponse().getContentAsString();
                String clienteId = objectMapper.readTree(responseBody).get("id").asText();

                // Atualizar dados
                ClienteRequest clienteAtualizado = new ClienteRequest(
                                "Cliente Atualizado",
                                "11122233344",
                                email,
                                "11999999999",
                                "11999999999",
                                "Rua Atualizada, 999",
                                "Cliente atualizado",
                                true);

                // Act & Assert
                mockMvc.perform(put("/api/clientes/" + clienteId)
                                .header("Authorization", "Bearer " + authToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(clienteAtualizado)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.nome").value("Cliente Atualizado"))
                                .andExpect(jsonPath("$.telefone").value("11999999999"));
        }

        @Test
        public void deveDeletarClienteComSucesso() throws Exception {
                // Arrange - Criar um cliente primeiro
                String email = "cliente.deletar" + System.currentTimeMillis() + "@teste.com";
                ClienteRequest clienteRequest = new ClienteRequest(
                                "Cliente Para Deletar",
                                "55566677788",
                                email,
                                "11987654321",
                                "11987654321",
                                "Rua Deletar, 111",
                                null,
                                true);

                MvcResult createResult = mockMvc.perform(post("/api/clientes")
                                .header("Authorization", "Bearer " + authToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(clienteRequest)))
                                .andExpect(status().isCreated())
                                .andReturn();

                String responseBody = createResult.getResponse().getContentAsString();
                String clienteId = objectMapper.readTree(responseBody).get("id").asText();

                // Act & Assert - Deletar
                mockMvc.perform(delete("/api/clientes/" + clienteId)
                                .header("Authorization", "Bearer " + authToken))
                                .andExpect(status().isNoContent());

                // Verificar que foi deletado
                mockMvc.perform(get("/api/clientes/" + clienteId)
                                .header("Authorization", "Bearer " + authToken))
                                .andExpect(status().isNotFound());
        }

        @Test
        public void deveFalharAoAcessarSemAutenticacao() throws Exception {
                // Act & Assert
                mockMvc.perform(get("/api/clientes"))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        public void deveFalharAoCriarClienteComDadosInvalidos() throws Exception {
                // Arrange - Cliente sem nome (nome vazio - deve falhar validação)
                ClienteRequest clienteRequest = new ClienteRequest(
                                "", // nome vazio
                                "12345678901",
                                "teste@teste.com",
                                "11987654321",
                                null,
                                null,
                                null,
                                true);

                // Act & Assert
                mockMvc.perform(post("/api/clientes")
                                .header("Authorization", "Bearer " + authToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(clienteRequest)))
                                .andExpect(status().isBadRequest());
        }
}
