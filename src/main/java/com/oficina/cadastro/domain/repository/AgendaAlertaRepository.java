package com.oficina.cadastro.domain.repository;

import com.oficina.cadastro.domain.enums.StatusAlerta;
import com.oficina.cadastro.domain.model.AgendaAlerta;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AgendaAlertaRepository {

    List<AgendaAlerta> findAll();

    Optional<AgendaAlerta> findById(UUID id);

    AgendaAlerta save(AgendaAlerta agendaAlerta);

    void deleteById(UUID id);

    List<AgendaAlerta> findByStatusAndDataAgendadaBetween(StatusAlerta status, OffsetDateTime starts,
            OffsetDateTime ends);
}
