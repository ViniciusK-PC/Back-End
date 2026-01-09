package com.oficina.cadastro.service;

import com.oficina.cadastro.domain.model.Cliente;
import com.oficina.cadastro.domain.model.Equipamento;
import com.oficina.cadastro.domain.repository.ClienteRepository;
import com.oficina.cadastro.domain.repository.EquipamentoRepository;
import com.oficina.cadastro.web.dto.EquipamentoRequest;
import com.oficina.cadastro.web.dto.EquipamentoResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EquipamentoService {

    private final EquipamentoRepository equipamentoRepository;
    private final ClienteRepository clienteRepository;

    public List<EquipamentoResponse> listarPorCliente(Long clienteId) {
        return equipamentoRepository.findByClienteId(clienteId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public EquipamentoResponse buscar(Long id) {
        return toResponse(findOrThrow(id));
    }

    public EquipamentoResponse criar(EquipamentoRequest request) {
        Equipamento equipamento = new Equipamento();
        applyRequest(request, equipamento);
        return toResponse(equipamentoRepository.save(equipamento));
    }

    public EquipamentoResponse atualizar(Long id, EquipamentoRequest request) {
        Equipamento equipamento = findOrThrow(id);
        applyRequest(request, equipamento);
        return toResponse(equipamentoRepository.save(equipamento));
    }

    private Equipamento findOrThrow(Long id) {
        return equipamentoRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Equipamento não encontrado"));
    }

    private Cliente findCliente(Long id) {
        return clienteRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    }

    private void applyRequest(EquipamentoRequest request, Equipamento equipamento) {
        equipamento.setCliente(findCliente(request.clienteId()));
        equipamento.setDescricao(request.descricao());
        equipamento.setMarca(request.marca());
        equipamento.setModelo(request.modelo());
        equipamento.setNumeroSerie(request.numeroSerie());
        equipamento.setDataCompra(request.dataCompra());
        equipamento.setObservacoes(request.observacoes());
    }

    private EquipamentoResponse toResponse(Equipamento equipamento) {
        return new EquipamentoResponse(
                equipamento.getId(),
                equipamento.getCliente().getId(),
                equipamento.getCliente().getNome(),
                equipamento.getDescricao(),
                equipamento.getMarca(),
                equipamento.getModelo(),
                equipamento.getNumeroSerie(),
                equipamento.getDataCompra(),
                equipamento.getObservacoes(),
                equipamento.getCriadoEm(),
                equipamento.getAtualizadoEm());
    }
}
