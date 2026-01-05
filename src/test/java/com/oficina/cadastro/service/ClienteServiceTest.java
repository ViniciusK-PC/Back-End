package com.oficina.cadastro.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.oficina.cadastro.domain.model.Cliente;
import com.oficina.cadastro.domain.repository.ClienteRepository;
import com.oficina.cadastro.web.dto.ClienteRequest;
import com.oficina.cadastro.web.dto.ClienteResponse;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;
    private UUID clienteId;

    @BeforeEach
    void setUp() {
        clienteId = UUID.randomUUID();
        cliente =
                Cliente.builder()
                        .id(clienteId)
                        .nome("João Silva")
                        .email("joao@example.com")
                        .telefone("11999999999")
                        .ativo(true)
                        .criadoEm(OffsetDateTime.now())
                        .atualizadoEm(OffsetDateTime.now())
                        .build();
    }

    @Test
    void deveListarClientes() {
        List<Cliente> clientes = Arrays.asList(cliente);
        when(clienteRepository.findAll()).thenReturn(clientes);

        List<ClienteResponse> resultado = clienteService.listar(null);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("João Silva", resultado.get(0).nome());
        verify(clienteRepository).findAll();
    }

    @Test
    void deveBuscarClientePorId() {
        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));

        ClienteResponse resultado = clienteService.buscar(clienteId);

        assertNotNull(resultado);
        assertEquals("João Silva", resultado.nome());
        verify(clienteRepository).findById(clienteId);
    }

    @Test
    void deveLancarExcecaoQuandoClienteNaoEncontrado() {
        when(clienteRepository.findById(clienteId)).thenReturn(Optional.empty());

        assertThrows(
                jakarta.persistence.EntityNotFoundException.class,
                () -> clienteService.buscar(clienteId));
    }

    @Test
    void deveCriarCliente() {
        ClienteRequest request =
                new ClienteRequest(
                        "Maria Santos",
                        "12345678900",
                        "maria@example.com",
                        "11888888888",
                        "11977777777",
                        "Rua Teste, 123",
                        "Observações",
                        true);

        Cliente novoCliente =
                Cliente.builder()
                        .id(UUID.randomUUID())
                        .nome(request.nome())
                        .email(request.email())
                        .telefone(request.telefone())
                        .ativo(true)
                        .build();

        when(clienteRepository.save(any(Cliente.class))).thenReturn(novoCliente);

        ClienteResponse resultado = clienteService.criar(request);

        assertNotNull(resultado);
        assertEquals("Maria Santos", resultado.nome());
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void deveAtualizarCliente() {
        ClienteRequest request =
                new ClienteRequest(
                        "João Silva Atualizado",
                        "12345678900",
                        "joao.novo@example.com",
                        "11999999999",
                        null,
                        null,
                        null,
                        true);

        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        ClienteResponse resultado = clienteService.atualizar(clienteId, request);

        assertNotNull(resultado);
        verify(clienteRepository).findById(clienteId);
        verify(clienteRepository).save(cliente);
    }
}

