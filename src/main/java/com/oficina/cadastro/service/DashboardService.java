package com.oficina.cadastro.service;

import com.oficina.cadastro.domain.enums.StatusAlerta;
import com.oficina.cadastro.domain.enums.StatusOrdemServico;
import com.oficina.cadastro.domain.model.AgendaAlerta;
import com.oficina.cadastro.domain.model.OrdemServico;
import com.oficina.cadastro.domain.repository.AgendaAlertaRepository;
import com.oficina.cadastro.domain.repository.OrdemServicoRepository;
import com.oficina.cadastro.web.dto.DashboardResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

        private final OrdemServicoRepository ordemServicoRepository;
        private final AgendaAlertaRepository agendaAlertaRepository;

        public DashboardResponse obterDashboard() {
                List<OrdemServico> todasOrdens = ordemServicoRepository.findAll();
                LocalDate hoje = LocalDate.now();
                LocalDate inicioMes = hoje.withDayOfMonth(1);
                LocalDate fimMes = hoje.withDayOfMonth(hoje.lengthOfMonth());
                LocalDate inicioMesAnterior = inicioMes.minusMonths(1);
                LocalDate fimMesAnterior = inicioMesAnterior.withDayOfMonth(inicioMesAnterior.lengthOfMonth());
                LocalDate inicio90Dias = hoje.minusDays(90);
                OffsetDateTime proximos7Dias = OffsetDateTime.now().plusDays(7);

                // Cards de métricas
                List<DashboardResponse.CardInsight> cards = calcularCards(
                                todasOrdens, inicioMes, fimMes, inicioMesAnterior, fimMesAnterior);

                // Ranking de clientes (últimos 90 dias)
                List<DashboardResponse.RankingCliente> rankingClientes = calcularRankingClientes(todasOrdens,
                                inicio90Dias);

                // Alertas (próximos 7 dias)
                List<DashboardResponse.AlertaItem> alertas = calcularAlertas(proximos7Dias);

                return DashboardResponse.builder()
                                .cards(cards)
                                .rankingClientes(rankingClientes)
                                .alertas(alertas)
                                .build();
        }

        private List<DashboardResponse.CardInsight> calcularCards(
                        List<OrdemServico> todasOrdens,
                        LocalDate inicioMes,
                        LocalDate fimMes,
                        LocalDate inicioMesAnterior,
                        LocalDate fimMesAnterior) {

                List<DashboardResponse.CardInsight> cards = new ArrayList<>();

                // Receita no mês
                BigDecimal receitaMes = todasOrdens.stream()
                                .filter(o -> o.getDataConclusao() != null
                                                && !o.getDataConclusao().isBefore(inicioMes)
                                                && !o.getDataConclusao().isAfter(fimMes)
                                                && o.getStatus() == StatusOrdemServico.ENTREGUE)
                                .map(OrdemServico::getValorTotal)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal receitaMesAnterior = todasOrdens.stream()
                                .filter(o -> o.getDataConclusao() != null
                                                && !o.getDataConclusao().isBefore(inicioMesAnterior)
                                                && !o.getDataConclusao().isAfter(fimMesAnterior)
                                                && o.getStatus() == StatusOrdemServico.ENTREGUE)
                                .map(OrdemServico::getValorTotal)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                String variacaoReceita = "0%";
                if (receitaMesAnterior.compareTo(BigDecimal.ZERO) > 0) {
                        BigDecimal percentual = receitaMes
                                        .subtract(receitaMesAnterior)
                                        .divide(receitaMesAnterior, 4, RoundingMode.HALF_UP)
                                        .multiply(new BigDecimal("100"));
                        variacaoReceita = String.format(
                                        Locale.US, "%+.0f%% vs mês anterior", percentual.doubleValue());
                } else if (receitaMes.compareTo(BigDecimal.ZERO) > 0) {
                        variacaoReceita = "+100% vs mês anterior";
                }

                cards.add(DashboardResponse.CardInsight.builder()
                                .label("RECEITA NO MÊS")
                                .value("R$ " + formatarValor(receitaMes))
                                .helper(variacaoReceita)
                                .build());

                // Ordens em aberto
                long ordensAberto = todasOrdens.stream()
                                .filter(o -> o.getStatus() != StatusOrdemServico.ENTREGUE)
                                .count();

                long aguardandoPecas = todasOrdens.stream()
                                .filter(o -> o.getStatus() == StatusOrdemServico.AGUARDANDO_PECAS)
                                .count();

                cards.add(DashboardResponse.CardInsight.builder()
                                .label("ORDENS EM ABERTO")
                                .value(String.valueOf(ordensAberto))
                                .helper(aguardandoPecas > 0 ? aguardandoPecas + " aguardando peças" : "")
                                .build());

                // Ticket médio
                List<OrdemServico> ordensConcluidasMes = todasOrdens.stream()
                                .filter(o -> o.getDataConclusao() != null
                                                && !o.getDataConclusao().isBefore(inicioMes)
                                                && !o.getDataConclusao().isAfter(fimMes)
                                                && o.getStatus() == StatusOrdemServico.ENTREGUE
                                                && o.getValorTotal().compareTo(BigDecimal.ZERO) > 0)
                                .toList();

                BigDecimal ticketMedio = BigDecimal.ZERO;
                if (!ordensConcluidasMes.isEmpty()) {
                        BigDecimal soma = ordensConcluidasMes.stream()
                                        .map(OrdemServico::getValorTotal)
                                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                        ticketMedio = soma.divide(
                                        new BigDecimal(ordensConcluidasMes.size()), 2, RoundingMode.HALF_UP);
                }

                cards.add(DashboardResponse.CardInsight.builder()
                                .label("TICKET MÉDIO")
                                .value("R$ " + formatarValor(ticketMedio))
                                .helper("Meta: R$ 1.250")
                                .build());

                // SLA cumprido (simplificado - baseado em previsão de entrega)
                long ordensComPrevisao = todasOrdens.stream()
                                .filter(o -> o.getPrevisaoEntrega() != null
                                                && o.getStatus() == StatusOrdemServico.ENTREGUE
                                                && o.getDataConclusao() != null)
                                .count();

                long ordensNoPrazo = todasOrdens.stream()
                                .filter(o -> o.getPrevisaoEntrega() != null
                                                && o.getStatus() == StatusOrdemServico.ENTREGUE
                                                && o.getDataConclusao() != null
                                                && !o.getDataConclusao().isAfter(o.getPrevisaoEntrega()))
                                .count();

                int slaPercentual = 0;
                if (ordensComPrevisao > 0) {
                        slaPercentual = (int) Math.round((double) ordensNoPrazo / ordensComPrevisao * 100);
                }

                cards.add(DashboardResponse.CardInsight.builder()
                                .label("SLA CUMPRIDO")
                                .value(slaPercentual + "%")
                                .helper("Meta: 90%")
                                .build());

                return cards;
        }

        private List<DashboardResponse.RankingCliente> calcularRankingClientes(
                        List<OrdemServico> todasOrdens, LocalDate inicio90Dias) {
                Map<String, List<OrdemServico>> porCliente = todasOrdens.stream()
                                .filter(o -> o.getDataConclusao() != null
                                                && !o.getDataConclusao().isBefore(inicio90Dias)
                                                && o.getStatus() == StatusOrdemServico.ENTREGUE)
                                .collect(Collectors.groupingBy(
                                                o -> o.getCliente().getNome()));

                return porCliente.entrySet().stream()
                                .map(entry -> {
                                        String nome = entry.getKey();
                                        List<OrdemServico> ordens = entry.getValue();
                                        BigDecimal total = ordens.stream()
                                                        .map(OrdemServico::getValorTotal)
                                                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                                        return DashboardResponse.RankingCliente.builder()
                                                        .nome(nome)
                                                        .valor(total)
                                                        .ordens((long) ordens.size())
                                                        .build();
                                })
                                .sorted(Comparator.comparing(DashboardResponse.RankingCliente::getValor)
                                                .reversed())
                                .limit(10)
                                .collect(Collectors.toList());
        }

        private List<DashboardResponse.AlertaItem> calcularAlertas(OffsetDateTime proximos7Dias) {
                OffsetDateTime agora = OffsetDateTime.now();
                List<AgendaAlerta> alertas = agendaAlertaRepository.findByStatusAndDataAgendadaBetween(
                                StatusAlerta.PENDENTE, agora, proximos7Dias);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM • HH'h'",
                                Locale.forLanguageTag("pt-BR"));

                return alertas.stream()
                                .map(alerta -> {
                                        String dataFormatada = formatarDataAlerta(alerta.getDataAgendada(), formatter);
                                        String titulo = alerta.getTipo().name().replace("_", " ");
                                        String descricao = "Cliente: " + (alerta.getOrdemServico() != null
                                                        && alerta.getOrdemServico().getCliente() != null
                                                                        ? alerta.getOrdemServico().getCliente()
                                                                                        .getNome()
                                                                        : "Desconhecido");
                                        if (alerta.getDescricao() != null && !alerta.getDescricao().isEmpty()) {
                                                titulo = alerta.getDescricao();
                                        }
                                        return DashboardResponse.AlertaItem.builder()
                                                        .titulo(titulo)
                                                        .data(dataFormatada)
                                                        .descricao(descricao)
                                                        .build();
                                })
                                .sorted(Comparator.comparing(DashboardResponse.AlertaItem::getData))
                                .limit(10)
                                .collect(Collectors.toList());
        }

        private String formatarDataAlerta(OffsetDateTime data, DateTimeFormatter formatter) {
                OffsetDateTime agora = OffsetDateTime.now();
                LocalDate hoje = agora.toLocalDate();
                LocalDate dataLocal = data.toLocalDate();

                if (dataLocal.equals(hoje)) {
                        return "Hoje • " + data.format(DateTimeFormatter.ofPattern("HH'h'"));
                } else if (dataLocal.equals(hoje.plusDays(1))) {
                        return "Amanhã • " + data.format(DateTimeFormatter.ofPattern("HH'h'"));
                } else {
                        return data.format(formatter);
                }
        }

        private String formatarValor(BigDecimal valor) {
                if (valor == null)
                        return "0,00";
                return String.format(Locale.US, "%,.2f", valor).replace(",", "X").replace(".", ",").replace("X", ".");
        }
}
