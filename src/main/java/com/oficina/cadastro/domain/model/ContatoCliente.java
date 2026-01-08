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
public class ContatoCliente {

    private UUID id;
    private Cliente cliente;
    private String nome;
    private String cargo;
    private String email;
    private String telefone;
    private String whatsapp;
    private boolean principal = false;
    private OffsetDateTime criadoEm;
    private OffsetDateTime atualizadoEm;
}
