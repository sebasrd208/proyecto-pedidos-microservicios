package com.mx.proveedores.controller;

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
        return ResponseEntity.ok(service.listar());
    }

    @PostMapping
    public ResponseEntity<?> guardar(@Valid @RequestBody ProveedorRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(dto));
    }

    @GetMapping("/{idProveedor}")
    public ResponseEntity<?> buscar(@PathVariable int idProveedor){
        return ResponseEntity.ok(service.buscar(idProveedor));
    }

    @PutMapping
    public ResponseEntity<?> editar(@Valid @RequestBody ProveedorRequestDTO dto){
        return ResponseEntity.ok(service.editar(dto));
    }

    @DeleteMapping("/{idProveedor}")
    public ResponseEntity<?> eliminar(@PathVariable int idProveedor){
        service.eliminar(idProveedor);
        return ResponseEntity.noContent().build();
    }
}
