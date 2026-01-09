package com.oficina.cadastro.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.oficina.cadastro.domain.enums.PerfilUsuario;
import com.oficina.cadastro.domain.model.Usuario;
import com.oficina.cadastro.domain.repository.UsuarioRepository;
import com.oficina.cadastro.security.JwtTokenProvider;
import com.oficina.cadastro.web.dto.LoginRequest;
import com.oficina.cadastro.web.dto.LoginResponse;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

        @Mock
        private UsuarioRepository usuarioRepository;

        @Mock
        private PasswordEncoder passwordEncoder;

        @Mock
        private JwtTokenProvider tokenProvider;

        @Mock
        private AuthenticationManager authenticationManager;

        @InjectMocks
        private AuthService authService;

        private Usuario usuario;
        private Long usuarioId;
        private LoginRequest loginRequest;

        @BeforeEach
        void setUp() {
                usuarioId = 1L;
                usuario = Usuario.builder()
                                .id(usuarioId)
                                .nome("Admin")
                                .email("admin@example.com")
                                .senhaHash("$2a$10$encoded")
                                .perfil(PerfilUsuario.GERENTE)
                                .ativo(true)
                                .build();

                loginRequest = new LoginRequest("admin@example.com", "senha123");
        }

        @Test
        void deveFazerLoginComSucesso() {
                UserDetails userDetails = User.builder()
                                .username("admin@example.com")
                                .password("$2a$10$encoded")
                                .authorities(java.util.Collections.singletonList(
                                                new org.springframework.security.core.authority.SimpleGrantedAuthority(
                                                                "ROLE_GERENTE")))
                                .build();

                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

                when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                                .thenReturn(authentication);
                when(usuarioRepository.findByEmail("admin@example.com"))
                                .thenReturn(Optional.of(usuario));
                when(tokenProvider.generateToken(any(Long.class), anyString(), anyString()))
                                .thenReturn("token-jwt-123");

                LoginResponse response = authService.login(loginRequest);

                assertNotNull(response);
                assertEquals("token-jwt-123", response.getToken());
                assertEquals(usuarioId, response.getUsuarioId());
                assertEquals("Admin", response.getNome());
                verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
                verify(tokenProvider).generateToken(usuarioId, "admin@example.com", "GERENTE");
        }

        @Test
        void deveLancarExcecaoQuandoCredenciaisInvalidas() {
                when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                                .thenThrow(new BadCredentialsException("Credenciais inválidas"));

                assertThrows(
                                BadCredentialsException.class,
                                () -> authService.login(loginRequest),
                                "Email ou senha inválidos");
        }
}
