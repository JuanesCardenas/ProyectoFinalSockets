package com.proyectofinal.excepciones;

public class ProductNotFoundException extends Exception {
    public ProductNotFoundException() {
        super("El producto no existe.");
    }

    public ProductNotFoundException(String message) {
        super(message);
    }
}