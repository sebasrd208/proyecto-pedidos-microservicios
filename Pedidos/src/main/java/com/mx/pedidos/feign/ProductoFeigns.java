package com.mx.pedidos.feign;

import java.util.*;
import com.mx.pedidos.models.*;
import org.springframework.cloud.openfeign.*;
import org.springframework.web.bind.annotation.*;

@FeignClient(name="Productos", url="http://localhost:8082", path="/productos")
public interface ProductoFeigns {

    @PostMapping("/generar")
    public List<ProductosDTO> generarTicket(@RequestBody ProductoRequestDTO compras);

}
