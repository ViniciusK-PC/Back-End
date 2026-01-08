package com.oficina.cadastro.domain.repository.impl;

import com.oficina.cadastro.domain.enums.StatusAlerta;
import com.oficina.cadastro.domain.model.AgendaAlerta;
import com.oficina.cadastro.domain.repository.AgendaAlertaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class InMemoryAgendaAlertaRepository implements AgendaAlertaRepository {

    private final Map<UUID, AgendaAlerta> storage = new ConcurrentHashMap<>();

    @Override
    public List<AgendaAlerta> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Optional<AgendaAlerta> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public AgendaAlerta save(AgendaAlerta agendaAlerta) {
        if (agendaAlerta.getId() == null) {
            agendaAlerta.setId(UUID.randomUUID());
        }
        agendaAlerta.setAtualizadoEm(OffsetDateTime.now());
        storage.put(agendaAlerta.getId(), agendaAlerta);
        return agendaAlerta;
    }

    @Override
    public void deleteById(UUID id) {
        storage.remove(id);
    }

    @Override
    public List<AgendaAlerta> findByStatusAndDataAgendadaBetween(StatusAlerta status, OffsetDateTime starts,
            OffsetDateTime ends) {
        return storage.values().stream()
                .filter(a -> a.getStatus() == status &&
                        !a.getDataAgendada().isBefore(starts) &&
                        !a.getDataAgendada().isAfter(ends))
                .collect(Collectors.toList());
    }
}
