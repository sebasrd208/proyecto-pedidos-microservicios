package com.mx.productos.service;

import java.util.*;
import com.mx.productos.dto.*;
import com.mx.productos.mappers.*;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.*;
import org.springframework.beans.factory.annotation.*;

@Service
public class ProductosService {

    @Autowired
    MapeoGeneral mapeo;

    public List<InventarioDTO> listar(){
        Map<String, Object> params = new HashMap<>();
        mapeo.SP_GETINVENTARIO(params);

        List<InventarioDTO> inventario = (List<InventarioDTO>) params.get("rec_cursor");

        return inventario;
    }

    public void insertarInventario(InventarioDTO dto){
        Map<String, Object> params = new HashMap<>();
        params.put("PA_NOMBRE", dto.getNombre());
        params.put("PA_PRECIO", dto.getPrecio());
        params.put("PA_STOCK", dto.getStock());

        try {
            mapeo.SP_SETINVENTARIO(params);
        }catch(DataAccessException s){
            throw new RuntimeException(s.getMostSpecificCause().getMessage());
        }
    }

    public InventarioDTO byIdInventario(String idInventario){
        Map<String, Object> params = new HashMap<>();
        params.put("PA_ID", idInventario);

        List<InventarioDTO> inventario;
        try{
            mapeo.SP_GET_ID_INVENTARIO(params);

            inventario = (List<InventarioDTO>) params.get("rec_cursor");
        }catch (DataAccessException s){
            throw new RuntimeException(s.getMostSpecificCause().getMessage());
        }

        return inventario.get(0);
    }

    public void actualizarInventario(InventarioDTO dto){
        Map<String, Object> params = new HashMap<>();
        params.put("PA_NOMBRE", dto.getNombre());
        params.put("PA_PRECIO", dto.getPrecio());
        params.put("PA_STOCK", dto.getStock());

        try {
            mapeo.SP_UPDATE_INVENTARIO(params);
        }catch(DataAccessException s){
            throw new RuntimeException(s.getMostSpecificCause().getMessage());
        }
    }

    public void eliminar(String idInventario){
        Map<String, Object> params=new HashMap<>();
        params.put("PA_ID", idInventario);

        try{
            mapeo.SP_DELETE_INVENTARIO(params);
        }catch(DataAccessException s){
            throw new RuntimeException(s.getMostSpecificCause().getMessage());
        }
    }

    public List<ProductosDTO> generarTicket(ProductoRequestDTO compras) {

        List<ProductosDTO> resultadoFinal = new ArrayList<>();
        List<TicketDTO> todasLasCompras = new ArrayList<>();
        double totalGeneral = 0;

        for (PedidosDTO dto : compras.getPedidos()) {

            Map<String, Object> params = new HashMap<>();
            params.put("PA_PRODUCTO", dto.getProducto());
            params.put("PA_CANTIDAD", dto.getCantidad());

            try {
                mapeo.SP_TICKET_INVENTARIO(params);

                List<TicketDTO> tickets = (List<TicketDTO>) params.get("rec_cursor");

                if (!tickets.isEmpty()) {

                    todasLasCompras.addAll(tickets);

                    for (TicketDTO ticket : tickets) {
                        if (ticket.getTotal() != null && !ticket.getTotal().isEmpty()) {
                            totalGeneral += Double.parseDouble(ticket.getTotal());
                        }
                    }
                }

            } catch (DataAccessException s) {
                throw new RuntimeException(s.getMostSpecificCause().getMessage());
            }
        }

        ProductosDTO productoDTO = new ProductosDTO();
        productoDTO.setCompras(todasLasCompras);
        productoDTO.setTotal(totalGeneral);

        resultadoFinal.add(productoDTO);

        return resultadoFinal;
    }
}
