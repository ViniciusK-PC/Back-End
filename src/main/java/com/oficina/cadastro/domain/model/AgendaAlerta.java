package com.oficina.cadastro.domain.model;

import com.oficina.cadastro.domain.enums.CanalNotificacao;
import com.oficina.cadastro.domain.enums.StatusAlerta;
import com.oficina.cadastro.domain.enums.TipoAlerta;
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
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "agenda_alertas")
public class AgendaAlerta {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ordem_id")
    private OrdemServico ordemServico;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private TipoAlerta tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private StatusAlerta status = StatusAlerta.PENDENTE;

    @Column(name = "data_agendada", nullable = false)
    private OffsetDateTime dataAgendada;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private CanalNotificacao canal;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "criado_por")
    private Usuario criadoPor;

    @UpdateTimestamp
    @Column(name = "atualizado_em")
    private OffsetDateTime atualizadoEm;
}

