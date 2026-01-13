package com.oficina.cadastro.repository;

import com.oficina.cadastro.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    Optional<VerificationCode> findTopByEmailAndUsedFalseOrderByCreatedAtDesc(String email);
    List<VerificationCode> findByEmailAndUsedFalse(String email);
    void deleteByEmail(String email);
}
