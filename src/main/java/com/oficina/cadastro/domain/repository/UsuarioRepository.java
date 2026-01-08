package com.oficina.cadastro.domain.repository;

import com.oficina.cadastro.domain.model.Usuario;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

public interface UsuarioRepository extends org.springframework.data.jpa.repository.JpaRepository<Usuario, UUID> {

    Optional<Usuario> findByEmail(String email);
}
