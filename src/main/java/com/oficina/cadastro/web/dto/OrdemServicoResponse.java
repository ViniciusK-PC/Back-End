package com.oficina.cadastro.web.dto;

import com.oficina.cadastro.domain.enums.StatusOrdemServico;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public record OrdemServicoResponse(
        UUID id,
        UUID clienteId,
        String clienteNome,
        UUID equipamentoId,
        String equipamentoDescricao,
        UUID responsavelId,
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

