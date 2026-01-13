package com.oficina.cadastro.service;

import com.oficina.cadastro.model.VerificationCode;
import com.oficina.cadastro.repository.VerificationCodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.mail.internet.MimeMessage;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@Primary
public class MockEmailService {

    private static final Logger logger = LoggerFactory.getLogger(MockEmailService.class);
    private static final int CODE_LENGTH = 6;
    private static final int CODE_EXPIRATION_MINUTES = 10;

    @Value("${spring.mail.username:noreply@eletrotecnicamauricio.com}")
    private String fromEmail;

    @Value("${sendgrid.from.name:Eletrotecnica Mauricio}")
    private String fromName;

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @Autowired
    private JavaMailSender mailSender;

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
        return verificationCodeRepository.save(verificationCode);
    }

    public boolean sendVerificationEmail(String toEmail, String code) {
        try {
            // Para desenvolvimento, vamos salvar o código em um arquivo e logar
            saveCodeToFile(toEmail, code);
            
            // Tentar enviar email via SMTP (Gmail)
            try {
                return sendViaGmail(toEmail, code);
            } catch (Exception e) {
                logger.warn("Falha ao enviar email via Gmail, usando mock: {}", e.getMessage());
                // Se falhar, continuar com o mock
                return true;
            }
        } catch (Exception e) {
            logger.error("Erro ao enviar email mock para {}: {}", toEmail, e.getMessage());
            return false;
        }
    }

    private boolean sendViaGmail(String toEmail, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail, fromName);
            helper.setTo(toEmail);
            helper.setSubject("Codigo de Verificacao - Eletrotecnica Mauricio");
            
            String htmlContent = buildEmailTemplate(code);
            helper.setText(htmlContent, true);
            
            mailSender.send(message);
            logger.info("Email enviado com sucesso para {} via Gmail", toEmail);
            return true;
        } catch (Exception e) {
            logger.error("Erro ao enviar email via Gmail: {}", e.getMessage());
            throw new RuntimeException("Falha ao enviar email via Gmail", e);
        }
    }

    private void saveCodeToFile(String email, String code) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String filename = "verification_code_" + email.replace("@", "_at_") + "_" + timestamp + ".txt";
            
            FileWriter writer = new FileWriter(filename);
            writer.write("Email: " + email + "\n");
            writer.write("Código: " + code + "\n");
            writer.write("Data/Hora: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + "\n");
            writer.write("Este código expira em 10 minutos\n");
            writer.close();
            
            logger.info("Código de verificação salvo em arquivo: {}", filename);
            logger.info("📧 Código para {}: {} (arquivo: {})", email, code, filename);
        } catch (IOException e) {
            logger.error("Erro ao salvar código em arquivo: {}", e.getMessage());
        }
    }

    @Transactional
    public boolean verifyCode(String email, String code) {
        Optional<VerificationCode> verificationCode = 
            verificationCodeRepository.findTopByEmailAndUsedFalseOrderByCreatedAtDesc(email);
        
        if (verificationCode.isPresent() && verificationCode.get().isValid(code)) {
            VerificationCode vc = verificationCode.get();
            vc.setUsed(true);
            verificationCodeRepository.save(vc);
            return true;
        }
        return false;
    }

    private String buildEmailTemplate(String code) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 20px; }
                    .container { max-width: 500px; margin: 0 auto; background: white; border-radius: 10px; padding: 40px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
                    .header { text-align: center; margin-bottom: 30px; }
                    .logo { font-size: 24px; font-weight: bold; color: #FF6B35; }
                    .code-box { background: #FF6B35; color: white; font-size: 32px; font-weight: bold; letter-spacing: 8px; text-align: center; padding: 20px; border-radius: 8px; margin: 20px 0; }
                    .message { color: #333; font-size: 16px; line-height: 1.6; text-align: center; }
                    .warning { color: #666; font-size: 12px; text-align: center; margin-top: 20px; }
                    .footer { text-align: center; margin-top: 30px; color: #999; font-size: 12px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <div class="logo">Eletrotecnica Mauricio</div>
                    </div>
                    <p class="message">Seu codigo de verificacao e:</p>
                    <div class="code-box">%s</div>
                    <p class="message">Use este codigo para confirmar sua conta.</p>
                    <p class="warning">Este codigo expira em 10 minutos. Nao compartilhe com ninguem.</p>
                    <div class="footer">
                        <p>Se voce nao solicitou este codigo, ignore este email.</p>
                        <p>&copy; 2025 Eletrotecnica Mauricio</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(code);
    }
}