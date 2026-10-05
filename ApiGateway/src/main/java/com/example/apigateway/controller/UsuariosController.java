package com.example.apigateway.controller;

import com.example.apigateway.exception.UsuarioNoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import com.example.apigateway.entity.*;
import com.example.apigateway.service.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("usuarios")
public class UsuariosController {

    @Autowired
    UsuarioService service;

    @GetMapping
    public ResponseEntity<?> mostrar(){
        try{
            List<Usuario> usuarios = service.mostrar();
            return ResponseEntity.ok(usuarios);
        }catch(Exception s){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(s.getCause().getMessage().lines().findFirst().orElse("").trim());
        }
    }

    @GetMapping("buscar")
    public ResponseEntity<?> buscarUsuario(@RequestParam String usuario){
        try{
            Usuario encontrado = service.mostrarUsuario(usuario);
            return ResponseEntity.ok(encontrado);
        }catch (UsuarioNoEncontradoException s){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(s.getCause().getMessage().lines().findFirst().orElse("").trim());
        }
    }

    @PostMapping("registro")
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario){
        try {
            service.registrar(usuario);
            return ResponseEntity.status(201).body("{\"Mensaje\":\"Registro exitoso\"}");
        }catch(RuntimeException s){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(s.getMessage().lines().findFirst().orElse("").trim());
        }
    }

    @PutMapping("/actualizar-password")
    public ResponseEntity<?> actualizarPassword(@RequestParam String username, @RequestParam String password){
        try {
            service.modificarPassword(username, password);
            return ResponseEntity.status(HttpStatus.OK).body("{\"Mensaje\":\"Se actualizó la contraseña del usuario "+username+"\"}");
        } catch (RuntimeException s) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(s.getMessage().lines().findFirst().orElse("").trim());
        }
    }

    @PutMapping("/actualizar")
    public ResponseEntity<?> actualizar(@RequestBody Usuario usuario){
        try {
            service.editar(usuario);
            return ResponseEntity.status(HttpStatus.OK).body("{\"Mensaje\":\"Se actualizó la contraseña del usuario "+usuario.getUsername()+"\"}");
        } catch (RuntimeException s) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(s.getMessage().lines().findFirst().orElse("").trim());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String usuario, @RequestParam String password) {
        try {
            Usuario username = service.login(usuario, password);

            if (username == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Usuario o contraseña incorrectos");
            }
            return ResponseEntity.ok(username);
        }catch(Exception s){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(s.getCause().getMessage().lines().findFirst().orElse("").trim());
        }
    }

    @DeleteMapping("/eliminar")
    public ResponseEntity<?> eliminar(@RequestParam String username) {
        try {
            service.borrarUsuario(username);
            return ResponseEntity.status(HttpStatus.CREATED).body("{\"Mensaje\":\"Usuario eliminado de manera exitosa\"}");
        } catch (RuntimeException s) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(s.getMessage().lines().findFirst().orElse("").trim());
        }
    }
}
