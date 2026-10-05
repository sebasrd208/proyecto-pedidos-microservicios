package com.example.apigateway.service;

import com.example.apigateway.entity.*;
import com.example.apigateway.exception.UsuarioNoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.*;
import com.example.apigateway.repository.*;
import org.springframework.security.crypto.password.*;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    iUsuarioRepository repository;

    @Autowired
    PasswordEncoder encoder;

    public List<Usuario> mostrar(){
        return repository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public Usuario mostrarUsuario(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));
    }

    public Usuario modificarPassword(String username, String password){
        Usuario usuario = repository.findByUsername(username).orElseThrow();

        usuario.setPassword(encoder.encode(password));

        return repository.save(usuario);
    }

    public void borrarUsuario(String username){
        Usuario usuario = repository.findByUsername(username)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        repository.delete(usuario);

    }

    public Usuario editar(Usuario usuario) {

        Usuario encontrado = repository.findById(usuario.getId())
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        encontrado.setNombreCompleto(usuario.getNombreCompleto());
        encontrado.setUsername(usuario.getUsername());
        encontrado.setRol(usuario.getRol());

        return repository.save(encontrado);
    }

    public Usuario registrar(Usuario usuario) {
        usuario.setPassword(encoder.encode(usuario.getPassword()));

        if(usuario.getRol() == null) {
            usuario.setRol(Rol.USER);
        }

        return repository.save(usuario);
    }

    public Usuario login(String usuario, String password) {

        Usuario user = repository.findByUsername(usuario)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        if (!encoder.matches(password, user.getPassword())) {
            return null;
        }

        return user;
    }
}
