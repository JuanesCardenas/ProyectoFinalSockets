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
    

    // Método para agregar un mensaje al muro
    public void agregarMensaje(Mensaje mensaje) {
        this.mensajes.add(mensaje);
    }

    // Método para agregar un producto publicado al muro
    public void agregarProductoPublicado(Producto producto) {
        this.productosPublicados.add(producto);
    }

    // Método para obtener todos los mensajes del muro
    public List<Mensaje> getMensajes() {
        return mensajes;
    }

    // Método para obtener todos los productos publicados
    public List<Producto> getProductosPublicados() {
        return productosPublicados;
    }

    // Método para mostrar los mensajes y productos publicados en el muro
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Muro de Productos Publicados:\n");
        for (Producto producto : productosPublicados) {
            sb.append(producto.toString()).append("\n");
        }

        sb.append("Mensajes:\n");
        for (Mensaje mensaje : mensajes) {
            sb.append(mensaje.toString()).append("\n");
        }
        return sb.toString();
    }
}
