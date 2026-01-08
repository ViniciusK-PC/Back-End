package com.oficina.cadastro.domain.repository.impl;

import com.oficina.cadastro.domain.model.Equipamento;
import com.oficina.cadastro.domain.repository.EquipamentoRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class InMemoryEquipamentoRepository implements EquipamentoRepository {

    private final Map<UUID, Equipamento> storage = new ConcurrentHashMap<>();

    @Override
    public List<Equipamento> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Optional<Equipamento> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Equipamento save(Equipamento equipamento) {
        if (equipamento.getId() == null) {
            equipamento.setId(UUID.randomUUID());
            equipamento.setCriadoEm(OffsetDateTime.now());
        }
        equipamento.setAtualizadoEm(OffsetDateTime.now());
        storage.put(equipamento.getId(), equipamento);
        return equipamento;
    }

    @Override
    public void deleteById(UUID id) {
        storage.remove(id);
    }

    @Override
    public List<Equipamento> findByClienteId(UUID clienteId) {
        return storage.values().stream()
                .filter(e -> e.getCliente() != null && e.getCliente().getId().equals(clienteId))
                .collect(Collectors.toList());
    }
}
