package com.mx.productos.dto;

import lombok.Data;

@Data
public class InventarioDTO {

    private String idInventario;
    private String nombre;
    private String precio;
    private String stock;

}
