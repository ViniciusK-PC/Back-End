package com.oficina.cadastro.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum PerfilUsuario {
    RECEPCAO,
    ATENDENTE,
    TECNICO,
    MECANICO,
    GERENTE,
    ADMINISTRADOR,
    DONO;

    @JsonCreator
    public static PerfilUsuario fromString(String value) {
        if (value == null || value.isBlank())
            return null;

        String upperValue = value.toUpperCase();

        // Mapeamentos específicos para evitar erros
        if (upperValue.equals("ADM"))
            return ADMINISTRADOR;

        return PerfilUsuario.valueOf(upperValue);
    }
}
