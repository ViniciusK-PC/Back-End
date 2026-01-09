package com.oficina.cadastro.web.dto;

import com.oficina.cadastro.domain.enums.StatusOrdemServico;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record OrdemServicoRequest(
                Long id,
                @NotNull Long clienteId,
                String clienteNome,
                @NotNull Long equipamentoId,
                String equipamentoDescricao,
                Long responsavelId,
                StatusOrdemServico status,
                @NotBlank String descricaoProblema,
                String descricaoServico,
                BigDecimal valorMaoObra,
                BigDecimal valorPecas,
                LocalDate previsaoEntrega,
                LocalDate dataConclusao,
                @Size(max = 4000) String observacoes) {
}
