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
public class Cliente {

    private UUID id;
    private String nome;
    private String documento;
    private String email;
    private String telefone;
    private String whatsapp;
    private String endereco;
    private String observacoes;
    private boolean ativo = true;
    private OffsetDateTime criadoEm;
    private OffsetDateTime atualizadoEm;
}
