package com.oficina.cadastro.domain.model;

import com.oficina.cadastro.domain.enums.StatusOrdemServico;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdemServico {

    private UUID id;
    private Cliente cliente;
    private Equipamento equipamento;
    private Usuario responsavel;
    private StatusOrdemServico status = StatusOrdemServico.RECEBIDO;
    private String descricaoProblema;
    private String descricaoServico;
    private BigDecimal valorMaoObra = BigDecimal.ZERO;
    private BigDecimal valorPecas = BigDecimal.ZERO;
    private BigDecimal valorTotal = BigDecimal.ZERO;
    private LocalDate previsaoEntrega;
    private LocalDate dataConclusao;
    private String observacoes;
    private OffsetDateTime criadoEm;
    private OffsetDateTime atualizadoEm;

    public void recalcTotal() {
        BigDecimal maoObra = valorMaoObra != null ? valorMaoObra : BigDecimal.ZERO;
        BigDecimal pecas = valorPecas != null ? valorPecas : BigDecimal.ZERO;
        this.valorTotal = maoObra.add(pecas);
    }
}
