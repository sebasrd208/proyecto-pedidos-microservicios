package com.example.apigateway.controller;

import org.springframework.http.*;
import com.example.apigateway.entity.*;
import com.example.apigateway.service.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("usuarios")
public class UsuariosController {

    private final UsuarioService service;

    public UsuariosController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping("registro")
    public ResponseEntity<Usuario> registrar(@RequestBody Usuario usuario){
        return ResponseEntity.status(201).body(service.registrar(usuario));
    }
}
