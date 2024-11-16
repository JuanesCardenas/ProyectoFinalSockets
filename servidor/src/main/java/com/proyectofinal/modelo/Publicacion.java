package com.proyectofinal.modelo;

import java.time.LocalDateTime;
import java.util.List;

public class Publicacion {
    
    private Producto producto;
    private List<Comentario> comentarios;
    private LocalDateTime fechaPublicacion;

    // Constructor
    public Publicacion(Producto producto, List<Comentario> comentarios, LocalDateTime fechaPublicacion) {
        this.producto = producto;
        this.comentarios = comentarios;
        this.fechaPublicacion = fechaPublicacion;
    }
    // Gets y sets
    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public List<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(List<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    public LocalDateTime getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(LocalDateTime fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public void agregarComentario(Comentario comentario){

    }
    
    public void publicar(Muro muro){
        
    }

}
