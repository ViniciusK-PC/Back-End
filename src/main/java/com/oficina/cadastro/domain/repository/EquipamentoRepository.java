package com.oficina.cadastro.domain.repository;

import com.oficina.cadastro.domain.model.Cliente;
import com.oficina.cadastro.domain.model.Equipamento;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipamentoRepository extends JpaRepository<Equipamento, UUID> {

    List<Equipamento> findByCliente(Cliente cliente);
}

