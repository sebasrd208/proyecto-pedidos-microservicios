package com.mx.pedidos.excepciones;

public class PedidosNotFoundException extends RuntimeException {
    public PedidosNotFoundException(String message) {
        super(message);
    }
}
