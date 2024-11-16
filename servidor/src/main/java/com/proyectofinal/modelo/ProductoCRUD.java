package com.proyectofinal.modelo;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import com.proyectofinal.excepciones.AlreadyRegisteredUser;

public class ProductoCRUD {

    private static final String ARCHIVO_PRODUCTOS = AdministradorPropiedades.getInstance().getRuta("persistencia.directory") + "/Productos.dat";  // Archivo donde se almacenan los productos

    // Método para obtener todos los productos (deserialización)
    @SuppressWarnings("unchecked")
    public List<Producto> obtenerTodosLosProductos() {
        List<Producto> productos = null;
            // Guardar la lista actualizada en el archivo (serialización)
            HiloSerializador cargar = new HiloSerializador(productos, ARCHIVO_PRODUCTOS, "binario", false);
            Thread hilo = new Thread(cargar);
            hilo.start();
            try {
                hilo.join();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                AdministradorLogger.getInstance().escribirLog(ProductoCRUD.class, e.toString(), java.util.logging.Level.SEVERE);
            }
            productos = (List<Producto>) cargar.getResultadoDeserializacion();

        // Si no hay productos deserializados, retornar una lista vacía
        if (productos == null) {
            productos = new ArrayList<>();
        }
        return productos;
    }

    // Método para registrar un nuevo producto
    public void registrarProducto(Producto nuevoProducto) throws IOException, AlreadyRegisteredUser {
        List<Producto> productos = obtenerTodosLosProductos();

        // Verificar si el producto ya está registrado (puedes definir tu propia lógica de comparación)
        for (Producto producto : productos) {
            if (producto.getCodigo() == nuevoProducto.getCodigo()) { // Cambiar esto según tu lógica de identificación
                AdministradorLogger.getInstance().escribirLog(ProductoCRUD.class, "El producto ya está registrado.", java.util.logging.Level.INFO);
                throw new AlreadyRegisteredUser("El producto con el codigo " + nuevoProducto.getCodigo() + " ya está registrado.");
            }
        }

        // Agregar el nuevo producto a la lista
        productos.add(nuevoProducto);

        // Guardar la lista actualizada en el archivo (serialización)
        HiloSerializador guardar = new HiloSerializador(productos, ARCHIVO_PRODUCTOS, "binario", true);
        Thread hilo = new Thread(guardar);
        hilo.start();
        try {
            hilo.join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            AdministradorLogger.getInstance().escribirLog(ProductoCRUD.class, e.toString(), java.util.logging.Level.SEVERE);
        }
        AdministradorLogger.getInstance().escribirLog(ProductoCRUD.class, "Producto registrado correctamente.", java.util.logging.Level.INFO);
    }

    // Método para actualizar un producto
    public void actualizarProducto(Producto productoActualizado) throws IOException {
        List<Producto> productos = obtenerTodosLosProductos();
        boolean encontrado = false;

        // Buscar el producto por ID y actualizarlo
        for (int i = 0; i < productos.size(); i++) {
            Producto productoExistente = productos.get(i);
            if (productoExistente.getCodigo() == productoActualizado.getCodigo()) {
                productos.set(i, productoActualizado);  // Reemplaza el producto
                encontrado = true;
                break;
            }
        }

        if (encontrado) {
            // Serializar la lista actualizada de productos
            // Guardar la lista actualizada en el archivo (serialización)
            HiloSerializador guardar = new HiloSerializador(productos, ARCHIVO_PRODUCTOS, "binario", true);
            Thread hilo = new Thread(guardar);
            hilo.start();
            try {
                hilo.join();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                AdministradorLogger.getInstance().escribirLog(ProductoCRUD.class, e.toString(), java.util.logging.Level.SEVERE);
            }
            AdministradorLogger.getInstance().escribirLog(ProductoCRUD.class, "Producto actualizado correctamente.", java.util.logging.Level.INFO);
        } else {
            AdministradorLogger.getInstance().escribirLog(ProductoCRUD.class, "No se encontró un producto con el ID: " + productoActualizado.getCodigo(), java.util.logging.Level.INFO);
        }
    }

    // Método para eliminar un producto
    public void eliminarProducto(Producto producto) throws IOException {
        List<Producto> productos = obtenerTodosLosProductos();
        boolean eliminado = false;

        // Buscar y eliminar el producto por ID
        for (int i = 0; i < productos.size(); i++) {
            Producto productoExistente = productos.get(i);
            if (productoExistente.getCodigo().equals(producto.getCodigo())) {
                productos.remove(i);  // Elimina el producto
                eliminado = true;
                break;
            }
        }

        if (eliminado) {
            // Serializar la lista actualizada después de eliminar el producto
            // Guardar la lista actualizada en el archivo (serialización)
            HiloSerializador guardar = new HiloSerializador(productos, ARCHIVO_PRODUCTOS, "binario", true);
            Thread hilo = new Thread(guardar);
            hilo.start();
            try {
                hilo.join();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                AdministradorLogger.getInstance().escribirLog(ProductoCRUD.class, e.toString(), java.util.logging.Level.SEVERE);
            }
            
            AdministradorLogger.getInstance().escribirLog(ProductoCRUD.class, "Producto eliminado correctamente.", java.util.logging.Level.INFO);
        } else {
            AdministradorLogger.getInstance().escribirLog(ProductoCRUD.class, "No se encontró un producto con el ID: " + producto.getCodigo(), java.util.logging.Level.INFO);
        }
    }

    // Método para buscar un producto por ID
    public Producto buscarProducto(String codigo) {
        List<Producto> productos = obtenerTodosLosProductos();
        for (Producto producto : productos) {
            if (producto.getCodigo().equals(codigo)) {
                AdministradorLogger.getInstance().escribirLog(ProductoCRUD.class, "Producto encontrado correctamente.", java.util.logging.Level.INFO);
                return producto; 
            }
        }
        AdministradorLogger.getInstance().escribirLog(ProductoCRUD.class, "No se encontró un producto con el ID: " + codigo, java.util.logging.Level.INFO);
        return null;  // Si no se encuentra el producto
    }
}
