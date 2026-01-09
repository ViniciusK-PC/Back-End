package com.oficina.cadastro.domain.repository;

import com.oficina.cadastro.domain.enums.StatusOrdemServico;
import com.oficina.cadastro.domain.model.OrdemServico;
import java.util.List;

public interface OrdemServicoRepository
                extends org.springframework.data.jpa.repository.JpaRepository<OrdemServico, Long> {

        List<OrdemServico> findByStatus(StatusOrdemServico status);

        List<OrdemServico> findByClienteId(Long clienteId);

        Long countByStatusIn(List<StatusOrdemServico> statuses);

        @org.springframework.data.jpa.repository.Query("SELECT COUNT(os) FROM OrdemServico os WHERE os.status = :status AND os.dataConclusao > :data")
        Long countByStatusAndDataConclusaoAfter(StatusOrdemServico status, java.time.LocalDate data);

        @org.springframework.data.jpa.repository.Query("SELECT SUM(os.valorTotal) FROM OrdemServico os WHERE os.dataConclusao > :data")
        java.math.BigDecimal sumValoresByDataConclusaoAfter(java.time.LocalDate data);

        @org.springframework.data.jpa.repository.Query("SELECT os.status, COUNT(os) FROM OrdemServico os GROUP BY os.status")
        List<Object[]> countByStatusGrouped();

        @org.springframework.data.jpa.repository.Query("SELECT CAST(EXTRACT(MONTH FROM os.dataConclusao) AS int), SUM(os.valorTotal) FROM OrdemServico os WHERE CAST(EXTRACT(YEAR FROM os.dataConclusao) AS int) = :ano GROUP BY CAST(EXTRACT(MONTH FROM os.dataConclusao) AS int)")
        List<Object[]> getReceitaMensalByAno(int ano);

        @org.springframework.data.jpa.repository.Query("SELECT os.cliente.id, os.cliente.nome, COUNT(os), SUM(os.valorTotal) FROM OrdemServico os GROUP BY os.cliente.id, os.cliente.nome ORDER BY COUNT(os) DESC")
        List<Object[]> getTopClientes();
}
