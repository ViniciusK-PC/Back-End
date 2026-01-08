package com.oficina.cadastro.domain.repository;

import com.oficina.cadastro.domain.model.Cliente;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClienteRepository extends org.springframework.data.jpa.repository.JpaRepository<Cliente, UUID> {

    List<Cliente> findByNomeContainingIgnoreCase(String nome);
}
