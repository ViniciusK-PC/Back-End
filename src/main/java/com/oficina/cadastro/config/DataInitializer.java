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
                System.out.println("Iniciando verificação de restrições do banco...");
                // Tenta remover várias possíveis variações do nome da constraint
                jdbcTemplate.execute("ALTER TABLE usuarios DROP CONSTRAINT IF EXISTS usuarios_perfil_check");
                jdbcTemplate.execute("ALTER TABLE usuarios DROP CONSTRAINT IF EXISTS usuarios_perfil_check1");
                jdbcTemplate.execute("ALTER TABLE usuarios DROP CONSTRAINT IF EXISTS UK_perfil_usuarios");
                System.out.println("Restrições de perfil removidas ou verificadas.");
            } catch (Exception e) {
                System.out.println("Aviso: Falha ao tentar remover restrição: " + e.getMessage());
            }

            // Criar ou Sincronizar usuário DONO (Mauricio Silva)
            if (usuarioRepository.findByEmail("mauricio@oficina.com").isEmpty()) {
                Usuario mauricio = new Usuario();
                mauricio.setNome("Mauricio Silva");
                mauricio.setEmail("mauricio@oficina.com");
                mauricio.setSenhaHash(passwordEncoder.encode("admin123"));
                mauricio.setPerfil(PerfilUsuario.DONO);
                mauricio.setAtivo(true);
                usuarioRepository.save(mauricio);
                System.out.println("Usuário Maurício Silva criado com perfil DONO.");
            } else {
                // Força a sincronização de nome, perfil e SENHA
                String senhaHash = passwordEncoder.encode("admin123");
                jdbcTemplate.update(
                        "UPDATE usuarios SET nome = 'Mauricio Silva', perfil = 'DONO', senha_hash = ? WHERE email = 'mauricio@oficina.com'",
                        senhaHash);
                System.out.println("Usuário Maurício Silva sincronizado com perfil DONO e senha redefinida.");
            }

            // Criar usuário ATENDENTE para teste se não houver um
            if (usuarioRepository.findByEmail("atendente@oficina.com").isEmpty()) {
                Usuario atendente = new Usuario();
                atendente.setNome("Atendente Teste");
                atendente.setEmail("atendente@oficina.com");
                atendente.setSenhaHash(passwordEncoder.encode("atendente123"));
                atendente.setPerfil(PerfilUsuario.ATENDENTE);
                atendente.setAtivo(true);
                usuarioRepository.save(atendente);
                System.out.println("Usuário Atendente criado para teste!");
            }
        };
    }
}
