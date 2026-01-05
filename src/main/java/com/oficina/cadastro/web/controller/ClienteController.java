package com.oficina.cadastro.web.controller;

import com.oficina.cadastro.service.ClienteService;
import com.oficina.cadastro.web.dto.ClienteRequest;
import com.oficina.cadastro.web.dto.ClienteResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    public List<ClienteResponse> listar(@RequestParam(required = false) String nome) {
        return clienteService.listar(nome);
    }

    @GetMapping("/{id}")
    public ClienteResponse buscar(@PathVariable UUID id) {
        return clienteService.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponse criar(@Valid @RequestBody ClienteRequest request) {
        return clienteService.criar(request);
    }

    @PutMapping("/{id}")
    public ClienteResponse atualizar(
            @PathVariable UUID id, @Valid @RequestBody ClienteRequest request) {
        return clienteService.atualizar(id, request);
    }
}

