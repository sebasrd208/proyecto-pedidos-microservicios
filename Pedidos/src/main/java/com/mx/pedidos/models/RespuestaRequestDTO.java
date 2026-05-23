package com.mx.pedidos.models;

import lombok.Data;
import java.util.List;

@Data
public class RespuestaRequestDTO {

    private int proveedor;
    private List<PedidosDTO> pedidos;
}
