package com.oficina.cadastro.domain.repository;

import com.oficina.cadastro.domain.model.Cliente;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClienteRepository {

    List<Cliente> findAll();

    Optional<Cliente> findById(UUID id);

    Cliente save(Cliente cliente);

    void deleteById(UUID id);

    List<Cliente> findByNomeContainingIgnoreCase(String nome);

    long count();
}
