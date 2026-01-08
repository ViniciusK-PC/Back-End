package com.oficina.cadastro.service;

import com.oficina.cadastro.domain.enums.StatusOrdemServico;
import com.oficina.cadastro.domain.model.Cliente;
import com.oficina.cadastro.domain.model.Equipamento;
import com.oficina.cadastro.domain.model.OrdemServico;
import com.oficina.cadastro.domain.model.Usuario;
import com.oficina.cadastro.domain.repository.ClienteRepository;
import com.oficina.cadastro.domain.repository.EquipamentoRepository;
import com.oficina.cadastro.domain.repository.OrdemServicoRepository;
import com.oficina.cadastro.domain.repository.UsuarioRepository;
import com.oficina.cadastro.web.dto.OrdemServicoRequest;
import com.oficina.cadastro.web.dto.OrdemServicoResponse;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrdemServicoService {

    private final OrdemServicoRepository ordemServicoRepository;
    private final ClienteRepository clienteRepository;
    private final EquipamentoRepository equipamentoRepository;
    private final UsuarioRepository usuarioRepository;

    public List<OrdemServicoResponse> listar(StatusOrdemServico status, UUID clienteId) {
        List<OrdemServico> ordens;
        if (status != null) {
            ordens = ordemServicoRepository.findByStatus(status);
        } else if (clienteId != null) {
            ordens = ordemServicoRepository.findByClienteId(clienteId);
        } else {
            ordens = ordemServicoRepository.findAll();
        }
        return ordens.stream().map(this::toResponse).collect(Collectors.toList());
    }

    public OrdemServicoResponse buscar(UUID id) {
        return toResponse(findOrThrow(id));
    }

    public OrdemServicoResponse criar(OrdemServicoRequest request) {
        OrdemServico ordem = new OrdemServico();
        applyRequest(request, ordem);
        ordem.recalcTotal();
        return toResponse(ordemServicoRepository.save(ordem));
    }

    public OrdemServicoResponse atualizar(UUID id, OrdemServicoRequest request) {
        OrdemServico ordem = findOrThrow(id);
        applyRequest(request, ordem);
        ordem.recalcTotal();
        return toResponse(ordemServicoRepository.save(ordem));
    }

    public void deletar(UUID id) {
        OrdemServico ordem = findOrThrow(id);
        ordemServicoRepository.deleteById(ordem.getId());
    }

    private OrdemServico findOrThrow(UUID id) {
        return ordemServicoRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Ordem de serviço não encontrada"));
    }

    private Cliente findCliente(UUID id) {
        return clienteRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    }

    private Equipamento findEquipamento(UUID id) {
        return equipamentoRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Equipamento não encontrado"));
    }

    private Usuario findUsuario(UUID id) {
        if (id == null) {
            return null;
        }
        return usuarioRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    private void applyRequest(OrdemServicoRequest request, OrdemServico ordem) {
        ordem.setCliente(findCliente(request.clienteId()));
        ordem.setEquipamento(findEquipamento(request.equipamentoId()));
        ordem.setResponsavel(findUsuario(request.responsavelId()));
        if (request.status() != null) {
            ordem.setStatus(request.status());
        }
        ordem.setDescricaoProblema(request.descricaoProblema());
        ordem.setDescricaoServico(request.descricaoServico());
        ordem.setValorMaoObra(request.valorMaoObra());
        ordem.setValorPecas(request.valorPecas());
        ordem.setPrevisaoEntrega(request.previsaoEntrega());
        ordem.setDataConclusao(request.dataConclusao());
        ordem.setObservacoes(request.observacoes());
    }

    private OrdemServicoResponse toResponse(OrdemServico ordem) {
        return new OrdemServicoResponse(
                ordem.getId(),
                ordem.getCliente().getId(),
                ordem.getCliente().getNome(),
                ordem.getEquipamento().getId(),
                ordem.getEquipamento().getDescricao(),
                ordem.getResponsavel() != null ? ordem.getResponsavel().getId() : null,
                ordem.getResponsavel() != null ? ordem.getResponsavel().getNome() : null,
                ordem.getStatus(),
                ordem.getDescricaoProblema(),
                ordem.getDescricaoServico(),
                ordem.getValorMaoObra(),
                ordem.getValorPecas(),
                ordem.getValorTotal(),
                ordem.getPrevisaoEntrega(),
                ordem.getDataConclusao(),
                ordem.getObservacoes(),
                ordem.getCriadoEm(),
                ordem.getAtualizadoEm());
    }
}
