package com.oficina.cadastro.service;

import com.oficina.cadastro.domain.model.Usuario;
import com.oficina.cadastro.domain.repository.UsuarioRepository;
import com.oficina.cadastro.web.dto.UsuarioRequest;
import com.oficina.cadastro.web.dto.UsuarioResponse;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UsuarioResponse> listar(String nome, String email,
            com.oficina.cadastro.domain.enums.PerfilUsuario perfil, Boolean ativo) {
        return usuarioRepository.findByFiltros(nome, email, perfil, ativo).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public UsuarioResponse buscar(UUID id) {
        return toResponse(findOrThrow(id));
    }

    public UsuarioResponse criar(UsuarioRequest request) {
        if (request.senha() == null || request.senha().isBlank()) {
            throw new RuntimeException("A senha é obrigatória para novos usuários");
        }

        Usuario usuario = Usuario.builder()
                .nome(request.nome())
                .email(request.email())
                .perfil(request.perfil())
                .ativo(request.ativo() == null || request.ativo())
                .senhaHash(passwordEncoder.encode(request.senha()))
                .build();
        return toResponse(usuarioRepository.save(usuario));
    }

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
        return toResponse(usuarioRepository.save(usuario));
    }

    public void deletar(UUID id) {
        Usuario usuario = findOrThrow(id);
        usuarioRepository.delete(usuario);
    }

    @org.springframework.transaction.annotation.Transactional
    public UsuarioResponse alternarAtivo(UUID id) {
        Usuario usuario = findOrThrow(id);
        usuario.setAtivo(!usuario.isAtivo());
        return toResponse(usuarioRepository.saveAndFlush(usuario));
    }

    private Usuario findOrThrow(UUID id) {
        return usuarioRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    private UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPerfil(),
                usuario.isAtivo(),
                usuario.getSenhaHash(),
                usuario.getCriadoEm(),
                usuario.getAtualizadoEm());
    }
}
