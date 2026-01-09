package com.oficina.cadastro.web.dto;

import com.oficina.cadastro.domain.enums.PerfilUsuario;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String token;
    private String tipo = "Bearer";
    private Long usuarioId;
    private String nome;
    private String email;
    private PerfilUsuario perfil;
}
