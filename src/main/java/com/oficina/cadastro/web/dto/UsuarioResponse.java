package com.oficina.cadastro.web.dto;

import com.oficina.cadastro.domain.enums.PerfilUsuario;
import java.time.OffsetDateTime;

public record UsuarioResponse(
        Long id,
        String nome,
        String email,
        PerfilUsuario perfil,
        boolean ativo,
        String senhaHash,
        OffsetDateTime criadoEm,
        OffsetDateTime atualizadoEm) {
}
