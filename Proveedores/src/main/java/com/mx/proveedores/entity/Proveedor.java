package com.mx.proveedores.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "PROVEEDOR_BD")
@Data
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idProveedor;
    private String nombre;
    private String rfc;
    private String email;
    private String telefono;
    private String direccion;

}
