package com.mx.proveedores.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;

@Data
@JsonPropertyOrder({"idProveedor", "nombre", "rfc", "email", "telefono"})
public class ProveedorResponseDTO {

    private int idProveedor;
    private String nombre;
    private String rfc;
    private String email;
    private String telefono;
    private String direccion;
}
