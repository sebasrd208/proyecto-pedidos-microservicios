package com.example.apigateway.exception;

public class UsuarioNoEncontradoException extends RuntimeException {
  public UsuarioNoEncontradoException(String message) {
    super(message);
  }
}
