package com.mx.proveedores.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProveedorRequestDTO {

    private int idProveedor;
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    @NotBlank(message = "El rfc es obligatorio")
    private String rfc;
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Formato no válido")
    private String email;
    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^[0-9]+$", message = "Solo se permiten números")
    private String telefono;
    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

}
