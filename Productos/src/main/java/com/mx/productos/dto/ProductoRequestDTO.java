package com.mx.productos.dto;

import lombok.Data;
import java.util.*;

@Data
public class ProductoRequestDTO {
    private List<PedidosDTO> pedidos;
}
