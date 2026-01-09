package com.oficina.cadastro.domain.model;

import com.oficina.cadastro.domain.enums.CanalNotificacao;
import com.oficina.cadastro.domain.enums.StatusAlerta;
import com.oficina.cadastro.domain.enums.TipoAlerta;
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
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "agenda_alertas")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgendaAlerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ordem_servico_id")
    private OrdemServico ordemServico;

    @Enumerated(EnumType.STRING)
    private TipoAlerta tipo;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StatusAlerta status = StatusAlerta.PENDENTE;

    private OffsetDateTime dataAgendada;

    @Enumerated(EnumType.STRING)
    private CanalNotificacao canal;

    private String descricao;

    @ManyToOne
    @JoinColumn(name = "criado_por_id")
    private Usuario criadoPor;

    private OffsetDateTime atualizadoEm;
    private OffsetDateTime criadoEm;

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
