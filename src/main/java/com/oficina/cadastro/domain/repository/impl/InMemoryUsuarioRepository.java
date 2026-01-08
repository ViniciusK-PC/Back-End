package com.oficina.cadastro.domain.repository.impl;

import com.oficina.cadastro.domain.enums.PerfilUsuario;
import com.oficina.cadastro.domain.model.Usuario;
import com.oficina.cadastro.domain.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryUsuarioRepository implements UsuarioRepository {

    private final Map<UUID, Usuario> storage = new ConcurrentHashMap<>();
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public InMemoryUsuarioRepository() {
        // Initial user will be created by DataInitializer, but we can have one here too
        // if we want
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        return storage.values().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    @Override
    public Optional<Usuario> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Usuario save(Usuario usuario) {
        if (usuario.getId() == null) {
            usuario.setId(UUID.randomUUID());
            usuario.setCriadoEm(OffsetDateTime.now());
        }
        usuario.setAtualizadoEm(OffsetDateTime.now());
        storage.put(usuario.getId(), usuario);
        return usuario;
    }

    @Override
    public List<Usuario> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void deleteById(UUID id) {
        storage.remove(id);
    }

    @Override
    public long count() {
        return storage.size();
    }
}
