package com.oficina.cadastro.service;

import com.oficina.cadastro.model.VerificationCode;
import com.oficina.cadastro.repository.VerificationCodeRepository;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@Primary
public class SendGridEmailService {

    private static final Logger logger = LoggerFactory.getLogger(SendGridEmailService.class);
    private static final int CODE_LENGTH = 6;
    private static final int CODE_EXPIRATION_MINUTES = 10;

    @Value("${sendgrid.api.key:}")
    private String sendGridApiKey;

    @Value("${sendgrid.from.email:tecmau@gmail.com}")
    private String fromEmail;

    @Value("${sendgrid.from.name:Eletrotecnica Mauricio}")
    private String fromName;

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

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
            // **MOSTRAR CÓDIGO NO CONSOLE** - Essencial para debug/produção
            logger.info("=========================================");
            logger.info("📧 VERIFICAÇÃO DE EMAIL - CÓDIGO GERADO");
            logger.info("Para: {}", toEmail);
            logger.info("🎯 CÓDIGO: {}", code);
            logger.info("Data/Hora: {}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            logger.info("Este código expira em 10 minutos");
            logger.info("=========================================");
            
            // Verificar configuração do SendGrid
            logger.info("Verificando configuração SendGrid...");
            logger.info("API Key presente: {}", sendGridApiKey != null && !sendGridApiKey.trim().isEmpty());
            logger.info("From Email: {}", fromEmail);
            logger.info("From Name: {}", fromName);
            
            // Tentar enviar email real apenas se tiver SendGrid configurado
            if (sendGridApiKey != null && !sendGridApiKey.trim().isEmpty()) {
                logger.info("SendGrid configurado, enviando email...");
                return sendEmailViaSendGrid(toEmail, code);
            } else {
                logger.warn("SendGrid não configurado. Usando fallback de console.");
                return true; // Simula sucesso para não quebrar o fluxo
            }
        } catch (Exception e) {
            logger.error("Erro ao enviar email para {}: {}", toEmail, e.getMessage());
            return false;
        }
    }

    private boolean sendEmailViaSendGrid(String toEmail, String code) {
        try {
            logger.info("Preparando email via SendGrid para: {}", toEmail);
            
            Email from = new Email(fromEmail, fromName);
            Email to = new Email(toEmail);
            String subject = "Código de Verificação - Eletrotecnica Mauricio";
            
            logger.info("Remetente: {} ({})", fromEmail, fromName);
            logger.info("Destinatário: {}", toEmail);
            logger.info("Assunto: {}", subject);
            
            // Criar conteúdo HTML
            String htmlContent = buildEmailTemplate(code);
            Content content = new Content("text/html", htmlContent);
            
            // Criar email
            Mail mail = new Mail(from, subject, to, content);
            
            logger.info("Email criado com sucesso, preparando envio...");
            
            // Enviar email
            SendGrid sg = new SendGrid(sendGridApiKey);
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            
            logger.info("Enviando requisição para SendGrid...");
            Response response = sg.api(request);
            
            logger.info("Resposta SendGrid - Status: {}, Body: {}", response.getStatusCode(), response.getBody());
            
            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                logger.info("✅ Email enviado com sucesso via SendGrid para {} (Status: {})", toEmail, response.getStatusCode());
                return true;
            } else {
                logger.error("❌ Erro ao enviar email via SendGrid. Status: {}, Body: {}", response.getStatusCode(), response.getBody());
                return false;
            }
            
        } catch (IOException e) {
            logger.error("💥 Erro de IO ao enviar email via SendGrid: {}", e.getMessage(), e);
            return false;
        } catch (Exception e) {
            logger.error("💥 Erro inesperado ao enviar email via SendGrid: {}", e.getMessage(), e);
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
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
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
                <div class="container">
                    <div class="header">
                        <div class="logo">⚡ Eletrotecnica Mauricio</div>
                        <div class="subtitle">Segurança e Confiança</div>
                    </div>
                    
                    <p class="message">Olá! 👋</p>
                    <p class="message">Você solicitou um código de verificação para acessar nossa plataforma.</p>
                    
                    <div class="code-box">%s</div>
                    
                    <p class="message">Use este código para confirmar sua conta e continuar com o cadastro.</p>
                    
                    <div class="security-note">
                        🔒 <strong>Dica de segurança:</strong> Nunca compartilhe este código com ninguém. 
                        Ele é exclusivo para você e garante a segurança da sua conta.
                    </div>
                    
                    <div class="warning">
                        ⚠️ <strong>Importante:</strong> Este código expira em 10 minutos. 
                        Se não solicitou este código, por favor ignore este email.
                    </div>
                    
                    <div class="footer">
                        <p>Se tiver dúvidas, entre em contato conosco.</p>
                        <p>&copy; 2025 Eletrotecnica Mauricio. Todos os direitos reservados.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(code);
    }
}