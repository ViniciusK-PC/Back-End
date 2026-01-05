package com.oficina.cadastro.domain.repository;

import com.oficina.cadastro.domain.enums.StatusOrdemServico;
import com.oficina.cadastro.domain.model.OrdemServico;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrdemServicoRepository extends JpaRepository<OrdemServico, UUID> {

    List<OrdemServico> findByStatus(StatusOrdemServico status);

    List<OrdemServico> findByClienteId(UUID clienteId);

    // Dashboard queries
    Long countByStatusIn(List<StatusOrdemServico> statuses);

    Long countByStatusAndDataConclusaoAfter(StatusOrdemServico status, LocalDateTime data);

    @Query("SELECT COALESCE(SUM(o.valorMaoObra + o.valorPecas), 0.0) FROM OrdemServico o WHERE o.dataConclusao >= :data")
    Double sumValoresByDataConclusaoAfter(@Param("data") LocalDateTime data);

    @Query("SELECT o.status as status, COUNT(o) as count FROM OrdemServico o GROUP BY o.status")
    List<Object[]> countByStatusGrouped();

    @Query("SELECT MONTH(o.dataConclusao) as mes, YEAR(o.dataConclusao) as ano, SUM(o.valorMaoObra + o.valorPecas) as receita "
            +
            "FROM OrdemServico o WHERE YEAR(o.dataConclusao) = :ano GROUP BY MONTH(o.dataConclusao), YEAR(o.dataConclusao) ORDER BY mes")
    List<Object[]> getReceitaMensalByAno(@Param("ano") int ano);

    @Query("SELECT o.cliente.id as clienteId, o.cliente.nome as clienteNome, COUNT(o) as totalOrdens, SUM(o.valorMaoObra + o.valorPecas) as valorTotal "
            +
            "FROM OrdemServico o GROUP BY o.cliente.id, o.cliente.nome ORDER BY valorTotal DESC")
    List<Object[]> getTopClientes();
}
