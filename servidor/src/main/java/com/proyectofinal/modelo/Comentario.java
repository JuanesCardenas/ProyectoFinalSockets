package com.proyectofinal.modelo;

import java.time.LocalDateTime;
import java.io.Serializable;

public class Comentario implements Serializable {

    private static final long serialVersionUID = 1L;
    private Vendedor autor;             // Quién realizó el comentario
    private String contenido;           // Contenido del comentario
    private LocalDateTime fechaComentario; // Fecha en que se hizo el comentario

    public Comentario(Vendedor autor, String contenido) {
        this.autor = autor;
        this.contenido = contenido;
        this.fechaComentario = LocalDateTime.now();  // Fecha de creación actual
    }

    // Getters y Setters
    public Vendedor getAutor() {
        return autor;
    }

    public void setAutor(Vendedor autor) {
        this.autor = autor;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public LocalDateTime getFechaComentario() {
        return fechaComentario;
    }

    // Sobrescritura del método toString para mostrar información del comentario
    @Override
    public String toString() {
        return "Comentario{" +
                "autor=" + autor.getNombre() +
                ", contenido='" + contenido + '\'' +
                ", fechaComentario=" + fechaComentario +
                '}';
    }
}

