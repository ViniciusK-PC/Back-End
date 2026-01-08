package com.oficina.cadastro.service;

import com.oficina.cadastro.domain.model.Cliente;
import com.oficina.cadastro.domain.repository.ClienteRepository;
import com.oficina.cadastro.web.dto.ClienteRequest;
import com.oficina.cadastro.web.dto.ClienteResponse;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public List<ClienteResponse> listar(String nome) {
        List<Cliente> clientes = nome == null || nome.isBlank()
                ? clienteRepository.findAll()
                : clienteRepository.findByNomeContainingIgnoreCase(nome);
        return clientes.stream().map(this::toResponse).collect(Collectors.toList());
    }

    public ClienteResponse buscar(UUID id) {
        return toResponse(findOrThrow(id));
    }

    public ClienteResponse criar(ClienteRequest request) {
        Cliente cliente = new Cliente();
        applyRequest(request, cliente);
        return toResponse(clienteRepository.save(cliente));
    }

    public ClienteResponse atualizar(UUID id, ClienteRequest request) {
        Cliente cliente = findOrThrow(id);
        applyRequest(request, cliente);
        return toResponse(clienteRepository.save(cliente));
    }

    public void deletar(UUID id) {
        Cliente cliente = findOrThrow(id);
        clienteRepository.deleteById(cliente.getId());
    }

    private Cliente findOrThrow(UUID id) {
        return clienteRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    }

    private void applyRequest(ClienteRequest request, Cliente cliente) {
        cliente.setNome(request.nome());
        cliente.setDocumento(request.documento());
        cliente.setEmail(request.email());
        cliente.setTelefone(request.telefone());
        cliente.setWhatsapp(request.whatsapp());
        cliente.setEndereco(request.endereco());
        cliente.setObservacoes(request.observacoes());
        if (request.ativo() != null) {
            cliente.setAtivo(request.ativo());
        }
    }

    private ClienteResponse toResponse(Cliente cliente) {
        return new ClienteResponse(
                cliente.getId(),
                cliente.getNome(),
                cliente.getDocumento(),
                cliente.getEmail(),
                cliente.getTelefone(),
                cliente.getWhatsapp(),
                cliente.getEndereco(),
                cliente.getObservacoes(),
                cliente.isAtivo(),
                cliente.getCriadoEm(),
                cliente.getAtualizadoEm());
    }
}
