package com.oficina.cadastro.security;

import com.oficina.cadastro.domain.model.Usuario;
import com.oficina.cadastro.domain.repository.UsuarioRepository;
import java.util.Collections;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario =
                usuarioRepository
                        .findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

        if (!usuario.isAtivo()) {
            throw new UsernameNotFoundException("Usuário inativo: " + email);
        }

        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getSenhaHash())
                .authorities(
                        Collections.singletonList(
                                new SimpleGrantedAuthority("ROLE_" + usuario.getPerfil().name())))
                .build();
    }

    @Transactional(readOnly = true)
    public UserDetails loadUserById(UUID id) {
        Usuario usuario =
                usuarioRepository
                        .findById(id)
                        .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + id));

        if (!usuario.isAtivo()) {
            throw new UsernameNotFoundException("Usuário inativo: " + id);
        }

        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getSenhaHash())
                .authorities(
                        Collections.singletonList(
                                new SimpleGrantedAuthority("ROLE_" + usuario.getPerfil().name())))
                .build();
    }
}

