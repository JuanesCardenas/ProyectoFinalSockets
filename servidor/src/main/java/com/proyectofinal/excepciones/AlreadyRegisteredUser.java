package com.proyectofinal.excepciones;

public class AlreadyRegisteredUser extends Exception {
    public AlreadyRegisteredUser(String mensaje) {
        super(mensaje);
    }
}
