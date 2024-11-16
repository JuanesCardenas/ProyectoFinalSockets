package com.proyectofinal.excepciones;

public class ExcepcionLimiteVendedores extends RuntimeException {
    public ExcepcionLimiteVendedores(String mensaje) {
        super(mensaje);
    }
}