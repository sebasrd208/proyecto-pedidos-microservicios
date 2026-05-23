package com.example.apigateway.service;

import com.example.apigateway.entity.*;
import org.springframework.stereotype.*;
import com.example.apigateway.repository.*;
import org.springframework.security.core.userdetails.*;

@Service
public class UserDetailServiceImp implements UserDetailsService {

    private final iUsuarioRepository repository;

    public UserDetailServiceImp(iUsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .roles(usuario.getRol().name())
                .build();
    }
}
