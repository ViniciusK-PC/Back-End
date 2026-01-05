package com.oficina.cadastro.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardKPIsDTO {
    private Long totalClientes;
    private Long ordensEmAndamento;
    private Long ordensConcluidasMes;
    private Double receitaTotalMes;
}
