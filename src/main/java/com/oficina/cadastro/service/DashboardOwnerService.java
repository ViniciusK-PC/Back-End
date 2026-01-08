package com.oficina.cadastro.service;

import com.oficina.cadastro.domain.enums.StatusOrdemServico;
import com.oficina.cadastro.domain.repository.ClienteRepository;
import com.oficina.cadastro.domain.repository.OrdemServicoRepository;
import com.oficina.cadastro.web.dto.DashboardKPIsDTO;
import com.oficina.cadastro.web.dto.ReceitaMensalDTO;
import com.oficina.cadastro.web.dto.StatusCountDTO;
import com.oficina.cadastro.web.dto.TopClienteDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardOwnerService {

        private final OrdemServicoRepository ordemRepository;
        private final ClienteRepository clienteRepository;

        public DashboardKPIsDTO calcularKPIs() {
                java.time.LocalDate inicioMes = java.time.LocalDate.now().withDayOfMonth(1);

                Long totalClientes = clienteRepository.count();

                List<StatusOrdemServico> statusEmAndamento = Arrays.asList(
                                StatusOrdemServico.RECEBIDO,
                                StatusOrdemServico.DIAGNOSTICO,
                                StatusOrdemServico.EM_REPARO,
                                StatusOrdemServico.AGUARDANDO_PECAS);

                Long ordensEmAndamento = ordemRepository.countByStatusIn(statusEmAndamento);

                Long ordensConcluidasMes = ordemRepository.countByStatusAndDataConclusaoAfter(
                                StatusOrdemServico.ENTREGUE,
                                inicioMes);

                java.math.BigDecimal receitaTotalMesBD = ordemRepository.sumValoresByDataConclusaoAfter(inicioMes);
                Double receitaTotalMes = receitaTotalMesBD != null ? receitaTotalMesBD.doubleValue() : 0.0;

                return DashboardKPIsDTO.builder()
                                .totalClientes(totalClientes)
                                .ordensEmAndamento(ordensEmAndamento)
                                .ordensConcluidasMes(ordensConcluidasMes)
                                .receitaTotalMes(receitaTotalMes)
                                .build();
        }

        public List<StatusCountDTO> contarOrdensPorStatus() {
                List<Object[]> results = ordemRepository.countByStatusGrouped();

                return results.stream()
                                .map(row -> StatusCountDTO.builder()
                                                .status(((StatusOrdemServico) row[0]).name())
                                                .count((Long) row[1])
                                                .build())
                                .collect(Collectors.toList());
        }

        public List<ReceitaMensalDTO> calcularReceitaMensal(int ano) {
                List<Object[]> results = ordemRepository.getReceitaMensalByAno(ano);

                return results.stream()
                                .map(row -> ReceitaMensalDTO.builder()
                                                .mes((Integer) row[0])
                                                .ano(ano)
                                                .receita(row[1] != null ? ((java.math.BigDecimal) row[1]).doubleValue()
                                                                : 0.0)
                                                .build())
                                .collect(Collectors.toList());
        }

        public List<TopClienteDTO> getTopClientes(int limit) {
                List<Object[]> results = ordemRepository.getTopClientes();

                return results.stream()
                                .limit(limit)
                                .map(row -> TopClienteDTO.builder()
                                                .id((UUID) row[0])
                                                .nome((String) row[1])
                                                .totalOrdens((Long) row[2])
                                                .valorTotal(row[3] != null
                                                                ? ((java.math.BigDecimal) row[3]).doubleValue()
                                                                : 0.0)
                                                .build())
                                .collect(Collectors.toList());
        }
}
