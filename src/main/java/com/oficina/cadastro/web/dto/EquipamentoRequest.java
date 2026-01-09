package com.oficina.cadastro.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record EquipamentoRequest(
        @NotNull Long clienteId,
        @NotBlank @Size(max = 160) String descricao,
        @Size(max = 80) String marca,
        @Size(max = 80) String modelo,
        @Size(max = 80) String numeroSerie,
        LocalDate dataCompra,
        String observacoes) {
}
