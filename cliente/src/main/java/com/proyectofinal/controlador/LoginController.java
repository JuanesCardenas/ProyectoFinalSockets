package com.proyectofinal.controlador;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;

import com.proyectofinal.ManejadorEscenas;
import com.proyectofinal.modelo.AdministradorLogger;
import com.proyectofinal.modelo.Vendedor;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class LoginController {
    
    @FXML
    private TextField apellidoRegistroLabel;

    @FXML
    private PasswordField cedulaConfirRegisLabel;

    @FXML
    private PasswordField cedulaRegistroLabel;

    @FXML
    private PasswordField contraseñaLoginLabel;

    @FXML
    private Button crearVendedorButton;

    @FXML
    private TextField direccionRegistroLabel;

    @FXML
    private Button loginButton;

    @FXML
    private AnchorPane loginPane;

    @FXML
    private TextField nombreLoginLabel;

    @FXML
    private TextField nombreRegistroLabel;

    @FXML
    private AnchorPane opcionPane;

    @FXML
    private AnchorPane registroPane;

    @FXML
    private Button regsitrarButton;

    @FXML
    private Button tengoCuentaButton;

    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    @FXML
    public void initialize() {
        
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
    
    public void manejarLogin() throws ClassNotFoundException, IOException {

        conectarAlServidor();
        // Asigna los datos ingresados en los campos de texto como variables.
        String nombreVendedor = nombreLoginLabel.getText();
        String cedula = contraseñaLoginLabel.getText();
        int cedulaVendedor = -1; // Inicializado con un valor temporal

        // Verifica si la cédula ingresada es un número válido
        try {
            cedulaVendedor = Integer.parseInt(cedula);  // Cambiamos a long si la cédula puede ser un número grande
        } catch (NumberFormatException e) {
            // Cuando la cédula ingresada no es un número
            mostrarAlerta("Error", "Cédula no es numérica.", "Verifique los datos ingresados.");
            AdministradorLogger.getInstance().escribirLog(LoginController.class, "Cédula no es numérica.", Level.WARNING);
            return;
        }

        // mandar comando para buscar vendedor
        out.writeObject("LOGIN");
        out.writeObject(nombreVendedor);
        out.writeObject(cedulaVendedor);
        out.flush();
        Vendedor vendedorEncontrado = (Vendedor) in.readObject();

        if (vendedorEncontrado != null) {
            // Si se encuentra el vendedor, cambiar a la escena del Marketplace
            try {
                ManejadorEscenas.cambiarEscenaConDatos("perfilVendedor", vendedorEncontrado);
            } catch (IOException e) {
                e.printStackTrace();
                AdministradorLogger.getInstance().escribirLog(LoginController.class, e.toString() + "Error al cambiar de escena.", Level.SEVERE);
            }
        } else {
            // Si no se encuentra el vendedor o los datos no coinciden
            mostrarAlerta("Error", "Nombre o cédula no coinciden.", "Verifique los datos ingresados.");
            AdministradorLogger.getInstance().escribirLog(LoginController.class, "Nombre o cédula no coinciden.", Level.WARNING);
        }
    }

    public void manejarRegistro() throws IOException, ClassNotFoundException {

        conectarAlServidor();
        // Asigna los datos ingresados en los campos de texto como variables
        String nombreVendedor = nombreRegistroLabel.getText();
        String apellidoVendedor = apellidoRegistroLabel.getText();
        String cedula = cedulaRegistroLabel.getText();
        String cedulaConfirmada = cedulaConfirRegisLabel.getText();
        String direccion = direccionRegistroLabel.getText();
        int cedulaVendedor;
    
        // Verifica si la cédula ingresada es un número válido
        try {
            cedulaVendedor = Integer.parseInt(cedula);  // Convertir la cédula a un número
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Cédula inválida.", "La cédula debe contener solo números.");
            AdministradorLogger.getInstance().escribirLog(LoginController.class, "Cédula no es numérica.", Level.WARNING);
            return;
        }
    
        // Validar si la cédula confirmada coincide con la cédula ingresada
        if (!cedula.equals(cedulaConfirmada)) {
            mostrarAlerta("Error", "Cédulas no coinciden.", "La cédula ingresada y la cédula confirmada no coinciden.");
            AdministradorLogger.getInstance().escribirLog(LoginController.class, "Las cédulas no coinciden.", Level.WARNING);
            return;
        }
    
        // Crear una instancia del vendedor
        Vendedor nuevoVendedor = new Vendedor(nombreVendedor, apellidoVendedor, cedulaVendedor, direccion);
    
        // Intentar registrar el vendedor usando MarketplaceService
        try {
            out.writeObject("REGISTRAR_VENDEDOR");
            out.writeObject(nuevoVendedor);
            out.flush();
            String respuestaServidor = (String) in.readObject();
            if(!respuestaServidor.startsWith("Error: ")){
                mostrarInformacion("Éxito", "Registro completado.", "El vendedor ha sido registrado exitosamente.");
                AdministradorLogger.getInstance().escribirLog(LoginController.class, "Vendedor registrado: " + nombreVendedor, Level.INFO);
            }else{
                mostrarAlerta("Error", "Usuario ya existente ", respuestaServidor);
                AdministradorLogger.getInstance().escribirLog(LoginController.class, "El vendedor " + nombreVendedor + " existe", Level.WARNING);
            }  
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Error de registro.", "Hubo un problema al registrar al vendedor.");
            AdministradorLogger.getInstance().escribirLog(LoginController.class, "Error al registrar vendedor.", Level.SEVERE);
        }
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

    public void registrarseBoton(ActionEvent event){

        TranslateTransition slider = new TranslateTransition();

        if(event.getSource() == crearVendedorButton){
            slider.setNode(opcionPane);
            slider.setToX(300);
            slider.setDuration(Duration.seconds(.5));

            slider.setOnFinished((ActionEvent e) ->{
                tengoCuentaButton.setVisible(true);
                crearVendedorButton.setVisible(false);
            });

            slider.play();
        }else if(event.getSource() == tengoCuentaButton){
            slider.setNode(opcionPane);
            slider.setToX(0);
            slider.setDuration(Duration.seconds(.5));

            slider.setOnFinished((ActionEvent e) ->{
                tengoCuentaButton.setVisible(false);
                crearVendedorButton.setVisible(true);
            });

            slider.play();
        }
    }
}
