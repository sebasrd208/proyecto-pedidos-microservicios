package com.mx.pedidos.models;

import lombok.*;
import java.util.*;
import com.mx.pedidos.dto.*;

@Data
public class TicketProveedorResponseDTO {

    private PedidosResponseDTO pedido;
    private ProveedorResponseDTO proveedor;
    private List<ProductosDTO> ticket;
}
