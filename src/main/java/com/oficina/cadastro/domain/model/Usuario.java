package com.oficina.cadastro.domain.model;

import com.oficina.cadastro.domain.enums.PerfilUsuario;
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
public class Usuario {

    private UUID id;
    private String nome;
    private String email;
    private String senhaHash;
    private PerfilUsuario perfil;
    private boolean ativo = true;
    private OffsetDateTime criadoEm;
    private OffsetDateTime atualizadoEm;
}
