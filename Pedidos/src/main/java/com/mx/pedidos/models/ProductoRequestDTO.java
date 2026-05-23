package com.mx.pedidos.models;

import lombok.Data;
import java.util.List;

@Data
public class ProductoRequestDTO {

    private List<PedidosDTO> pedidos;
}
