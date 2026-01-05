package com.oficina.cadastro.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceitaMensalDTO {
    private Integer mes;
    private Integer ano;
    private Double receita;
}
