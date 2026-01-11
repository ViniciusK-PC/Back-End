package com.oficina.cadastro.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/")
    public String health() {
        return "Backend is Online!";
    }

    @GetMapping("/api/health")
    public String apiHealth() {
        return "API is Online!";
    }
}
