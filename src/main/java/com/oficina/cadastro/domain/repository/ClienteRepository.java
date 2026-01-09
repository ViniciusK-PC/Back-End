package com.oficina.cadastro.domain.repository;

import com.oficina.cadastro.domain.model.Cliente;
import java.util.List;

public interface ClienteRepository extends org.springframework.data.jpa.repository.JpaRepository<Cliente, Long> {

    List<Cliente> findByNomeContainingIgnoreCase(String nome);
}
