package com.oficina.cadastro.service;

import com.oficina.cadastro.model.VerificationCode;
import com.oficina.cadastro.repository.VerificationCodeRepository;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@Primary
public class BrevoEmailService {

    private static final Logger logger = LoggerFactory.getLogger(BrevoEmailService.class);
    private static final int CODE_LENGTH = 6;
    private static final int CODE_EXPIRATION_MINUTES = 10;

    @Value("${brevo.from.email:noreply@eletrotecnicamauricio.com}")
    private String fromEmail;

    @Value("${brevo.from.name:Eletrotecnica Mauricio}")
    private String fromName;

    @Value("${spring.mail.username:}")
    private String smtpUsername;

    @Value("${spring.mail.password:}")
    private String smtpPassword;

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
        logger.info("=========================================");
        logger.info("📧 VERIFICAÇÃO DE EMAIL - CÓDIGO GERADO");
        logger.info("Para: {}", toEmail);
        logger.info("🎯 CÓDIGO: {}", code);
        logger.info("Data/Hora: {}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        logger.info("Este código expira em 10 minutos");
        logger.info("=========================================");

        if (smtpUsername == null || smtpUsername.isBlank() || smtpPassword == null || smtpPassword.isBlank()) {
            logger.error("Brevo SMTP não configurado (spring.mail.username/spring.mail.password)");
            return false;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail, fromName);
            helper.setTo(toEmail);
            helper.setSubject("Código de Verificação - Eletrotecnica Mauricio");
            helper.setText(buildEmailTemplate(code), true);

            mailSender.send(message);
            logger.info("✅ Email enviado com sucesso para {} via Brevo (SMTP)", toEmail);
            return true;
        } catch (Exception e) {
            logger.error("❌ Erro ao enviar email via Brevo (SMTP) para {}: {}", toEmail, e.getMessage());
            return false;
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
                <meta charset=\"UTF-8\">
                <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">
                <title>Código de Verificação</title>
                <style>
                    body {
                        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                        background-color: #f8f9fa;
                        margin: 0;
                        padding: 20px;
                        line-height: 1.6;
                    }
                    .container {
                        max-width: 500px;
                        margin: 0 auto;
                        background: white;
                        border-radius: 15px;
                        padding: 40px;
                        box-shadow: 0 10px 30px rgba(0,0,0,0.1);
                        border: 1px solid #e9ecef;
                    }
                    .header {
                        text-align: center;
                        margin-bottom: 30px;
                        border-bottom: 2px solid #FF6B35;
                        padding-bottom: 20px;
                    }
                    .logo {
                        font-size: 28px;
                        font-weight: bold;
                        color: #FF6B35;
                        margin-bottom: 10px;
                    }
                    .subtitle {
                        color: #6c757d;
                        font-size: 14px;
                    }
                    .code-box {
                        background: linear-gradient(135deg, #FF6B35, #FF8C42);
                        color: white;
                        font-size: 36px;
                        font-weight: bold;
                        letter-spacing: 10px;
                        text-align: center;
                        padding: 25px;
                        border-radius: 12px;
                        margin: 25px 0;
                        box-shadow: 0 5px 15px rgba(255, 107, 53, 0.3);
                    }
                    .message {
                        color: #495057;
                        font-size: 16px;
                        text-align: center;
                        margin: 20px 0;
                    }
                    .warning {
                        background: #fff3cd;
                        color: #856404;
                        font-size: 13px;
                        text-align: center;
                        margin-top: 25px;
                        padding: 15px;
                        border-radius: 8px;
                        border-left: 4px solid #ffc107;
                    }
                    .footer {
                        text-align: center;
                        margin-top: 30px;
                        padding-top: 20px;
                        border-top: 1px solid #e9ecef;
                        color: #6c757d;
                        font-size: 12px;
                    }
                    .security-note {
                        background: #d1ecf1;
                        color: #0c5460;
                        padding: 12px;
                        border-radius: 6px;
                        margin: 15px 0;
                        font-size: 12px;
                        border-left: 4px solid #17a2b8;
                    }
                    @media (max-width: 600px) {
                        .container {
                            padding: 20px;
                            margin: 10px;
                        }
                        .code-box {
                            font-size: 28px;
                            padding: 20px;
                        }
                    }
                </style>
            </head>
            <body>
                <div class=\"container\">
                    <div class=\"header\">
                        <div class=\"logo\">⚡ Eletrotecnica Mauricio</div>
                        <div class=\"subtitle\">Segurança e Confiança</div>
                    </div>

                    <p class=\"message\">Olá! 👋</p>
                    <p class=\"message\">Você solicitou um código de verificação para acessar nossa plataforma.</p>

                    <div class=\"code-box\">%s</div>

                    <p class=\"message\">Use este código para confirmar sua conta e continuar com o cadastro.</p>

                    <div class=\"security-note\">
                        🔒 <strong>Dica de segurança:</strong> Nunca compartilhe este código com ninguém.
                        Ele é exclusivo para você e garante a segurança da sua conta.
                    </div>

                    <div class=\"warning\">
                        ⚠️ <strong>Importante:</strong> Este código expira em 10 minutos.
                        Se não solicitou este código, por favor ignore este email.
                    </div>

                    <div class=\"footer\">
                        <p>Se tiver dúvidas, entre em contato conosco.</p>
                        <p>&copy; 2025 Eletrotecnica Mauricio. Todos os direitos reservados.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(code);
    }
}

