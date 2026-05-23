package com.mx.pedidos.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Data
public class PedidosResponseDTO {

    private int idPedido;
    private String folio;
    private int proveedorId;
    @JsonIgnore
    private String documento;
    private String fechaPedido;
    private double total;
    private String estado;
}
