package com.oficina.cadastro.service;

import com.oficina.cadastro.domain.model.Usuario;
import com.oficina.cadastro.domain.repository.UsuarioRepository;
import com.oficina.cadastro.web.dto.UsuarioRequest;
import com.oficina.cadastro.web.dto.UsuarioResponse;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listar() {
        return usuarioRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscar(UUID id) {
        return toResponse(findOrThrow(id));
    }

    @Transactional
    public UsuarioResponse criar(UsuarioRequest request) {
        Usuario usuario =
                Usuario.builder()
                        .nome(request.nome())
                        .email(request.email())
                        .perfil(request.perfil())
                        .ativo(request.ativo() == null || request.ativo())
                        .senhaHash(passwordEncoder.encode(request.senha()))
                        .build();
        return toResponse(usuarioRepository.save(usuario));
    }

    @Transactional
    public UsuarioResponse atualizar(UUID id, UsuarioRequest request) {
        Usuario usuario = findOrThrow(id);
        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        usuario.setPerfil(request.perfil());
        if (request.ativo() != null) {
            usuario.setAtivo(request.ativo());
        }
        if (request.senha() != null && !request.senha().isBlank()) {
            usuario.setSenhaHash(passwordEncoder.encode(request.senha()));
        }
        return toResponse(usuario);
    }

    private Usuario findOrThrow(UUID id) {
        return usuarioRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
    }

    private UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPerfil(),
                usuario.isAtivo(),
                usuario.getCriadoEm(),
                usuario.getAtualizadoEm());
    }
}

