package com.oficina.cadastro.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oficina.cadastro.domain.enums.StatusOrdemServico;
import com.oficina.cadastro.web.dto.ClienteRequest;
import com.oficina.cadastro.web.dto.EquipamentoRequest;
import com.oficina.cadastro.web.dto.LoginRequest;
import com.oficina.cadastro.web.dto.OrdemServicoRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de integração para operações de Ordem de Serviço
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class OrdemServicoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String authToken;
    private String clienteId;
    private String equipamentoId;

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

        // Criar um cliente para usar nos testes
        String email = "cliente.os" + System.currentTimeMillis() + "@teste.com";
        ClienteRequest clienteRequest = new ClienteRequest(
                "Cliente OS Teste",
                "12345678901",
                email,
                "11987654321",
                null,
                null,
                null,
                true);

        MvcResult clienteResult = mockMvc.perform(post("/api/clientes")
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        clienteId = objectMapper.readTree(clienteResult.getResponse().getContentAsString())
                .get("id").asText();

        // Criar um equipamento para usar nos testes
        EquipamentoRequest equipamentoRequest = new EquipamentoRequest(
                UUID.fromString(clienteId),
                "Notebook Dell",
                "Dell",
                "Inspiron 15",
                "ABC123456",
                null,
                null);

        MvcResult equipamentoResult = mockMvc.perform(post("/api/equipamentos")
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(equipamentoRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        equipamentoId = objectMapper.readTree(equipamentoResult.getResponse().getContentAsString())
                .get("id").asText();
    }

    @Test
    public void deveCriarOrdemServicoComSucesso() throws Exception {
        // Arrange
        OrdemServicoRequest osRequest = new OrdemServicoRequest(
                UUID.fromString(clienteId),
                UUID.fromString(equipamentoId),
                null, // responsavelId
                StatusOrdemServico.RECEBIDO,
                "Manutenção preventiva - Limpeza e troca de pasta térmica",
                null, // descricaoServico
                BigDecimal.valueOf(150.00), // valorMaoObra
                BigDecimal.valueOf(50.00), // valorPecas
                null, // previsaoEntrega
                null, // dataConclusao
                "Cliente solicitou revisão completa");

        // Act & Assert
        mockMvc.perform(post("/api/ordens-servico")
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(osRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.descricaoProblema").value(osRequest.descricaoProblema()))
                .andExpect(jsonPath("$.status").value(osRequest.status().toString()))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    public void deveListarOrdensServicoComSucesso() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/ordens-servico")
                .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void deveAtualizarStatusOrdemServicoComSucesso() throws Exception {
        // Arrange - Criar uma OS primeiro
        OrdemServicoRequest osRequest = new OrdemServicoRequest(
                UUID.fromString(clienteId),
                UUID.fromString(equipamentoId),
                null,
                StatusOrdemServico.ABERTA,
                "Troca de óleo e filtros",
                null,
                BigDecimal.valueOf(100.00),
                BigDecimal.valueOf(80.00),
                null,
                null,
                null);

        MvcResult createResult = mockMvc.perform(post("/api/ordens-servico")
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(osRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        String osId = objectMapper.readTree(createResult.getResponse().getContentAsString())
                .get("id").asText();

        // Atualizar status
        OrdemServicoRequest osAtualizada = new OrdemServicoRequest(
                UUID.fromString(clienteId),
                UUID.fromString(equipamentoId),
                null,
                StatusOrdemServico.EM_REPARO,
                "Troca de óleo e filtros",
                "Iniciado serviço de manutenção",
                BigDecimal.valueOf(100.00),
                BigDecimal.valueOf(80.00),
                null,
                null,
                "Serviço em andamento");

        // Act & Assert
        mockMvc.perform(put("/api/ordens-servico/" + osId)
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(osAtualizada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("EM_REPARO"));
    }

    @Test
    public void deveFalharAoAcessarSemAutenticacao() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/ordens-servico"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void deveFalharAoCriarOSSemDescricao() throws Exception {
        // Arrange - OS sem descrição do problema (campo obrigatório)
        OrdemServicoRequest osRequest = new OrdemServicoRequest(
                UUID.fromString(clienteId),
                UUID.fromString(equipamentoId),
                null,
                StatusOrdemServico.ABERTA,
                "", // descrição vazia - deve falhar
                null,
                null,
                null,
                null,
                null,
                null);

        // Act & Assert
        mockMvc.perform(post("/api/ordens-servico")
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(osRequest)))
                .andExpect(status().isBadRequest());
    }
}
