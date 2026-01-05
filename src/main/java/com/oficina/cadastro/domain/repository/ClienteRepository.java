package com.oficina.cadastro.domain.repository;

import com.oficina.cadastro.domain.model.Cliente;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, UUID> {

    List<Cliente> findByNomeContainingIgnoreCase(String nome);
}

