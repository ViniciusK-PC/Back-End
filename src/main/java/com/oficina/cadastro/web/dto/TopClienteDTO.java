package com.oficina.cadastro.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopClienteDTO {
    private Long id;
    private String nome;
    private Long totalOrdens;
    private Double valorTotal;
}
