package com.oficina.cadastro.web.controller;

import com.oficina.cadastro.service.DashboardService;
import com.oficina.cadastro.web.dto.DashboardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<DashboardResponse> obterDashboard() {
        return ResponseEntity.ok(dashboardService.obterDashboard());
    }
}











