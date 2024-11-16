module com.proyectofinal {
    requires javafx.controls;
    requires transitive javafx.fxml;
    requires java.desktop;
    requires transitive java.logging;
    requires javafx.graphics;
    requires javafx.base;
    
    opens com.proyectofinal to javafx.fxml;
    exports com.proyectofinal;
    exports com.proyectofinal.modelo;
    exports com.proyectofinal.controlador;
    opens com.proyectofinal.controlador to javafx.fxml;

}
