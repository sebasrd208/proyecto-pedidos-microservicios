package com.mx.proveedores.excepciones;

public class ProveedorNotFoundException extends RuntimeException {
    public ProveedorNotFoundException(String message) {
        super(message);
    }
}
