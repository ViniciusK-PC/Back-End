package com.oficina.cadastro.domain.repository;

import com.oficina.cadastro.domain.model.Equipamento;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EquipamentoRepository
        extends org.springframework.data.jpa.repository.JpaRepository<Equipamento, UUID> {

    List<Equipamento> findByClienteId(UUID clienteId);
}
