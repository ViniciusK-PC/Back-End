package com.oficina.cadastro.domain.model;

import com.oficina.cadastro.domain.enums.StatusOrdemServico;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ordens_servico")
public class OrdemServico {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "equipamento_id")
    private Equipamento equipamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsavel_id")
    private Usuario responsavel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private StatusOrdemServico status = StatusOrdemServico.RECEBIDO;

    @Column(name = "descricao_problema", columnDefinition = "TEXT", nullable = false)
    private String descricaoProblema;

    @Column(name = "descricao_servico", columnDefinition = "TEXT")
    private String descricaoServico;

    @Column(name = "valor_mao_obra", precision = 12, scale = 2)
    private BigDecimal valorMaoObra = BigDecimal.ZERO;

    @Column(name = "valor_pecas", precision = 12, scale = 2)
    private BigDecimal valorPecas = BigDecimal.ZERO;

    @Column(name = "valor_total", precision = 12, scale = 2)
    private BigDecimal valorTotal = BigDecimal.ZERO;

    @Column(name = "previsao_entrega")
    private LocalDate previsaoEntrega;

    @Column(name = "data_conclusao")
    private LocalDate dataConclusao;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @CreationTimestamp
    @Column(name = "criado_em", nullable = false, updatable = false)
    private OffsetDateTime criadoEm;

    @UpdateTimestamp
    @Column(name = "atualizado_em")
    private OffsetDateTime atualizadoEm;

    public void recalcTotal() {
        BigDecimal maoObra = valorMaoObra != null ? valorMaoObra : BigDecimal.ZERO;
        BigDecimal pecas = valorPecas != null ? valorPecas : BigDecimal.ZERO;
        this.valorTotal = maoObra.add(pecas);
    }
}

