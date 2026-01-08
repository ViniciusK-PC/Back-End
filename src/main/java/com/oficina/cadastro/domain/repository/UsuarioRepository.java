package com.oficina.cadastro.domain.repository;

import com.oficina.cadastro.domain.model.Usuario;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

public interface UsuarioRepository {

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findById(UUID id);

    Usuario save(Usuario usuario);

    List<Usuario> findAll();

    void deleteById(UUID id);

    long count();
}
