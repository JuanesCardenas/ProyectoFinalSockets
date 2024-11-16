package com.proyectofinal.excepciones;

public class InvalidProductException extends Exception {
    public InvalidProductException() {
        super("Uno o mas datos del producto no son validos.");
    }

    public InvalidProductException(String message) {
        super(message);
    }
}