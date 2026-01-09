package com.oficina.cadastro.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum PerfilUsuario {
    RECEPCAO,
    ATENDENTE,
    TECNICO,
    GERENTE,
    DONO;

    @JsonCreator
    public static PerfilUsuario fromString(String value) {
        if (value == null)
            return null;
        return PerfilUsuario.valueOf(value.toUpperCase());
    }
}
