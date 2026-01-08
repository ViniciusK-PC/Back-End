package com.oficina.cadastro.domain.model;

import com.oficina.cadastro.domain.enums.CanalNotificacao;
import com.oficina.cadastro.domain.enums.StatusAlerta;
import com.oficina.cadastro.domain.enums.TipoAlerta;
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
public class AgendaAlerta {

    private UUID id;
    private OrdemServico ordemServico;
    private TipoAlerta tipo;
    private StatusAlerta status = StatusAlerta.PENDENTE;
    private OffsetDateTime dataAgendada;
    private CanalNotificacao canal;
    private String descricao;
    private Usuario criadoPor;
    private OffsetDateTime atualizadoEm;
}
