package com.mx.productos.dto;

import lombok.Data;
import java.util.List;

@Data
public class ProductosDTO {
    private List<TicketDTO> compras;
    private double total;
}
