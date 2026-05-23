package com.mx.pedidos.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class ProveedorResponseDTO {

    private int idProveedor;
    private String nombre;
    @JsonIgnore
    private String rfc;
    private String email;
    private String telefono;
    @JsonIgnore
    private String direccion;
}

