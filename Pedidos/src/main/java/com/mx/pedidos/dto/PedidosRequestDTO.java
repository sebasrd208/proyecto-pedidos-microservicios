package com.mx.pedidos.dto;

import lombok.*;
import com.mx.pedidos.entity.*;
import jakarta.validation.constraints.*;

@Data
public class PedidosRequestDTO {

    private int idPedido;
    @NotBlank(message = "El proveedor es obligatorio")
    private int proveedorId;
    private String documento;
    @NotBlank(message = "El total es obligatorio")
    private double total;
    @NotBlank(message = "El estado es obligatorio")
    private Estado estado;

}
