package com.oficina.cadastro.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller utilitário para gerar hashes BCrypt.
 * ATENÇÃO: Remova ou proteja este endpoint em produção!
 */
@RestController
@RequestMapping("/api/util")
@RequiredArgsConstructor
public class UtilController {

    private final PasswordEncoder passwordEncoder;

    @PostMapping("/gerar-hash")
    public String gerarHash(@RequestBody String senha) {
        return passwordEncoder.encode(senha);
    }

    @GetMapping("/version")
    public Map<String, Object> getVersion() {
        Map<String, Object> version = new HashMap<>();
        version.put("version", "1.1.0-cors-fix");
        version.put("timestamp", LocalDateTime.now().toString());
        version.put("corsFilterRemoved", true);
        version.put("message", "CorsFilter removed - using CorsConfig only");
        return version;
    }
}

