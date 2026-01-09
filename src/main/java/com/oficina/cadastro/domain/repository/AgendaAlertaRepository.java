package com.oficina.cadastro.domain.repository;

import com.oficina.cadastro.domain.enums.StatusAlerta;
import com.oficina.cadastro.domain.model.AgendaAlerta;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface AgendaAlertaRepository
                extends org.springframework.data.jpa.repository.JpaRepository<AgendaAlerta, Long> {

        List<AgendaAlerta> findByStatusAndDataAgendadaBetween(StatusAlerta status, OffsetDateTime starts,
                        OffsetDateTime ends);
}
