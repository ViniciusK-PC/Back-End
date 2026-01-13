package com.oficina.cadastro.repository;

import com.oficina.cadastro.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    Optional<VerificationCode> findTopByEmailOrderByCreatedAtDesc(String email);
    Optional<VerificationCode> findByEmailAndCode(String email, String code);
}

