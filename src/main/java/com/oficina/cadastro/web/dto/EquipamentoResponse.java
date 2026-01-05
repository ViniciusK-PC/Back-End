package com.oficina.cadastro.web.dto;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public record EquipamentoResponse(
        UUID id,
        UUID clienteId,
        String clienteNome,
        String descricao,
        String marca,
        String modelo,
        String numeroSerie,
        LocalDate dataCompra,
        String observacoes,
        OffsetDateTime criadoEm,
        OffsetDateTime atualizadoEm) {
}

