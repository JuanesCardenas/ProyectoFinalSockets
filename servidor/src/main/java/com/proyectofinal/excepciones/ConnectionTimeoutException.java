package com.proyectofinal.excepciones;

public class ConnectionTimeoutException extends Exception {
    public ConnectionTimeoutException() {
        super("Se agotó el tiempo de espera al intentar conectarse.");
    }

    public ConnectionTimeoutException(String message) {
        super(message);
    }
}