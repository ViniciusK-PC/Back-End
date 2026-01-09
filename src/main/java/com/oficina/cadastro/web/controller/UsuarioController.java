package com.oficina.cadastro.web.controller;

import com.oficina.cadastro.service.UsuarioService;
import com.oficina.cadastro.web.dto.UsuarioRequest;
import com.oficina.cadastro.web.dto.UsuarioResponse;
import jakarta.validation.Valid;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public List<UsuarioResponse> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) com.oficina.cadastro.domain.enums.PerfilUsuario perfil,
            @RequestParam(required = false) Boolean ativo) {
        return usuarioService.listar(nome, email, perfil, ativo);
    }

    @GetMapping("/role/{perfil}")
    public List<UsuarioResponse> listarPorPerfil(@PathVariable String perfil) {
        System.out.println("Perfil recebido via rota /role: " + perfil);
        // Mapeia termos usados pelo frontend para o Enum correto
        com.oficina.cadastro.domain.enums.PerfilUsuario perfilEnum = com.oficina.cadastro.domain.enums.PerfilUsuario
                .fromString(perfil);
        return usuarioService.listar(null, null, perfilEnum, null);
    }

    @GetMapping("/{id}")
    public UsuarioResponse buscar(@PathVariable Long id) {
        return usuarioService.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse criar(@Valid @RequestBody UsuarioRequest request) {
        return usuarioService.criar(request);
    }

    @PutMapping("/{id}")
    public UsuarioResponse atualizar(
            @PathVariable Long id, @Valid @RequestBody UsuarioRequest request) {
        return usuarioService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
    }

    @PatchMapping("/{id}/status")
    public UsuarioResponse alternarAtivo(@PathVariable Long id) {
        return usuarioService.alternarAtivo(id);
    }
}
