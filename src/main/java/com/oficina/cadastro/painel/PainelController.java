package com.oficina.cadastro.painel;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/painel")
public class PainelController {

    @GetMapping("/status")
    public ResponseEntity<Map<String, String>> getPainelStatus() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "ativo");
        response.put("mensagem", "Módulo Painel operacional e isolado.");
        return ResponseEntity.ok(response);
    }
}
