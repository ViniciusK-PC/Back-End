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

        // Remove acentos e converte para maiúsculo para comparação robusta
        String normalized = java.text.Normalizer.normalize(value, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toUpperCase()
                .trim();

        // Mapeamentos específicos para evitar erros de frontend e suporte a termos
        // variados
        if (normalized.equals("ADM") || normalized.equals("ADMIN") || normalized.equals("ADMINISTRADOR")
                || normalized.equals("ADMINISTRADORES"))
            return ADMINISTRADOR;

        if (normalized.equals("MECANICO") || normalized.equals("MECANICA") || normalized.equals("MECANICOS"))
            return MECANICO;

        if (normalized.equals("TECNICO") || normalized.equals("TECNICOS"))
            return TECNICO;

        if (normalized.equals("RECEPCAO") || normalized.equals("RECEPCIONISTA"))
            return RECEPCAO;

        if (normalized.equals("ATENDENTE") || normalized.equals("ATENDIMENTO") || normalized.equals("ATENDENTES"))
            return ATENDENTE;

        if (normalized.equals("GERENTE") || normalized.equals("GERENCIA") || normalized.equals("GERENTES"))
            return GERENTE;

        if (normalized.equals("DONO") || normalized.equals("PROPRIETARIO"))
            return DONO;

        try {
            return PerfilUsuario.valueOf(normalized);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
