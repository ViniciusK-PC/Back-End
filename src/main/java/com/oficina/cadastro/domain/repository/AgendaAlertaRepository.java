package com.oficina.cadastro.domain.repository;

import com.oficina.cadastro.domain.enums.StatusAlerta;
import com.oficina.cadastro.domain.model.AgendaAlerta;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgendaAlertaRepository extends JpaRepository<AgendaAlerta, UUID> {

    List<AgendaAlerta> findByStatusAndDataAgendadaBetween(
            StatusAlerta status, OffsetDateTime inicio, OffsetDateTime fim);
}











