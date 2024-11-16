package com.proyectofinal.modelo;

import java.io.IOException;

public class HiloSerializador implements Runnable {

    private Object objeto;
    private String nombreArchivo;
    private String tipoSerializacion;
    private boolean esSerializar;
    private Object resultadoDeserializacion; // Atributo para almacenar el resultado

    // Constructor para serializar
    public HiloSerializador(Object objeto, String nombreArchivo, String tipoSerializacion, boolean esSerializar) {
        this.objeto = objeto;
        this.nombreArchivo = nombreArchivo;
        this.tipoSerializacion = tipoSerializacion;
        this.esSerializar = esSerializar;
    }

    // Constructor para deserializar
    public HiloSerializador(String nombreArchivo, String tipoSerializacion, boolean esSerializar) {
        this.nombreArchivo = nombreArchivo;
        this.tipoSerializacion = tipoSerializacion;
        this.esSerializar = esSerializar;
    }

    @Override
    public void run() {
        try {
            if (esSerializar) {
                if ("xml".equalsIgnoreCase(tipoSerializacion)) {
                    AdministradorPersistencia.serializarObjetoXML(objeto, nombreArchivo);
                } else if ("binario".equalsIgnoreCase(tipoSerializacion)) {
                    AdministradorPersistencia.serializarObjetoBinario(objeto, nombreArchivo);
                }
                System.out.println("Serialización completada: " + nombreArchivo);
            } else {
                if ("xml".equalsIgnoreCase(tipoSerializacion)) {
                    resultadoDeserializacion = AdministradorPersistencia.deserializarObjetoXML(nombreArchivo);
                } else if ("binario".equalsIgnoreCase(tipoSerializacion)) {
                    resultadoDeserializacion = AdministradorPersistencia.deserializarObjetoBinario(nombreArchivo);
                }
                System.out.println("Deserialización completada: " + nombreArchivo);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error durante el proceso de " + (esSerializar ? "serialización" : "deserialización"));
        }
    }

    // Método para obtener el resultado de la deserialización
    public Object getResultadoDeserializacion() {
        return resultadoDeserializacion;
    }
}
