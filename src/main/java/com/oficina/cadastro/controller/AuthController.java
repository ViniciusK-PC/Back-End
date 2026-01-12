package com.oficina.cadastro.controller;

import com.oficina.cadastro.model.User;
import com.oficina.cadastro.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody User user) {
        logger.info("Tentativa de registro para email: {}", user.getEmail());
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                response.put("success", false);
                response.put("message", "E-mail ja cadastrado!");
                return ResponseEntity.badRequest().body(response);
            }
            
            User savedUser = userRepository.save(user);
            logger.info("Usuario registrado com sucesso: {}", savedUser.getEmail());
            
            response.put("success", true);
            response.put("message", "Usuario cadastrado com sucesso!");
            response.put("user", savedUser);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erro ao registrar usuario: ", e);
            response.put("success", false);
            response.put("message", "Erro ao cadastrar: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/check-email")
    public ResponseEntity<Map<String, Object>> checkEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        Map<String, Object> response = new HashMap<>();
        
        if (email == null || email.trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "E-mail e obrigatorio");
            return ResponseEntity.badRequest().body(response);
        }

        logger.info("Verificando email: {}", email);
        Optional<User> user = userRepository.findByEmail(email.trim());
        
        if (user.isPresent()) {
            response.put("success", true);
            response.put("exists", true);
            response.put("message", "E-mail encontrado");
            return ResponseEntity.ok(response);
        }
        
        response.put("success", true);
        response.put("exists", false);
        response.put("message", "E-mail nao encontrado");
        return ResponseEntity.status(404).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");
        Map<String, Object> response = new HashMap<>();
        
        logger.info("Tentativa de login para email: {}", email);
        
        if (email == null || password == null) {
            response.put("success", false);
            response.put("message", "E-mail e senha sao obrigatorios");
            return ResponseEntity.badRequest().body(response);
        }
        
        Optional<User> user = userRepository.findByEmail(email.trim());
        
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            logger.info("Login realizado com sucesso: {}", email);
            response.put("success", true);
            response.put("message", "Login realizado com sucesso!");
            response.put("user", user.get());
            return ResponseEntity.ok(response);
        }
        
        logger.warn("Falha no login para: {}", email);
        response.put("success", false);
        response.put("message", "E-mail ou senha invalidos");
        return ResponseEntity.status(401).body(response);
    }
}
