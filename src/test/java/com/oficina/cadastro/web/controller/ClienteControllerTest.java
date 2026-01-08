package com.oficina.cadastro.web.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.oficina.cadastro.service.ClienteService;
import com.oficina.cadastro.web.dto.ClienteRequest;
import com.oficina.cadastro.web.dto.ClienteResponse;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClienteControllerTest {

        @Mock
        private ClienteService clienteService;

        @InjectMocks
        private ClienteController clienteController;

        private UUID clienteId;
        private ClienteResponse clienteResponse;

        @BeforeEach
        void setUp() {
                clienteId = UUID.randomUUID();
                clienteResponse = new ClienteResponse(
                                clienteId,
                                "João Silva",
                                "12345678900",
                                "joao@example.com",
                                "11999999999",
                                "11988888888",
                                "Rua Teste",
                                "Observações",
                                true,
                                OffsetDateTime.now(),
                                OffsetDateTime.now());
        }

        @Test
        void deveListarClientes() {
                List<ClienteResponse> clientes = Arrays.asList(clienteResponse);
                when(clienteService.listar(null)).thenReturn(clientes);

                List<ClienteResponse> resultado = clienteController.listar(null);

                assertNotNull(resultado);
                assertEquals(1, resultado.size());
                assertEquals("João Silva", resultado.get(0).nome());
                verify(clienteService).listar(null);
        }

        @Test
        void deveCriarCliente() {
                ClienteRequest request = new ClienteRequest(
                                "Maria Santos",
                                "98765432100",
                                "maria@example.com",
                                "11888888888",
                                null,
                                null,
                                null,
                                true);

                ClienteResponse response = new ClienteResponse(
                                UUID.randomUUID(),
                                "Maria Santos",
                                "98765432100",
                                "maria@example.com",
                                "11888888888",
                                null,
                                null,
                                null,
                                true,
                                OffsetDateTime.now(),
                                OffsetDateTime.now());

                when(clienteService.criar(any(ClienteRequest.class))).thenReturn(response);

                ClienteResponse resultado = clienteController.criar(request);

                assertNotNull(resultado);
                assertEquals("Maria Santos", resultado.nome());
                verify(clienteService).criar(any(ClienteRequest.class));
        }

        @Test
        void deveAtualizarCliente() {
                ClienteRequest request = new ClienteRequest(
                                "João Atualizado",
                                "12345678900",
                                "joao.novo@example.com",
                                "11999999999",
                                null,
                                null,
                                null,
                                true);

                when(clienteService.atualizar(eq(clienteId), any(ClienteRequest.class)))
                                .thenReturn(clienteResponse);

                ClienteResponse resultado = clienteController.atualizar(clienteId, request);

                assertNotNull(resultado);
                verify(clienteService).atualizar(eq(clienteId), any(ClienteRequest.class));
        }

        @Test
        void deveDeletarCliente() {
                doNothing().when(clienteService).deletar(clienteId);

                clienteController.deletar(clienteId);

                verify(clienteService).deletar(clienteId);
        }
}
