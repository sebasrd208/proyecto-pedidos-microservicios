package com.mx.pedidos.controller;

import com.mx.pedidos.dto.*;
import com.mx.pedidos.models.*;
import com.mx.pedidos.service.PedidosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("pedidos")
public class PedidosController {

    @Autowired
    PedidosService service;

    @PostMapping("generar")
    public ResponseEntity<?> generar(@RequestBody RespuestaRequestDTO dto){
        return ResponseEntity.ok(service.generar(dto));
    }

    @GetMapping
    public ResponseEntity<?> mostrar(){
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("buscar")
    public ResponseEntity<?> buscar(@RequestParam int idProveedor){
        return ResponseEntity.ok(service.buscarProveedor(idProveedor));
    }

    @DeleteMapping("eliminar")
    public ResponseEntity<?> eliminar(@RequestParam int idPedido){
        service.eliminar(idPedido);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/pdf")
    public ResponseEntity<?> generarTicket(@RequestBody RespuestaRequestDTO request) {

        byte[] pdf = service.generarPDF(request);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "ticket.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(pdf);
    }

    @PostMapping("/obtener-pdf")
    public ResponseEntity<?> obtenerPDF(@RequestParam int idPedido) {

        byte[] pdf = service.obtenerPdfPorId(idPedido);

        PedidosResponseDTO pedido = service.buscar(idPedido);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "ticket_"+pedido.getFolio()+".pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(pdf);
    }
}
