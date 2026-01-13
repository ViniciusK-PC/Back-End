package com.oficina.cadastro.controller;

import com.oficina.cadastro.model.VerificationCode;
import com.oficina.cadastro.repository.VerificationCodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/debug")
public class DebugController {

    private static final Logger logger = LoggerFactory.getLogger(DebugController.class);

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @GetMapping("/verification-codes/{email}")
    public ResponseEntity<Map<String, Object>> getVerificationCodes(@PathVariable String email) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<VerificationCode> latestCode = 
                verificationCodeRepository.findTopByEmailAndUsedFalseOrderByCreatedAtDesc(email);
            
            if (latestCode.isPresent()) {
                VerificationCode code = latestCode.get();
                response.put("success", true);
                response.put("email", email);
                response.put("code", code.getCode());
                response.put("createdAt", code.getCreatedAt());
                response.put("expiresAt", code.getExpiresAt());
                response.put("used", code.isUsed());
                response.put("expired", code.isExpired());
                
                logger.info("Debug: Código de verificação para {}: {}", email, code.getCode());
            } else {
                response.put("success", false);
                response.put("message", "Nenhum código ativo encontrado para este email");
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erro ao buscar códigos de verificação: {}", e.getMessage());
            response.put("success", false);
            response.put("message", "Erro interno");
            return ResponseEntity.status(500).body(response);
        }
    }

    @DeleteMapping("/verification-codes/{email}")
    public ResponseEntity<Map<String, Object>> deleteVerificationCodes(@PathVariable String email) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Marcar todos os códigos do email como usados
            var codes = verificationCodeRepository.findByEmailAndUsedFalse(email);
            codes.forEach(code -> code.setUsed(true));
            verificationCodeRepository.saveAll(codes);
            
            response.put("success", true);
            response.put("message", "Códigos de verificação limpos com sucesso");
            response.put("count", codes.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erro ao limpar códigos de verificação: {}", e.getMessage());
            response.put("success", false);
            response.put("message", "Erro interno");
            return ResponseEntity.status(500).body(response);
        }
    }
}