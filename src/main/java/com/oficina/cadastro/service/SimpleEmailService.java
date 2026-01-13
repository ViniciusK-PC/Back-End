package com.oficina.cadastro.service;

import com.oficina.cadastro.model.VerificationCode;
import com.oficina.cadastro.repository.VerificationCodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Primary
public class SimpleEmailService {

    private static final Logger logger = LoggerFactory.getLogger(SimpleEmailService.class);
    private static final int CODE_LENGTH = 6;
    private static final int CODE_EXPIRATION_MINUTES = 10;
    
    // Armazenar códigos em memória para desenvolvimento
    private static final Map<String, List<CodeData>> tempCodes = new HashMap<>();

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    public static class CodeData {
        public String code;
        public LocalDateTime createdAt;
        public boolean used;

        public CodeData(String code) {
            this.code = code;
            this.createdAt = LocalDateTime.now();
            this.used = false;
        }

        public boolean isExpired() {
            return LocalDateTime.now().isAfter(createdAt.plusMinutes(CODE_EXPIRATION_MINUTES));
        }

        public boolean isValid(String inputCode) {
            return !used && !isExpired() && code.equals(inputCode);
        }
    }

    public String generateCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }

    @Transactional
    public VerificationCode createVerificationCode(String email) {
        String code = generateCode();
        VerificationCode verificationCode = new VerificationCode(email, code, CODE_EXPIRATION_MINUTES);
        
        // Salvar também em memória para fácil acesso
        List<CodeData> codes = tempCodes.getOrDefault(email, new ArrayList<>());
        codes.add(new CodeData(code));
        tempCodes.put(email, codes);
        
        return verificationCodeRepository.save(verificationCode);
    }

    public boolean sendVerificationEmail(String toEmail, String code) {
        try {
            // Logar o código no console para desenvolvimento
            logger.info("=========================================");
            logger.info("📧 EMAIL DE VERIFICAÇÃO SIMULADO");
            logger.info("Para: {}", toEmail);
            logger.info("Código: {}", code);
            logger.info("Data/Hora: {}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            logger.info("Este código expira em 10 minutos");
            logger.info("=========================================");
            
            // Também mostrar em um formato fácil de copiar
            logger.info("🎯 CÓDIGO PARA {}: {}", toEmail, code);
            
            return true;
        } catch (Exception e) {
            logger.error("Erro ao simular envio de email para {}: {}", toEmail, e.getMessage());
            return false;
        }
    }

    @Transactional
    public boolean verifyCode(String email, String code) {
        // Verificar no banco de dados primeiro
        Optional<VerificationCode> verificationCode = 
            verificationCodeRepository.findTopByEmailAndUsedFalseOrderByCreatedAtDesc(email);
        
        if (verificationCode.isPresent() && verificationCode.get().isValid(code)) {
            VerificationCode vc = verificationCode.get();
            vc.setUsed(true);
            verificationCodeRepository.save(vc);
            
            // Marcar como usado na memória também
            markCodeAsUsed(email, code);
            
            return true;
        }
        
        // Se não encontrar no banco, verificar na memória (para desenvolvimento)
        return verifyCodeInMemory(email, code);
    }

    private boolean verifyCodeInMemory(String email, String code) {
        List<CodeData> codes = tempCodes.get(email);
        if (codes != null) {
            for (CodeData codeData : codes) {
                if (codeData.isValid(code)) {
                    codeData.used = true;
                    return true;
                }
            }
        }
        return false;
    }

    private void markCodeAsUsed(String email, String code) {
        List<CodeData> codes = tempCodes.get(email);
        if (codes != null) {
            for (CodeData codeData : codes) {
                if (codeData.code.equals(code)) {
                    codeData.used = true;
                    break;
                }
            }
        }
    }

    // Método utilitário para debug
    public void printAllCodes() {
        logger.info("=== CÓDIGOS EM MEMÓRIA ===");
        for (Map.Entry<String, List<CodeData>> entry : tempCodes.entrySet()) {
            logger.info("Email: {}", entry.getKey());
            for (CodeData codeData : entry.getValue()) {
                logger.info("  Código: {} | Criado: {} | Usado: {} | Expirado: {}", 
                    codeData.code, 
                    codeData.createdAt.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                    codeData.used,
                    codeData.isExpired());
            }
        }
        logger.info("=========================");
    }
}