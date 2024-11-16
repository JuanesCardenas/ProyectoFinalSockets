package com.proyectofinal.controlador;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import com.proyectofinal.modelo.Producto;
import com.proyectofinal.modelo.Vendedor;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class PublicacionController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button agregarComentarioBoton;

    @FXML
    private Label autorLabel;

    @FXML
    private AnchorPane comentariosCampo;

    @FXML
    private Label fechaPublicacionLabel;

    @FXML
    private ImageView imagenCampo;

    @FXML
    private Label likeLabel;

    @FXML
    private Button meGustaBoton;

    @FXML
    private Label nombreProductoLabel;

    @FXML
    private Label precioProductoLabel;

    private int cantidadLikes = 0;
    private boolean likeDado = false;

    @FXML
    void AgregarComentario() {

    }

    @FXML
    void darMeGusta() {
        if (likeDado) { // Para ver si ya le dió like al post, se le quite el like al presionar el botón de nuevo
            cantidadLikes-=1;
            likeDado = false;
        }
        else{
            cantidadLikes+=1;
            likeDado = true;
        }

    }

    public void setPublicacion(Vendedor vendedor, Producto producto){
        byte[] imagenPath = producto.getImagen(); 

        if (imagenPath != null && imagenPath.length > 0) {
            try {
                // Convertir los bytes en una InputStream y luego en una Image de JavaFX
                InputStream imagenStream = new ByteArrayInputStream(imagenPath);
                Image imagen = new Image(imagenStream);
                imagenCampo.setImage(imagen); // Asigna la imagen al componente de interfaz gráfica
            } catch (Exception e) {
                System.out.println("Error al cargar la imagen: " + e.getMessage());
            }
        } else {
            System.out.println("La imagen del producto está vacía o es nula.");
        }
        nombreProductoLabel.setText("Nombre: " + producto.getNombre());
        precioProductoLabel.setText("Precio: $" + producto.getPrecio());
        autorLabel.setText("Publicado por: " + vendedor.getNombre());
        fechaPublicacionLabel.setText("Publicado el: " + LocalDate.now());
        likeLabel.setText("" + cantidadLikes);

    }

}
