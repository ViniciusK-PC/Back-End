package com.oficina.cadastro.controller;

import com.oficina.cadastro.model.VerificationCode;
import com.oficina.cadastro.service.SendGridEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/verification")
public class VerificationController {

    private static final Logger logger = LoggerFactory.getLogger(VerificationController.class);

    @Autowired
    private SendGridEmailService emailService;

    @PostMapping("/send-code")
    public ResponseEntity<Map<String, Object>> sendVerificationCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        Map<String, Object> response = new HashMap<>();

        if (email == null || email.trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "E-mail e obrigatorio");
            return ResponseEntity.badRequest().body(response);
        }

        logger.info("Solicitacao de codigo de verificacao para: {}", email);

        try {
            VerificationCode verificationCode = emailService.createVerificationCode(email.trim());
            boolean emailSent = emailService.sendVerificationEmail(email.trim(), verificationCode.getCode());

            if (emailSent) {
                response.put("success", true);
                response.put("message", "Codigo enviado para o e-mail");
                response.put("expiresIn", 10);
                logger.info("Codigo de verificacao enviado para: {}", email);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Erro ao enviar e-mail. Tente novamente.");
                return ResponseEntity.status(500).body(response);
            }
        } catch (Exception e) {
            logger.error("Erro ao enviar codigo para {}: {}", email, e.getMessage());
            response.put("success", false);
            response.put("message", "Erro interno. Tente novamente.");
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/verify-code")
    public ResponseEntity<Map<String, Object>> verifyCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = request.get("code");
        Map<String, Object> response = new HashMap<>();

        if (email == null || code == null) {
            response.put("success", false);
            response.put("message", "E-mail e codigo sao obrigatorios");
            return ResponseEntity.badRequest().body(response);
        }

        logger.info("Verificando codigo para: {}", email);

        boolean isValid = emailService.verifyCode(email.trim(), code.trim());

        if (isValid) {
            response.put("success", true);
            response.put("message", "Codigo verificado com sucesso");
            response.put("verified", true);
            logger.info("Codigo verificado com sucesso para: {}", email);
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Codigo invalido ou expirado");
            response.put("verified", false);
            logger.warn("Codigo invalido para: {}", email);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/resend-code")
    public ResponseEntity<Map<String, Object>> resendCode(@RequestBody Map<String, String> request) {
        return sendVerificationCode(request);
    }
}
