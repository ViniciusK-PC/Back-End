package com.oficina.cadastro.domain.repository;

import com.oficina.cadastro.domain.model.Usuario;
import java.util.Optional;

import java.util.List;

public interface UsuarioRepository extends org.springframework.data.jpa.repository.JpaRepository<Usuario, Long> {

        Optional<Usuario> findByEmail(String email);

        @org.springframework.data.jpa.repository.Query("SELECT u FROM Usuario u WHERE " +
                        "(:nome IS NULL OR CAST(u.nome AS string) LIKE CONCAT('%', :nome, '%')) AND " +
                        "(:email IS NULL OR CAST(u.email AS string) LIKE CONCAT('%', :email, '%')) AND " +
                        "(:perfil IS NULL OR u.perfil = :perfil) AND " +
                        "(:ativo IS NULL OR u.ativo = :ativo)")
        List<Usuario> findByFiltros(
                        @org.springframework.data.repository.query.Param("nome") String nome,
                        @org.springframework.data.repository.query.Param("email") String email,
                        @org.springframework.data.repository.query.Param("perfil") com.oficina.cadastro.domain.enums.PerfilUsuario perfil,
                        @org.springframework.data.repository.query.Param("ativo") Boolean ativo);
}
