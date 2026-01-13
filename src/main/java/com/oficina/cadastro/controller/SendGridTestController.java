package com.oficina.cadastro.controller;

import com.oficina.cadastro.service.SendGridEmailService;
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
    private SendGridEmailService emailService;

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
            logger.info("🧪 Testando SendGrid com email: {}", email);
            
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
            logger.error("💥 Erro ao testar SendGrid: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "Erro interno: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/sendgrid-status")
    public ResponseEntity<Map<String, Object>> getSendGridStatus() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Verificar se a chave SendGrid está configurada
            String apiKey = System.getProperty("sendgrid.api.key", "");
            if (apiKey.isEmpty()) {
                apiKey = System.getenv("SENDGRID_API_KEY");
            }
            
            boolean isConfigured = apiKey != null && !apiKey.trim().isEmpty();
            
            response.put("success", true);
            response.put("sendgridConfigured", isConfigured);
            response.put("apiKeyLength", isConfigured ? apiKey.length() : 0);
            response.put("fromEmail", "tecmau@gmail.com");
            response.put("fromName", "Eletrotecnica Mauricio");
            
            logger.info("📊 Status SendGrid - Configurado: {}", isConfigured);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("💥 Erro ao verificar status do SendGrid: {}", e.getMessage());
            response.put("success", false);
            response.put("message", "Erro ao verificar status");
            return ResponseEntity.status(500).body(response);
        }
    }
}