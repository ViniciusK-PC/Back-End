package com.oficina.cadastro.domain.model;

import com.oficina.cadastro.domain.enums.StatusOrdemServico;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ordens_servicos")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdemServico {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "equipamento_id")
    private Equipamento equipamento;

    @ManyToOne
    @JoinColumn(name = "responsavel_id")
    private Usuario responsavel;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StatusOrdemServico status = StatusOrdemServico.RECEBIDO;

    private String descricaoProblema;
    private String descricaoServico;

    @Builder.Default
    private BigDecimal valorMaoObra = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal valorPecas = BigDecimal.ZERO;

    @Builder.Default
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

    @PrePersist
    public void prePersist() {
        if (criadoEm == null) {
            criadoEm = OffsetDateTime.now();
        }
        atualizadoEm = OffsetDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        atualizadoEm = OffsetDateTime.now();
    }
}
