package com.oficina.cadastro.domain.repository;

import com.oficina.cadastro.domain.model.Usuario;
import java.util.Optional;

import java.util.List;

public interface UsuarioRepository extends org.springframework.data.jpa.repository.JpaRepository<Usuario, Long> {

        Optional<Usuario> findByEmail(String email);

        @org.springframework.data.jpa.repository.Query(value = "SELECT * FROM usuarios u WHERE " +
                        "(CAST(:nome AS varchar) IS NULL OR LOWER(u.nome) LIKE LOWER(CONCAT('%', CAST(:nome AS varchar), '%'))) AND "
                        +
                        "(CAST(:email AS varchar) IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', CAST(:email AS varchar), '%'))) AND "
                        +
                        "(CAST(:#{#perfil?.name()} AS varchar) IS NULL OR u.perfil = CAST(:#{#perfil?.name()} AS varchar)) AND "
                        +
                        "(CAST(:ativo AS boolean) IS NULL OR u.ativo = CAST(:ativo AS boolean))", nativeQuery = true)
        List<Usuario> findByFiltros(
                        @org.springframework.data.repository.query.Param("nome") String nome,
                        @org.springframework.data.repository.query.Param("email") String email,
                        @org.springframework.data.repository.query.Param("perfil") com.oficina.cadastro.domain.enums.PerfilUsuario perfil,
                        @org.springframework.data.repository.query.Param("ativo") Boolean ativo);
}
