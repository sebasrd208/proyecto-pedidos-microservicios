package com.mx.pedidos.feign;

import com.mx.pedidos.models.*;
import org.springframework.cloud.openfeign.*;
import org.springframework.web.bind.annotation.*;

@FeignClient(name="Proveedores", url="http://localhost:8081", path="/proveedores")
public interface ProveedorFeigns {

    @GetMapping("/{idProveedor}")
    public ProveedorResponseDTO buscar(@PathVariable int idProveedor);
}
