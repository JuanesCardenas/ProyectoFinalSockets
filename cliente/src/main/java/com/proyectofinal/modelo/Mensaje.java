package com.proyectofinal.modelo;

import java.time.LocalDateTime;
import java.io.Serializable;

public class Mensaje implements Serializable {

    private static final long serialVersionUID = 1L;
    private Vendedor remitente;         // El vendedor que envía el mensaje
    private Vendedor destinatario;      // El vendedor que recibe el mensaje
    private String contenido;           // Contenido del mensaje
    private LocalDateTime fechaEnvio;   // Fecha y hora en que se envió el mensaje

    // Constructor
    public Mensaje(Vendedor remitente, Vendedor destinatario, String contenido) {
        this.remitente = remitente;
        this.destinatario = destinatario;
        this.contenido = contenido;
        this.fechaEnvio = LocalDateTime.now(); // Fecha de creación actual
    }

    // Getters y Setters
    public Vendedor getRemitente() {
        return remitente;
    }

    public void setRemitente(Vendedor remitente) {
        this.remitente = remitente;
    }

    public Vendedor getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Vendedor destinatario) {
        this.destinatario = destinatario;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    @Override
    public String toString() {
        return "Mensaje{" +
                "remitente=" + remitente.getNombre() +
                ", destinatario=" + destinatario.getNombre() +
                ", contenido='" + contenido + '\'' +
                ", fechaEnvio=" + fechaEnvio +
                '}';
    }
}
