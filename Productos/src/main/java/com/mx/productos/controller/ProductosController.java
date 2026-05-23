package com.mx.productos.controller;

import java.util.*;
import com.mx.productos.dto.*;
import com.mx.productos.service.*;
import org.springframework.http.*;
import io.swagger.v3.oas.annotations.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.*;

@RestController
@RequestMapping("productos")
public class ProductosController {

    @Autowired
    ProductosService service;

    @GetMapping("/mostrar")
    @Operation(summary = "Mostrar productos", description = "Muestra todos los productos registrados")
    public ResponseEntity<?> mostrarProductos() {
        try {
            List<InventarioDTO> lista = service.listar();
            if (lista.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("{\"Mensaje\":\"No hay contenido en la lista\"}");
            }
            return ResponseEntity.ok(lista);
        }catch(RuntimeException s){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(s.getCause().getMessage().lines().findFirst().orElse("").trim());
        }
    }

    @PostMapping("/guardar")
    @Operation(summary = "Registrar productos", description = "Registra un nuevo producto")
    public ResponseEntity<?> guardar(@RequestBody InventarioDTO dto) {
        try {
            service.insertarInventario(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("{\"Mensaje\":\"Registro exitoso\"}");
        } catch (RuntimeException s) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(s.getMessage().lines().findFirst().orElse("").trim());
        }
    }

    @PutMapping("/editar")
    @Operation(summary = "Actualizar productos", description = "Actualiza un producto existente")
    public ResponseEntity<?> editar(@RequestBody InventarioDTO dto) {
        try {
            service.actualizarInventario(dto);
            return ResponseEntity.status(HttpStatus.OK).body("{\"Mensaje\":\"Actualización exitoso\"}");
        } catch (RuntimeException s) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(s.getMessage().lines().findFirst().orElse("").trim());
        }
    }

    @DeleteMapping("/eliminar")
    @Operation(summary = "Eliminar productos", description = "Elimina un producto existente")
    public ResponseEntity<?> eliminar(@RequestParam String idInventario) {
        try {
            service.eliminar(idInventario);
            return ResponseEntity.status(HttpStatus.OK).body("{\"Mensaje\":\"Producto eliminado exitosamente\"}");
        } catch (RuntimeException s) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(s.getMessage().lines().findFirst().orElse("").trim());
        }
    }

    @GetMapping("/id")
    @Operation(summary = "Mostrar datos de un producto", description = "Se obtienen datos de un solo producto")
    public ResponseEntity<?> mostrarProducto(@RequestParam String idInventario){
        try {
            InventarioDTO usuario = service.byIdInventario(idInventario);
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"Mensaje\":\"No hay contenido en la lista\"}");
            }
            return ResponseEntity.ok(usuario);
        }catch (RuntimeException s){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(s.getCause().getMessage().lines().findFirst().orElse("").trim());
        }
    }

    @PostMapping("/generar")
    @Operation(summary = "Generar ticket", description = "Se genera el ticket de los productos")
    public ResponseEntity<?> generarTicket(@RequestBody ProductoRequestDTO request) {

        try {
            List<ProductosDTO> ticket = service.generarTicket(request);
            return ResponseEntity.ok(ticket);
        } catch (RuntimeException s) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(s.getCause().getMessage().lines().findFirst().orElse("").trim());
        }
    }
}
