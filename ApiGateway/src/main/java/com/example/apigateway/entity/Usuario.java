package com.example.apigateway.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "GATEWAY_USUARIOS")
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nombreCompleto;
    @Column(unique = true)
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private Rol rol;

}
