package com.oficina.cadastro.web.dto;

import com.oficina.cadastro.domain.enums.StatusOrdemServico;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

public record OrdemServicoResponse(
                Long id,
                Long clienteId,
                String clienteNome,
                Long equipamentoId,
                String equipamentoDescricao,
                Long responsavelId,
                String responsavelNome,
                StatusOrdemServico status,
                String descricaoProblema,
                String descricaoServico,
                BigDecimal valorMaoObra,
                BigDecimal valorPecas,
                BigDecimal valorTotal,
                LocalDate previsaoEntrega,
                LocalDate dataConclusao,
                String observacoes,
                OffsetDateTime criadoEm,
                OffsetDateTime atualizadoEm) {
}
