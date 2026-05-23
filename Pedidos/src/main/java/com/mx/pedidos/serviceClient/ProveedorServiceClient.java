package com.mx.pedidos.serviceClient;

import feign.*;
import com.mx.pedidos.feign.*;
import com.mx.pedidos.models.*;
import org.springframework.stereotype.*;
import org.springframework.beans.factory.annotation.*;
import io.github.resilience4j.circuitbreaker.annotation.*;

@Service
public class ProveedorServiceClient {

    @Autowired
    ProveedorFeigns feigns;

    @CircuitBreaker(name = "proveedorService", fallbackMethod = "fallbackProveedores")
    public ProveedorResponseDTO buscarProveedorSeguro(int idProveedor){
        try{
            return feigns.buscar(idProveedor);
        }catch (FeignException.NotFound s){
            return null;
        }
    }

    public ProveedorResponseDTO fallbackProveedores(int id, Exception e) {
        throw new RuntimeException("CircuitBreaker PROVEEDORES: Servicio externo caído.");
    }

}
