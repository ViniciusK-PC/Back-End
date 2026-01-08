package com.oficina.cadastro.web.controller;

import com.oficina.cadastro.domain.enums.StatusOrdemServico;
import com.oficina.cadastro.service.OrdemServicoService;
import com.oficina.cadastro.web.dto.OrdemServicoRequest;
import com.oficina.cadastro.web.dto.OrdemServicoResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/api/ordens-servico")
@RequiredArgsConstructor
public class OrdemServicoController {

    private final OrdemServicoService ordemServicoService;

    @GetMapping
    public List<OrdemServicoResponse> listar(
            @RequestParam(required = false) StatusOrdemServico status,
            @RequestParam(required = false) UUID clienteId) {
        return ordemServicoService.listar(status, clienteId);
    }

    @GetMapping("/{id}")
    public OrdemServicoResponse buscar(@PathVariable UUID id) {
        return ordemServicoService.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrdemServicoResponse criar(@Valid @RequestBody OrdemServicoRequest request) {
        return ordemServicoService.criar(request);
    }

    @PutMapping("/{id}")
    public OrdemServicoResponse atualizar(
            @PathVariable UUID id, @Valid @RequestBody OrdemServicoRequest request) {
        return ordemServicoService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable UUID id) {
        ordemServicoService.deletar(id);
    }
}
