package com.oficina.cadastro.domain.repository;

import com.oficina.cadastro.domain.enums.StatusOrdemServico;
import com.oficina.cadastro.domain.model.OrdemServico;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrdemServicoRepository {

        List<OrdemServico> findAll();

        Optional<OrdemServico> findById(UUID id);

        OrdemServico save(OrdemServico ordemServico);

        void deleteById(UUID id);

        List<OrdemServico> findByStatus(StatusOrdemServico status);

        List<OrdemServico> findByClienteId(UUID clienteId);

        Long countByStatusIn(List<StatusOrdemServico> statuses);

        Long countByStatusAndDataConclusaoAfter(StatusOrdemServico status, LocalDateTime data);

        Double sumValoresByDataConclusaoAfter(LocalDateTime data);

        List<Object[]> countByStatusGrouped();

        List<Object[]> getReceitaMensalByAno(int ano);

        List<Object[]> getTopClientes();
}
