package com.mx.productos.dto;

import lombok.Data;

@Data
public class TicketDTO {

    private String producto;
    private String precioUnitario;
    private String cantidad;
    private String total;

}
