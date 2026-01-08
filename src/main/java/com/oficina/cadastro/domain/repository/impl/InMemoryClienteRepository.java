package com.oficina.cadastro.domain.repository.impl;

import com.oficina.cadastro.domain.model.Cliente;
import com.oficina.cadastro.domain.repository.ClienteRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class InMemoryClienteRepository implements ClienteRepository {

    private final Map<UUID, Cliente> storage = new ConcurrentHashMap<>();

    @Override
    public List<Cliente> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Optional<Cliente> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Cliente save(Cliente cliente) {
        if (cliente.getId() == null) {
            cliente.setId(UUID.randomUUID());
            cliente.setCriadoEm(OffsetDateTime.now());
        }
        cliente.setAtualizadoEm(OffsetDateTime.now());
        storage.put(cliente.getId(), cliente);
        return cliente;
    }

    @Override
    public void deleteById(UUID id) {
        storage.remove(id);
    }

    @Override
    public List<Cliente> findByNomeContainingIgnoreCase(String nome) {
        return storage.values().stream()
                .filter(c -> c.getNome().toLowerCase().contains(nome.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public long count() {
        return storage.size();
    }
}
