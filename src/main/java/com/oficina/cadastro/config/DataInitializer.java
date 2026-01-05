package com.oficina.cadastro.config;

import com.oficina.cadastro.domain.model.Usuario;
import com.oficina.cadastro.domain.repository.UsuarioRepository;
import com.oficina.cadastro.domain.enums.PerfilUsuario;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            if (usuarioRepository.count() == 0) {
                Usuario admin = new Usuario();
                admin.setNome("Administrador");
                admin.setEmail("admin@oficina.com");
                admin.setSenhaHash(passwordEncoder.encode("admin123"));
                admin.setPerfil(PerfilUsuario.GERENTE);
                admin.setAtivo(true);

                usuarioRepository.save(admin);
                System.out.println("Usuário Admin criado: admin@oficina.com / admin123");
            }
        };
    }
}
