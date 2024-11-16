package com.proyectofinal.modelo;

import java.util.ArrayList;
import java.util.List;

import com.proyectofinal.excepciones.AlreadyRegisteredUser;

import java.io.IOException;
import java.io.Serializable;

public class Vendedor  implements Serializable {

    private static final long serialVersionUID = 1L;
    private String nombre;
    private String apellidos;
    private int cedula; // Identificador único
    private String direccion;
    private List<Vendedor> contactos;   // Lista de contactos (vendedores aliados)
    private Muro muro;                  // Muro donde se publican productos y mensajes
    private List<Producto> productos;   // Lista de productos del vendedor

    public Vendedor(String nombre, String apellidos, int cedula, String direccion) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.cedula = cedula;
        this.direccion = direccion;
        this.contactos = new ArrayList<>();
        this.muro = new Muro();         // Crear un muro vacío para el vendedor
        this.productos = new ArrayList<>(); // Inicializar la lista de productos
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public int getCedula() {
        return cedula;
    }

    public void setCedula(int cedula) {
        this.cedula = cedula;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public List<Vendedor> getContactos() {
        return contactos;
    }

    public Muro getMuro() {
        return muro;
    }

    public List<Producto> getProductos() {
        return productos; // Getter para obtener la lista de productos
    }

    // Métodos para gestionar los contactos
    public void agregarContacto(Vendedor nuevoContacto) {
        if (!contactos.contains(nuevoContacto)) {
            contactos.add(nuevoContacto);
            nuevoContacto.getContactos().add(this);  // Vinculación bidireccional
        }
    }

    public void eliminarContacto(Vendedor contacto) {
        contactos.remove(contacto);
        contacto.getContactos().remove(this);  // Eliminar la relación bidireccional
    }

    // Método para publicar un producto en el muro y la lista de productos
    public void publicarProducto(Producto producto) {
        muro.agregarProductoPublicado(producto); // Agregar al muro
        productos.add(producto); // Agregar a la lista de productos

        // Serializar la lista de productos del vendedor
        try {
            ProductoCRUD productoCRUD = new ProductoCRUD(); // Crear instancia de ProductoCRUD
            productoCRUD.registrarProducto(producto); // Registrar el producto
            
        } catch (IOException | AlreadyRegisteredUser e) {
            AdministradorLogger.getInstance().escribirLog(Vendedor.class, e.toString() + " " + "Error al registrar el producto.", java.util.logging.Level.SEVERE);
        }

        try {
            VendedorCRUD vendedorCRUD = new VendedorCRUD(); // Crear instancia de VendedorCRUD
            vendedorCRUD.actualizarVendedor(this); // Actualizar el vendedor
        } catch (IOException e) {
            AdministradorLogger.getInstance().escribirLog(Vendedor.class, e.toString() + " " + "Error al actualizar el vendedor.", java.util.logging.Level.SEVERE);
        }
    }

    // Método para agregar un mensaje al muro
    public void enviarMensaje(Vendedor destinatario, String contenido) {
        Mensaje mensaje = new Mensaje(this, destinatario, contenido);
        this.muro.agregarMensaje(mensaje);
        destinatario.getMuro().agregarMensaje(mensaje); // Añadir mensaje al muro del destinatario
    }

    @Override
    public String toString() {
        return "Vendedor{" +
                "nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", cedula='" + cedula + '\'' +
                ", direccion='" + direccion + '\'' +
                ", contactos=" + contactos.size() +
                ", productos=" + productos.size() + // Muestra el número de productos
                ", muro=" + muro +
                '}';
    }
}
