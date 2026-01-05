package com.oficina.cadastro.web.controller;

import com.oficina.cadastro.web.dto.DashboardKPIsDTO;
import com.oficina.cadastro.web.dto.ReceitaMensalDTO;
import com.oficina.cadastro.web.dto.StatusCountDTO;
import com.oficina.cadastro.web.dto.TopClienteDTO;
import com.oficina.cadastro.service.DashboardOwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard/owner")
@RequiredArgsConstructor
@PreAuthorize("hasRole('DONO')")
public class DashboardOwnerController {

    private final DashboardOwnerService dashboardService;

    @GetMapping("/kpis")
    public ResponseEntity<DashboardKPIsDTO> getKPIs() {
        return ResponseEntity.ok(dashboardService.calcularKPIs());
    }

    @GetMapping("/ordens-por-status")
    public ResponseEntity<List<StatusCountDTO>> getOrdensPorStatus() {
        return ResponseEntity.ok(dashboardService.contarOrdensPorStatus());
    }

    @GetMapping("/receita-mensal")
    public ResponseEntity<List<ReceitaMensalDTO>> getReceitaMensal(
            @RequestParam(defaultValue = "2026") int ano) {
        return ResponseEntity.ok(dashboardService.calcularReceitaMensal(ano));
    }

    @GetMapping("/top-clientes")
    public ResponseEntity<List<TopClienteDTO>> getTopClientes(
            @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(dashboardService.getTopClientes(limit));
    }
}
