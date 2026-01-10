package com.oficina.cadastro.painel.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "donos_privados")
public class Dono {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username; // ou email

    @Column(nullable = false)
    private String password;

    // Construtor padrão
    public Dono() {
    }

    public Dono(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
