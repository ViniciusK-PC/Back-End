package com.oficina.cadastro.controller;

import com.oficina.cadastro.service.VerificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/verification")
public class VerificationController {
    private final VerificationService verificationService;

    public VerificationController(VerificationService verificationService) {
        this.verificationService = verificationService;
    }

    @PostMapping("/send-code")
    public ResponseEntity<?> sendCode(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Email inválido"));
        }
        boolean ok = verificationService.sendCode(email);
        if (ok) return ResponseEntity.ok(Map.of("success", true));
        return ResponseEntity.status(400).body(Map.of("success", false, "message", "Email não verificável pelo serviço ou falha no envio"));
    }

    @PostMapping("/validate-code")
    public ResponseEntity<?> validateCode(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String code = body.get("code");
        if (email == null || code == null) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Dados insuficientes"));
        }
        boolean ok = verificationService.validateCode(email, code);
        if (ok) return ResponseEntity.ok(Map.of("success", true));
        return ResponseEntity.status(400).body(Map.of("success", false, "message", "Código inválido ou expirado"));
    }
}

