package com.oficina.cadastro.domain.repository;

import com.oficina.cadastro.domain.model.Equipamento;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EquipamentoRepository {

    List<Equipamento> findAll();

    Optional<Equipamento> findById(UUID id);

    Equipamento save(Equipamento equipamento);

    void deleteById(UUID id);

    List<Equipamento> findByClienteId(UUID clienteId);
}
