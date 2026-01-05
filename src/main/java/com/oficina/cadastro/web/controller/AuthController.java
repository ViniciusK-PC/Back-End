package com.oficina.cadastro.web.controller;

import com.oficina.cadastro.service.AuthService;
import com.oficina.cadastro.web.dto.LoginRequest;
import com.oficina.cadastro.web.dto.LoginResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/login")
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Map<String, String> loginGet() {
        return Map.of(
                "error", "Método não permitido",
                "message", "Use POST para fazer login. Este endpoint aceita apenas requisições POST.",
                "method", "POST",
                "endpoint", "/api/auth/login"
        );
    }
}

