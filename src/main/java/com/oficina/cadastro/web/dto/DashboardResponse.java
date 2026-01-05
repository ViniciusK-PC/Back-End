package com.oficina.cadastro.web.dto;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {

    private List<CardInsight> cards;
    private List<RankingCliente> rankingClientes;
    private List<AlertaItem> alertas;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CardInsight {
        private String label;
        private String value;
        private String helper;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RankingCliente {
        private String nome;
        private BigDecimal valor;
        private Long ordens;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AlertaItem {
        private String titulo;
        private String data;
        private String descricao;
    }
}











