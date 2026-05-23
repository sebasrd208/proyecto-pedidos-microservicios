package com.mx.proveedores.repository;

import java.util.*;
import com.mx.proveedores.entity.*;
import org.springframework.data.jpa.repository.*;

public interface iProveedorRepository extends JpaRepository<Proveedor, Integer> {
    public Optional<Proveedor> findByEmail(String email);

}
