package com.mx.proveedores.controller;

import com.mx.proveedores.excepciones.ProveedorNotFoundException;
import jakarta.validation.*;
import com.mx.proveedores.dto.*;
import org.springframework.http.*;
import com.mx.proveedores.service.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.*;

@RestController
@RequestMapping("proveedores")
public class ProveedoresController {

    @Autowired
    ProveedorService service;

    @GetMapping
    public ResponseEntity<?> listar(){
        try{
            return ResponseEntity.ok(service.listar());
        }catch(ProveedorNotFoundException s){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(s.getCause().getMessage().lines().findFirst().orElse("").trim());
        }
    }

    @PostMapping
    public ResponseEntity<?> guardar(@Valid @RequestBody ProveedorRequestDTO dto){
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(dto));
        }catch(RuntimeException s){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(s.getMessage().lines().findFirst().orElse("").trim());
        }
    }

    @GetMapping("/{idProveedor}")
    public ResponseEntity<?> buscar(@PathVariable int idProveedor){
        try {
            return ResponseEntity.ok(service.buscar(idProveedor));
        } catch (ProveedorNotFoundException s) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(s.getCause().getMessage().lines().findFirst().orElse("").trim());
        }
    }

    @PutMapping
    public ResponseEntity<?> editar(@Valid @RequestBody ProveedorRequestDTO dto){
        try {
            return ResponseEntity.ok(service.editar(dto));
        }catch(RuntimeException s){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(s.getMessage().lines().findFirst().orElse("").trim());
        }
    }

    @DeleteMapping("/{idProveedor}")
    public ResponseEntity<?> eliminar(@PathVariable int idProveedor){
        try {
            service.eliminar(idProveedor);
            return ResponseEntity.noContent().build();
        }catch (ProveedorNotFoundException s){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(s.getCause().getMessage().lines().findFirst().orElse("").trim());
        }
    }
}
