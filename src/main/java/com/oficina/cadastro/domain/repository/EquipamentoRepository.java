package com.oficina.cadastro.domain.repository;

import com.oficina.cadastro.domain.model.Equipamento;
import java.util.List;

public interface EquipamentoRepository
        extends org.springframework.data.jpa.repository.JpaRepository<Equipamento, Long> {

    List<Equipamento> findByClienteId(Long clienteId);
}
