package com.oficina.cadastro.web.dto;

import com.oficina.cadastro.domain.enums.PerfilUsuario;
import java.time.OffsetDateTime;
import java.util.UUID;

public record UsuarioResponse(
        UUID id,
        String nome,
        String email,
        PerfilUsuario perfil,
        boolean ativo,
        OffsetDateTime criadoEm,
        OffsetDateTime atualizadoEm) {
}

