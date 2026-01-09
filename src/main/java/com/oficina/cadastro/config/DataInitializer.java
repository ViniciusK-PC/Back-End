package com.oficina.cadastro.config;

import com.oficina.cadastro.domain.model.Usuario;
import com.oficina.cadastro.domain.repository.UsuarioRepository;
import com.oficina.cadastro.domain.enums.PerfilUsuario;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbcTemplate;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            try {
                // Remove a restrição antiga para que o Hibernate possa atualizar com o novo
                // perfil ATENDENTE
                jdbcTemplate.execute("ALTER TABLE usuarios DROP CONSTRAINT IF EXISTS usuarios_perfil_check");
                System.out.println("Restrição de perfil atualizada no banco de dados.");
            } catch (Exception e) {
                System.err.println(
                        "Aviso: Não foi possível atualizar a restrição (pode já ter sido removida): " + e.getMessage());
            }

            if (usuarioRepository.count() == 0) {
                Usuario mauricio = new Usuario();
                mauricio.setNome("Mauricio");
                mauricio.setEmail("mauricio@oficina.com");
                mauricio.setSenhaHash(passwordEncoder.encode("admin123"));
                mauricio.setPerfil(PerfilUsuario.DONO); // Perfil de Dono
                mauricio.setAtivo(true);

                usuarioRepository.save(mauricio);
                System.out.println("Usuário Dono criado: Mauricio (mauricio@oficina.com / admin123)");
            }
        };
    }
}
