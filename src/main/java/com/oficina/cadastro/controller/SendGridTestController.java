package com.oficina.cadastro.controller;

import com.oficina.cadastro.service.BrevoEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class SendGridTestController {

    private static final Logger logger = LoggerFactory.getLogger(SendGridTestController.class);

    @Autowired
    private BrevoEmailService emailService;

    @PostMapping("/sendgrid-test")
    public ResponseEntity<Map<String, Object>> testSendGridEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        Map<String, Object> response = new HashMap<>();

        if (email == null || email.trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "Email é obrigatório");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            logger.info("🧪 Testando envio de email (Brevo SMTP) com email: {}", email);
            
            // Criar código de teste
            String testCode = emailService.generateCode();
            logger.info("📧 Código de teste gerado: {}", testCode);
            
            // Tentar enviar email
            boolean sent = emailService.sendVerificationEmail(email.trim(), testCode);
            
            if (sent) {
                response.put("success", true);
                response.put("message", "Email de teste enviado com sucesso!");
                response.put("code", testCode);
                response.put("email", email);
                logger.info("✅ Email de teste enviado com sucesso para: {}", email);
            } else {
                response.put("success", false);
                response.put("message", "Falha ao enviar email. Verifique os logs.");
                response.put("email", email);
                logger.warn("❌ Falha ao enviar email de teste para: {}", email);
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("💥 Erro ao testar envio de email (Brevo SMTP): {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "Erro interno: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/sendgrid-status")
    public ResponseEntity<Map<String, Object>> getSendGridStatus() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String username = System.getProperty("spring.mail.username", "");
            if (username.isEmpty()) {
                username = System.getenv("BREVO_SMTP_USERNAME");
            }

            boolean isConfigured = username != null && !username.trim().isEmpty();
            
            response.put("success", true);
            response.put("brevoSmtpConfigured", isConfigured);
            response.put("usernameLength", isConfigured ? username.length() : 0);
            response.put("fromEmail", System.getenv().getOrDefault("BREVO_FROM_EMAIL", "tecmau@gmail.com"));
            response.put("fromName", System.getenv().getOrDefault("BREVO_FROM_NAME", "Eletrotecnica Mauricio"));

            logger.info("📊 Status Brevo SMTP - Configurado: {}", isConfigured);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("💥 Erro ao verificar status do Brevo SMTP: {}", e.getMessage());
            response.put("success", false);
            response.put("message", "Erro ao verificar status");
            return ResponseEntity.status(500).body(response);
        }
    }
}
