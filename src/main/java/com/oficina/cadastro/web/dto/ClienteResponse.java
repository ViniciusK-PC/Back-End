package com.oficina.cadastro.web.dto;

import java.time.OffsetDateTime;

public record ClienteResponse(
        Long id,
        String nome,
        String documento,
        String email,
        String telefone,
        String whatsapp,
        String endereco,
        String observacoes,
        boolean ativo,
        OffsetDateTime criadoEm,
        OffsetDateTime atualizadoEm) {
}
