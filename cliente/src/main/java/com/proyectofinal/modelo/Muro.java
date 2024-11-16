package com.proyectofinal.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Muro implements Serializable{
    private static final long serialVersionUID = 1L;
    private List<Mensaje> mensajes;     // Lista de mensajes en el muro
    private List<Producto> productosPublicados; // Productos que el vendedor ha publicado


    // Constructor
    public Muro() {
        this.mensajes = new ArrayList<>();
        this.productosPublicados = new ArrayList<>();
    }

    // Método para obtener todos los mensajes del muro
    public List<Mensaje> getMensajes() {
        return mensajes;
    }

    // Método para obtener todos los productos publicados
    public List<Producto> getProductosPublicados() {
        return productosPublicados;
    }

    public void setMensajes(List<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }

    public void setProductosPublicados(List<Producto> productosPublicados) {
        this.productosPublicados = productosPublicados;
    }

    @Override
    public String toString() {
        return "Muro [mensajes=" + mensajes + ", productosPublicados=" + productosPublicados + "]";
    }

    
}
