package com.oficina.cadastro.web.controller;

import com.oficina.cadastro.service.EquipamentoService;
import com.oficina.cadastro.web.dto.EquipamentoRequest;
import com.oficina.cadastro.web.dto.EquipamentoResponse;
import jakarta.validation.Valid;
import java.util.List;
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
@RequestMapping("/api/equipamentos")
@RequiredArgsConstructor
public class EquipamentoController {

    private final EquipamentoService equipamentoService;

    @GetMapping
    public List<EquipamentoResponse> listarPorCliente(
            @RequestParam(name = "clienteId") Long clienteId) {
        return equipamentoService.listarPorCliente(clienteId);
    }

    @GetMapping("/{id}")
    public EquipamentoResponse buscar(@PathVariable Long id) {
        return equipamentoService.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EquipamentoResponse criar(@Valid @RequestBody EquipamentoRequest request) {
        return equipamentoService.criar(request);
    }

    @PutMapping("/{id}")
    public EquipamentoResponse atualizar(
            @PathVariable Long id, @Valid @RequestBody EquipamentoRequest request) {
        return equipamentoService.atualizar(id, request);
    }
}
