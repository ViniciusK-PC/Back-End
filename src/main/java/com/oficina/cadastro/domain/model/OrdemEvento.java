package com.oficina.cadastro.domain.model;

import com.oficina.cadastro.domain.enums.StatusOrdemServico;
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
public class OrdemEvento {

    private UUID id;
    private OrdemServico ordemServico;
    private Usuario usuario;
    private StatusOrdemServico statusAnterior;
    private StatusOrdemServico statusNovo;
    private String observacao;
    private OffsetDateTime criadoEm;
}
