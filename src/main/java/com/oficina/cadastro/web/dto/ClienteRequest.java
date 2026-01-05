package com.oficina.cadastro.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClienteRequest(
        @NotBlank @Size(max = 160) String nome,
        @Size(max = 32) String documento,
        @Email @Size(max = 160) String email,
        @Size(max = 32) String telefone,
        @Size(max = 32) String whatsapp,
        String endereco,
        String observacoes,
        Boolean ativo) {
}

