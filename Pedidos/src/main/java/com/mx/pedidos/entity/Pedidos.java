package com.mx.pedidos.entity;

import lombok.*;
import java.time.*;
import jakarta.persistence.*;
import org.hibernate.annotations.*;

@Entity
@Table(name="PEDIDOS_BD")
@Data
public class Pedidos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPedido;
    @UuidGenerator
    private String folio;
    private int proveedorId;
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String documento;
    @CreationTimestamp
    private LocalDate fechaPedido;
    private double total;
    private Estado estado;
}
