package com.ulsa.oaxaca.edu.proyecto_banco.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TarjetaYaExisteException.class)
    public ResponseEntity<String> handleTarjetaYaExisteException(TarjetaYaExisteException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    // Otros manejadores de excepciones
}
