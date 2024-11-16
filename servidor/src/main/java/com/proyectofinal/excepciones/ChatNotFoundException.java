package com.proyectofinal.excepciones;

public class ChatNotFoundException extends Exception {
    public ChatNotFoundException() {
        super("El chat no existe.");
    }

    public ChatNotFoundException(String message) {
        super(message);
    }
}