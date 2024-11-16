package com.proyectofinal.controlador;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import com.proyectofinal.modelo.AdministradorLogger;
import com.proyectofinal.modelo.Estado;
import com.proyectofinal.modelo.Producto;
import com.proyectofinal.modelo.Vendedor;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

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

    @FXML
    private Button editarButton;

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Producto producto;
    private Vendedor vendedorActual;
    private PerfilVendedorController perfilVendedorController;

    private void conectarAlServidor() {
        try {
            socket = new Socket("localhost", 5000);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo conectar al servidor.", e.toString());
            AdministradorLogger.getInstance().escribirLog(AgregarProductoController.class, "Error de conexión con el servidor.", Level.SEVERE);
        }
    }

    @FXML
    public void EditarProducto() throws IOException {
        conectarAlServidor();
        Dialog<Producto> dialog = new Dialog<>();
        dialog.setTitle("Actualizar Prodcuto");
        dialog.setHeaderText("Edite la infromacion que desea cambiar");

        // Configurar los campos de entrada
        Label nombreLabel = new Label("Nombre:");
        TextField nombreField = new TextField(producto.getNombre());
        Label codigoLabel = new Label("Codigo:");
        TextField codigoField = new TextField(producto.getCodigo());
        codigoField.setEditable(false);
        Label categoriaLabel = new Label("Categoria:");
        TextField categoriaField= new TextField(producto.getCategoria());
        Label estadoLabel = new Label("Estado:");
        TextField estadoField= new TextField(producto.getEstado().toString());
        estadoField.setEditable(false);
        ComboBox<Estado> estadoComboBox = new ComboBox<>();
        estadoComboBox.getItems().addAll(Estado.values());
        Label descripcionLabel = new Label("Descripcion:");
        TextField descripcionField = new TextField(producto.getDescripcion());
        Label precioLabel = new Label("Precio:");
        TextField precioField = new TextField((String.valueOf(producto.getPrecio())));

        VBox content = new VBox(10, nombreLabel, nombreField, codigoLabel, codigoField, categoriaLabel, categoriaField, estadoLabel, estadoField, estadoComboBox, descripcionLabel, descripcionField, precioLabel, precioField);
        dialog.getDialogPane().setContent(content);

        ButtonType addButtonType = new ButtonType("Actualizar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, addButtonType);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                // Actualizar el deporte seleccionado con los nuevos valores
                producto.setNombre(nombreField.getText());
                producto.setCategoria(categoriaField.getText());
                if (estadoComboBox.getValue() != null) {
                    producto.setEstado(estadoComboBox.getValue());
                }
                producto.setDescripcion(descripcionField.getText());
                producto.setPrecio(Double.parseDouble(precioField.getText()));
                return producto; // Devolver el deporte actualizado
            }
            return null;
        });

        // Mostrar el diálogo y procesar la entrada
        dialog.showAndWait().ifPresent(producto -> {
            try {
                out.writeObject("EDITAR_PRODUCTO");
                out.writeObject(vendedorActual);
                out.writeObject(producto);
                out.flush();
                Vendedor vendedorProductoEdit = (Vendedor) in.readObject();
                String mensajeServidor = (String) in.readObject();
                if(mensajeServidor.startsWith("EXITO")){
                    perfilVendedorController.setVendedorActual(vendedorProductoEdit);
                    mostrarInformacion("Éxito", "Producto editado", "El producto ha sido modificado exitosamente.");
                }
            } catch (IOException e) {
                AdministradorLogger.getInstance().escribirLog(DetalleProductoController.class, e.toString(), Level.SEVERE);
            } catch (ClassNotFoundException e) {
                
                AdministradorLogger.getInstance().escribirLog(DetalleProductoController.class, e.toString(), Level.SEVERE);
            }
            
        });
        

    }
    
    public void setVendedorActual(Vendedor vendedor) {
        this.vendedorActual = vendedor;
    }

    public void setPerfilVendedorController(PerfilVendedorController perfilVendedorController) {
        this.perfilVendedorController = perfilVendedorController;
    }

    // Método para configurar los detalles del producto
    public void setProducto(Producto producto) {
        this.producto = producto;
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

    private void mostrarAlerta(String titulo, String encabezado, String contenido) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(encabezado);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
    private void mostrarInformacion(String titulo, String encabezado, String contenido) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION); // Cambiar a tipo de alerta de información
        alerta.setTitle(titulo);
        alerta.setHeaderText(encabezado);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }

}

