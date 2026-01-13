package com.oficina.cadastro.service;

import com.oficina.cadastro.model.VerificationCode;
import com.oficina.cadastro.repository.VerificationCodeRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@Service
public class VerificationService {
    private final VerificationCodeRepository repo;
    private final EmailValidationService emailValidationService;
    private final SendGridEmailService sendGridEmailService;

    public VerificationService(VerificationCodeRepository repo,
                               EmailValidationService emailValidationService,
                               SendGridEmailService sendGridEmailService) {
        this.repo = repo;
        this.emailValidationService = emailValidationService;
        this.sendGridEmailService = sendGridEmailService;
    }

    public boolean sendCode(String email) {
        if (!emailValidationService.isDeliverable(email)) {
            return false;
        }
        String code = String.format("%06d", new Random().nextInt(1_000_000));
        Instant now = Instant.now();
        VerificationCode vc = new VerificationCode();
        vc.setEmail(email);
        vc.setCode(code);
        vc.setCreatedAt(now);
        vc.setExpiresAt(now.plus(10, ChronoUnit.MINUTES));
        vc.setUsed(false);
        repo.save(vc);

        String subject = "Código de Verificação";
        String body = "<p>Seu código é: <strong>" + code + "</strong></p><p>Expira em 10 minutos.</p>";
        return sendGridEmailService.sendVerificationEmail(email, subject, body);
    }

    public boolean validateCode(String email, String code) {
        return repo.findByEmailAndCode(email, code)
                .map(vc -> {
                    Instant now = Instant.now();
                    if (vc.isUsed() || now.isAfter(vc.getExpiresAt())) {
                        return false;
                    }
                    vc.setUsed(true);
                    repo.save(vc);
                    return true;
                })
                .orElse(false);
    }
}

