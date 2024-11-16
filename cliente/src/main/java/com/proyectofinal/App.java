package com.proyectofinal;

import com.proyectofinal.modelo.AdministradorLogger;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(@SuppressWarnings("exports") Stage stage) throws Exception {
        //inicializamos el logger
        AdministradorLogger.getInstance().inicializarLogger();
        // Inicializa el Stage en el manejador de escenas
        ManejadorEscenas.inicializar(stage);
        
        // Cambia a la escena de login
        ManejadorEscenas.cambiarEscena("login");
        
        // Muestra el escenario
        stage.show();
    }

    public static void main(String[] args) {
        
        launch(args);
    }

}