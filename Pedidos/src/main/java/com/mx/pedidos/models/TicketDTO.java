package com.mx.pedidos.models;

import lombok.*;

@Data
public class TicketDTO {
    private String producto;
    private String precioUnitario;
    private String cantidad;
    private String total;
}
