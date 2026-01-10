package com.oficina.cadastro.painel.controller;

import com.oficina.cadastro.painel.model.Dono;
import com.oficina.cadastro.painel.repository.DonoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/painel/auth")
public class PainelAuthController {

    private final DonoRepository donoRepository;
    private final PasswordEncoder passwordEncoder;

    public PainelAuthController(DonoRepository donoRepository, PasswordEncoder passwordEncoder) {
        this.donoRepository = donoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        Optional<Dono> donoOpt = donoRepository.findByUsername(username);

        if (donoOpt.isPresent()) {
            Dono dono = donoOpt.get();
            if (passwordEncoder.matches(password, dono.getPassword())) {
                // Sucesso - Retorna um token simples ou confirmação
                // Como solicitado para ser isolado e não mexer nos serviços,
                // vamos retornar um sucesso simples e talvez um indicador pro frontend.
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Login privado realizado com sucesso");
                response.put("user", username);
                response.put("role", "DONO_PRIVADO");
                // Em um cenário real, geraria um JWT específico aqui

                return ResponseEntity.ok(response);
            }
        }

        return ResponseEntity.status(401).body(Map.of("success", false, "message", "Credenciais inválidas"));
    }

    // Endpoint auxiliar para criar o primeiro dono (apenas para teste/setup)
    @PostMapping("/setup")
    public ResponseEntity<?> setup(@RequestBody Map<String, String> data) {
        String username = data.get("username");
        String password = data.get("password");

        if (donoRepository.findByUsername(username).isPresent()) {
            return ResponseEntity.badRequest().body("Dono já existe");
        }

        Dono novoDono = new Dono(username, passwordEncoder.encode(password));
        donoRepository.save(novoDono);

        return ResponseEntity.ok("Dono criado com sucesso");
    }
}
