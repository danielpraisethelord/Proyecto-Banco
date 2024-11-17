package com.ulsa.oaxaca.edu.proyecto_banco.utils;

public class TarjetaYaExisteException extends RuntimeException {
    public TarjetaYaExisteException(String message) {
        super(message);
    }
}
