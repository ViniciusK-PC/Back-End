package com.oficina.cadastro.painel.repository;

import com.oficina.cadastro.painel.model.Dono;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DonoRepository extends JpaRepository<Dono, Long> {
    Optional<Dono> findByUsername(String username);
}
