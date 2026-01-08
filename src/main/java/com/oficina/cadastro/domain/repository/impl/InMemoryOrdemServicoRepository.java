package com.oficina.cadastro.domain.repository.impl;

import com.oficina.cadastro.domain.enums.StatusOrdemServico;
import com.oficina.cadastro.domain.model.OrdemServico;
import com.oficina.cadastro.domain.repository.OrdemServicoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class InMemoryOrdemServicoRepository implements OrdemServicoRepository {

    private final Map<UUID, OrdemServico> storage = new ConcurrentHashMap<>();

    @Override
    public List<OrdemServico> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Optional<OrdemServico> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public OrdemServico save(OrdemServico ordemServico) {
        if (ordemServico.getId() == null) {
            ordemServico.setId(UUID.randomUUID());
            ordemServico.setCriadoEm(OffsetDateTime.now());
        }
        ordemServico.setAtualizadoEm(OffsetDateTime.now());
        storage.put(ordemServico.getId(), ordemServico);
        return ordemServico;
    }

    @Override
    public void deleteById(UUID id) {
        storage.remove(id);
    }

    @Override
    public List<OrdemServico> findByStatus(StatusOrdemServico status) {
        return storage.values().stream()
                .filter(o -> o.getStatus() == status)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrdemServico> findByClienteId(UUID clienteId) {
        return storage.values().stream()
                .filter(o -> o.getCliente() != null && o.getCliente().getId().equals(clienteId))
                .collect(Collectors.toList());
    }

    @Override
    public Long countByStatusIn(List<StatusOrdemServico> statuses) {
        return storage.values().stream()
                .filter(o -> statuses.contains(o.getStatus()))
                .count();
    }

    @Override
    public Long countByStatusAndDataConclusaoAfter(StatusOrdemServico status, LocalDateTime data) {
        return storage.values().stream()
                .filter(o -> o.getStatus() == status && o.getDataConclusao() != null
                        && o.getDataConclusao().isAfter(data.toLocalDate()))
                .count();
    }

    @Override
    public Double sumValoresByDataConclusaoAfter(LocalDateTime data) {
        return storage.values().stream()
                .filter(o -> o.getDataConclusao() != null && o.getDataConclusao().isAfter(data.toLocalDate()))
                .mapToDouble(o -> o.getValorTotal().doubleValue())
                .sum();
    }

    @Override
    public List<Object[]> countByStatusGrouped() {
        Map<StatusOrdemServico, Long> counts = storage.values().stream()
                .collect(Collectors.groupingBy(OrdemServico::getStatus, Collectors.counting()));

        List<Object[]> result = new ArrayList<>();
        counts.forEach((status, count) -> result.add(new Object[] { status, count }));
        return result;
    }

    @Override
    public List<Object[]> getReceitaMensalByAno(int ano) {
        // Very simplified version
        List<Object[]> result = new ArrayList<>();
        for (int m = 1; m <= 12; m++) {
            final int month = m;
            double revenue = storage.values().stream()
                    .filter(o -> o.getDataConclusao() != null && o.getDataConclusao().getYear() == ano
                            && o.getDataConclusao().getMonthValue() == month)
                    .mapToDouble(o -> o.getValorTotal().doubleValue())
                    .sum();
            result.add(new Object[] { month, ano, revenue });
        }
        return result;
    }

    @Override
    public List<Object[]> getTopClientes() {
        Map<UUID, Double> totals = storage.values().stream()
                .filter(o -> o.getCliente() != null)
                .collect(Collectors.groupingBy(o -> o.getCliente().getId(),
                        Collectors.summingDouble(o -> o.getValorTotal().doubleValue())));

        List<Object[]> result = new ArrayList<>();
        totals.forEach((cid, total) -> {
            String nome = storage.values().stream()
                    .filter(o -> o.getCliente() != null && o.getCliente().getId().equals(cid))
                    .findFirst().get().getCliente().getNome();
            long count = storage.values().stream()
                    .filter(o -> o.getCliente() != null && o.getCliente().getId().equals(cid))
                    .count();
            result.add(new Object[] { cid, nome, count, total });
        });

        result.sort((a, b) -> Double.compare((Double) b[3], (Double) a[3]));
        return result;
    }
}
