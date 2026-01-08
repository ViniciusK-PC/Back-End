package com.oficina.cadastro.domain.model;

import java.time.OffsetDateTime;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Equipamento {

    private UUID id;
    private Cliente cliente;
    private String descricao;
    private String marca;
    private String modelo;
    private String numeroSerie;
    private LocalDate dataCompra;
    private String observacoes;
    private OffsetDateTime criadoEm;
    private OffsetDateTime atualizadoEm;
}
