package com.oficina.cadastro.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

