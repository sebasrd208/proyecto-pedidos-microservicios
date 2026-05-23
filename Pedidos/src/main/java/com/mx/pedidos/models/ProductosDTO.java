package com.mx.pedidos.models;

import lombok.*;
import java.util.*;

@Data
public class ProductosDTO {

    private List<TicketDTO> compras;
    private double total;

}
