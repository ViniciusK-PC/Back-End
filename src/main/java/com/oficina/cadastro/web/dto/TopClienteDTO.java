package com.oficina.cadastro.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopClienteDTO {
    private UUID id;
    private String nome;
    private Long totalOrdens;
    private Double valorTotal;
}
