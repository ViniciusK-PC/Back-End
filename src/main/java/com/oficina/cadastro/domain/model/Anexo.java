package com.oficina.cadastro.domain.model;

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
public class Anexo {

    private UUID id;
    private OrdemServico ordemServico;
    private String nomeArquivo;
    private String tipoConteudo;
    private Long tamanho;
    private String url;
    private OffsetDateTime criadoEm;
}
