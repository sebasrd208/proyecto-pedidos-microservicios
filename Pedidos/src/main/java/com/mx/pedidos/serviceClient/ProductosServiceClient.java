package com.mx.pedidos.serviceClient;

import feign.*;
import java.util.*;
import com.mx.pedidos.feign.*;
import com.mx.pedidos.models.*;
import org.springframework.stereotype.*;
import org.springframework.beans.factory.annotation.*;
import io.github.resilience4j.circuitbreaker.annotation.*;

@Service
public class ProductosServiceClient {

    @Autowired
    ProductoFeigns feigns;

    @CircuitBreaker(name = "productoService", fallbackMethod = "fallbackProductos")
    public List<ProductosDTO> generarTicket(ProductoRequestDTO compras){
        try{
            return feigns.generarTicket(compras);
        }catch (FeignException.NotFound s){
            return null;
        }
    }

    public List<ProductosDTO> fallbackProductos(ProductoRequestDTO compras, Exception e) {
        throw new RuntimeException("CircuitBreaker PROVEEDORES: Servicio externo caído.");
    }
}
