package com.example.apigateway.service;

import com.example.apigateway.entity.*;
import com.example.apigateway.exception.UsuarioExistenteException;
import com.example.apigateway.exception.UsuarioNoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.*;
import com.example.apigateway.repository.*;
import org.springframework.security.crypto.password.*;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService implements UserDetailsService {

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
        Usuario usuario = repository.findByUsername(username)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

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

        Optional<Usuario> usuarioExistente =
                repository.findByUsername(usuario.getUsername());

        if (usuarioExistente.isPresent()
                && usuarioExistente.get().getId() != encontrado.getId()) {

            throw new UsuarioExistenteException(
                    "El usuario ya está registrado"
            );
        }

        encontrado.setNombreCompleto(usuario.getNombreCompleto());
        encontrado.setUsername(usuario.getUsername());
        encontrado.setRol(usuario.getRol());

        return repository.save(encontrado);
    }

    public Usuario registrar(Usuario usuario) {

        if (repository.findByUsername(usuario.getUsername()).isPresent()) {
            throw new UsuarioExistenteException(
                    "El usuario ya está registrado"
            );
        }

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
