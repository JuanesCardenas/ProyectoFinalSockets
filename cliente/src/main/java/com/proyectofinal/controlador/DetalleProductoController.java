package com.proyectofinal.controlador;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.proyectofinal.modelo.Producto;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class DetalleProductoController {

    @FXML
    private Label nombreProductoLabel;

    @FXML
    private ImageView imagenUrl;

    @FXML
    private Label categoriaProductoLabel;

    @FXML
    private Label precioProductoLabel;

    @FXML
    private Label estadoProductoLabel;

    @FXML
    private Label descripcionProductoLabel;

    // Método para configurar los detalles del producto
    public void setProducto(Producto producto) {
        byte[] imagenBytes = producto.getImagen(); // Obtiene los bytes de la imagen del producto

        if (imagenBytes != null && imagenBytes.length > 0) {
            try {
                // Convertir los bytes en una InputStream y luego en una Image de JavaFX
                InputStream imagenStream = new ByteArrayInputStream(imagenBytes);
                Image imagen = new Image(imagenStream);
                imagenUrl.setImage(imagen); // Asigna la imagen al componente de interfaz gráfica
            } catch (Exception e) {
                System.out.println("Error al cargar la imagen: " + e.getMessage());
            }
        } else {
            System.out.println("La imagen del producto está vacía o es nula.");
        }

        nombreProductoLabel.setText("Nombre: " + producto.getNombre());
        categoriaProductoLabel.setText("Categoría: " + producto.getCategoria());
        precioProductoLabel.setText("Precio: $" + producto.getPrecio());
        estadoProductoLabel.setText("Estado: " + producto.getEstado());
        descripcionProductoLabel.setText("Descripción: " + producto.getDescripcion());
    }

}

