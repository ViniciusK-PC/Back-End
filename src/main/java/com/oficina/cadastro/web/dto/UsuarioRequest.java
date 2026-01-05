package com.oficina.cadastro.web.dto;

import com.oficina.cadastro.domain.enums.PerfilUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioRequest(
        @NotBlank @Size(max = 120) String nome,
        @Email @NotBlank @Size(max = 160) String email,
        @NotBlank @Size(min = 6, max = 60) String senha,
        @NotNull PerfilUsuario perfil,
        Boolean ativo) {
}

