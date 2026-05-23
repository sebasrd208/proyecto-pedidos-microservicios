package com.example.apigateway.service;

import com.example.apigateway.entity.*;
import org.springframework.stereotype.*;
import com.example.apigateway.repository.*;
import org.springframework.security.crypto.password.*;

@Service
public class UsuarioService {

    private final iUsuarioRepository repository;
    private final PasswordEncoder encoder;

    public UsuarioService(iUsuarioRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    public Usuario registrar(Usuario usuario) {
        usuario.setPassword(encoder.encode(usuario.getPassword()));

        if(usuario.getRol() == null) {
            usuario.setRol(Rol.USER);
        }

        return repository.save(usuario);
    }
}
