package com.proyectofinal.controlador;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.util.logging.Level;

import com.proyectofinal.modelo.AdministradorLogger;
import com.proyectofinal.modelo.Producto;
import com.proyectofinal.modelo.Vendedor;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;


public class AgregarProductoController {

    @FXML
    private TextField nombreField;
    @FXML
    private TextField codigoField;
    @FXML
    private TextField categoriaField;
    @FXML
    private TextField precioField;
    @FXML
    private TextArea descripcionArea;
    @FXML
    private ImageView imagenView;
    @FXML
    private Button agregarButton;

    private byte[] imageBytes;

    private Vendedor vendedorActual;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private PerfilVendedorController perfilVendedorController;
    
        public void setVendedorActual(Vendedor vendedor) {
            this.vendedorActual = vendedor;
        }

        public void setPerfilVendedorController(PerfilVendedorController perfilVendedorController) {
            this.perfilVendedorController = perfilVendedorController;
        }

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
    
        public void cargarImagen() {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                try {
                    imageBytes = Files.readAllBytes(file.toPath());
                    InputStream imagenStream = new ByteArrayInputStream(imageBytes);
                    Image imagen = new Image(imagenStream);
                    imagenView.setImage(imagen);
                } catch (IOException e) {
                    mostrarAlerta("Error", "No se pudo cargar la imagen.", e.toString());
                }
            }
        }
        
        public void agregarProducto() throws ClassNotFoundException {
    
            conectarAlServidor();
    
            String nombre = nombreField.getText();
            String codigo = codigoField.getText();
            String categoria = categoriaField.getText();
            double precio;
            
            try {
                precio = Double.parseDouble(precioField.getText());
            } catch (NumberFormatException e) {
                mostrarAlerta("Error", "El precio debe ser un número válido.", e.toString());
                return;
            }
        
            String descripcion = descripcionArea.getText();
            // Carga los bytes de la imagen
        
            // Crear el producto con la imagen cargada
            Producto nuevoProducto = new Producto(nombre, codigo, imageBytes, categoria, precio, descripcion);
            nuevoProducto.setImagenNombre(nombre+"_"+ codigo + ".png"); // Establecer el nombre de archivo
        
            try {
                out.writeObject("AGREGAR_PRODUCTO");
                out.writeObject(vendedorActual);
                out.writeObject(nuevoProducto);
                out.flush();
                vendedorActual = (Vendedor) in.readObject();
                String mensajeServidor = (String) in.readObject();
                if(mensajeServidor.equals("EXITO")){
                    perfilVendedorController.setVendedorActual(vendedorActual);
                    mostrarInformacion("Éxito", "Producto agregado", "El producto ha sido agregado exitosamente.");
                }
                limpiarCampos();
            } catch (IOException e) {
                mostrarAlerta("Error", "Error al enviar producto al servidor.", e.toString());
            }
            
        }
        
        
        public void cancelar() {
            limpiarCampos();
        }
    
        private void limpiarCampos() {
            nombreField.clear();
            codigoField.clear();
            categoriaField.clear();
            precioField.clear();
            descripcionArea.clear();
            imagenView.setImage(null);
        }
    
        private void mostrarInformacion(String titulo, String encabezado, String contenido) {
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle(titulo);
            alerta.setHeaderText(encabezado);
            alerta.setContentText(contenido);
            alerta.showAndWait();
        }
    
        private void mostrarAlerta(String titulo, String encabezado, String contenido) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle(titulo);
            alerta.setHeaderText(encabezado);
            alerta.setContentText(contenido);
            alerta.showAndWait();
        }
    
    
}
