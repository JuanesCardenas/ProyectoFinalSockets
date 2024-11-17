package com.proyectofinal.modelo;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class AdministradorPropiedades {

    private Properties propiedades;
    private static AdministradorPropiedades instancia;
    
    // Singleton: Constructor privado
    public AdministradorPropiedades(){
        propiedades = new Properties();
        try {
            propiedades.load(new FileInputStream("C:/Users/Lenovo/Documents/4 Semestre/Programación III/ProyectoFinalSockets/ProyectoFinalSockets/servidor/src/main/resources/Config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para obtener la única instancia de la clase
    public static AdministradorPropiedades getInstance(){
        if (instancia == null) {
            instancia = new AdministradorPropiedades();
        }
        return instancia;
    }

    public String getRuta(String key) {
        return propiedades.getProperty(key);
    }
}
