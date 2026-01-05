package com.oficina.cadastro.service;

import com.oficina.cadastro.domain.model.Usuario;
import com.oficina.cadastro.domain.repository.UsuarioRepository;
import com.oficina.cadastro.security.JwtTokenProvider;
import com.oficina.cadastro.web.dto.LoginRequest;
import com.oficina.cadastro.web.dto.LoginResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@lombok.extern.slf4j.Slf4j
public class AuthService {

        private final UsuarioRepository usuarioRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtTokenProvider tokenProvider;
        private final AuthenticationManager authenticationManager;

        @Transactional(readOnly = true)
        public LoginResponse login(LoginRequest request) {
                log.info("Tentativa de login para o email: {}", request.getEmail());
                try {
                        Authentication authentication = authenticationManager.authenticate(
                                        new UsernamePasswordAuthenticationToken(
                                                        request.getEmail(), request.getSenha()));

                        log.info("Autenticação bem-sucedida para o email: {}", request.getEmail());
                        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                        Usuario usuario = usuarioRepository
                                        .findByEmail(userDetails.getUsername())
                                        .orElseThrow(
                                                        () -> new EntityNotFoundException(
                                                                        "Usuário não encontrado: "
                                                                                        + userDetails.getUsername()));

                        String token = tokenProvider.generateToken(
                                        usuario.getId(), usuario.getEmail(), usuario.getPerfil().name());

                        log.info("Token gerado com sucesso para o usuário: {}", usuario.getEmail());
                        return LoginResponse.builder()
                                        .token(token)
                                        .usuarioId(usuario.getId())
                                        .nome(usuario.getNome())
                                        .email(usuario.getEmail())
                                        .perfil(usuario.getPerfil())
                                        .build();
                } catch (BadCredentialsException e) {
                        log.error("Falha na autenticação (credenciais inválidas) para o email: {}", request.getEmail());
                        throw new BadCredentialsException("Email ou senha inválidos");
                } catch (Exception e) {
                        log.error("Erro inesperado durante o login para o email: {}", request.getEmail(), e);
                        throw e;
                }
        }
}
