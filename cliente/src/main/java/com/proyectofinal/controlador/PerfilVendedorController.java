package com.proyectofinal.controlador;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;

import com.proyectofinal.modelo.AdministradorLogger;
import com.proyectofinal.modelo.Estado;
import com.proyectofinal.modelo.Producto;
import com.proyectofinal.modelo.Vendedor;

public class PerfilVendedorController {

    @FXML
    private Label cedulaLabel;

    @FXML
    private Label direccionLabel;

    @FXML
    private VBox muroVBox;

    @FXML
    private Label nombreLabel;

    @FXML
    private VBox productosVBox;

    @FXML
    private Button publicarButton;

    private Vendedor vendedorActual;

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    @FXML
    public void initialize() {
        conectarAlServidor();
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

    public void setVendedorActual(Vendedor vendedor) {
        this.vendedorActual = vendedor;
        mostrarInformacionVendedor();
        cargarProductos();
    }

    private void mostrarInformacionVendedor() {
        if (vendedorActual != null) {
            nombreLabel.setText(vendedorActual.getNombre() + " " + vendedorActual.getApellidos());
            cedulaLabel.setText(String.valueOf(vendedorActual.getCedula()));
            direccionLabel.setText(vendedorActual.getDireccion());
        }
    }
    @SuppressWarnings("unchecked")
    private void cargarProductos() {
        conectarAlServidor();
        try {
            out.writeObject("CARGAR_PRODUCTOS");
            out.writeObject(vendedorActual);
            out.flush();
            List<Producto> productos= (List<Producto>) in.readObject();
            productosVBox.getChildren().clear();
            for (Producto producto : productos) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/proyectofinal/detallesProductos.fxml"));
                Pane productoPane = loader.load();
                DetalleProductoController controller = loader.getController();
                controller.setProducto(producto);
                controller.setVendedorActual(vendedorActual);
                controller.setPerfilVendedorController(this);
                productosVBox.getChildren().add(productoPane);
            }
            muroVBox.getChildren().clear();
            for (Producto producto : productos) {
                if(producto.getEstado().equals(Estado.PUBLICADO)){
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/proyectofinal/publicacion.fxml"));
                    Pane publicacionPane = loader.load();
                    PublicacionController controller = loader.getController();
                    controller.setPublicacion(vendedorActual, producto);
                    muroVBox.getChildren().add(publicacionPane);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            AdministradorLogger.getInstance().escribirLog(PerfilVendedorController.class, "Error al cargar productos: " + e.toString(), Level.WARNING);
        }
    }

    public void PublicarBoton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/proyectofinal/agregarProducto.fxml"));
            Parent root = loader.load();
            AgregarProductoController agregarProductoController = loader.getController();
            agregarProductoController.setVendedorActual(vendedorActual); // Pasar el vendedor actual
            agregarProductoController.setPerfilVendedorController(this);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Agregar Producto");
            stage.show();
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo abrir la ventana para agregar producto.", e.toString());
            AdministradorLogger.getInstance().escribirLog(PerfilVendedorController.class, "No se pudo abrir la ventana para agregar producto: " + e.toString(), Level.WARNING);
        }
        
    }
    public void EliminarBoton() throws ClassNotFoundException{
        conectarAlServidor();
         try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/proyectofinal/eliminarProductos.fxml"));
            Parent root = loader.load();
            EliminarProductoController eliminarProductoController = loader.getController();
            // Cargar el vendedor y productos en la ventana
            eliminarProductoController.setVendedorActual(vendedorActual);
            eliminarProductoController.setPerfilVendedorController(this);
            eliminarProductoController.inicializarDatos();

            // Mostrar la ventana
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Eliminar Producto");
            stage.showAndWait();

        } catch (IOException | ClassCastException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo abrir la ventana de eliminación. ", "Error cargando los productos");
            AdministradorLogger.getInstance().escribirLog(PerfilVendedorController.class, "No se pudo abrir la ventana de eliminación." + e.toString(), Level.WARNING);
        }
    }

    private void mostrarAlerta(String titulo, String encabezado, String contenido) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(encabezado);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
}
