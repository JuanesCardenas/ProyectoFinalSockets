package com.proyectofinal.modelo;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;

import com.proyectofinal.excepciones.AlreadyRegisteredUser;

public class HiloCliente implements Runnable {

    private Socket socketCliente;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public HiloCliente(Socket socketCliente) {
        this.socketCliente = socketCliente;
        try {
            out = new ObjectOutputStream(socketCliente.getOutputStream());
            in = new ObjectInputStream(socketCliente.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        try {
            String comando = (String) in.readObject(); // Leer comando
            switch (comando) {
                case "REGISTRAR_VENDEDOR":
                    Vendedor vendedor = (Vendedor) in.readObject();
                    try {
                        MarketPlaceServicios.getInstance().registrarVendedor(vendedor);
                    } catch (AlreadyRegisteredUser e) {
                        out.writeObject("Error: El usuario ya existe, por favor verificar los datos");
                    }
                    out.writeObject("Vendedor registrado: " + vendedor.getNombre() + " " + vendedor.getApellidos());
                    out.flush();
                    break;
                    
                case "LOGIN":
                    String nombreVendedor = (String) in.readObject();
                    int cedulaVendedor = (int) in.readObject();
                    Vendedor vendedorEncontrado = MarketPlaceServicios.getInstance().buscarVendedor(nombreVendedor, cedulaVendedor);
                    out.writeObject(vendedorEncontrado);
                    out.flush();
                    break;
                case "CARGAR_PRODUCTOS":
                    List<Producto> productos = null;
                    Vendedor vendedorProductos = (Vendedor) in.readObject();
                    productos = vendedorProductos.getProductos();
                    out.writeObject(productos);
                    out.flush();
                    break;
                case "AGREGAR_PRODUCTO":
                        // Recibe el vendedor actual y el producto completo (incluida la imagen)
                        Vendedor vendedorActual = (Vendedor) in.readObject();
                        Producto nuevoProducto = (Producto) in.readObject();
                
                        // Guarda la imagen en el servidor si el producto contiene una imagen
                        if (nuevoProducto.getImagen() != null) {
                            String fileName = nuevoProducto.getImagenNombre();
                            Path serverPath = Paths.get(AdministradorPropiedades.getInstance().getRuta("archivos.directory") + "/" + fileName);
                            Files.write(serverPath, nuevoProducto.getImagen()); // Escribe los bytes de la imagen en el servidor
                
                            System.out.println("Imagen " + fileName + " recibida y guardada en el servidor.");
                        }
                
                        // Agrega el producto al vendedor
                        vendedorActual.publicarProducto(nuevoProducto);
                        out.writeObject(vendedorActual);
                        out.writeObject("EXITO");
                        out.flush();
                    break;
                case "ELIMINAR_PRODUCTO":
                    Vendedor vendedorActual2 = (Vendedor) in.readObject();
                    Producto producto = (Producto) in.readObject();
                    MarketPlaceServicios.getInstance().eliminarProducto(vendedorActual2, producto);
                    out.writeObject(vendedorActual2);
                    out.writeObject("EXITO");
                    out.flush();
                    break;
                case "EDITAR_PRODUCTO":
                    Vendedor vendedorEditarP = (Vendedor) in.readObject();
                    Producto productoEditar = (Producto) in.readObject();
                    MarketPlaceServicios.getInstance().editarProducto(vendedorEditarP, productoEditar);
                    out.writeObject(vendedorEditarP);
                    out.writeObject("EXITO");
                    out.flush();
                    break;   
                default:
                    out.writeObject("Comando no reconocido.");
                    out.flush();
                    break;
            }
        } catch (IOException e) {
                    AdministradorLogger.getInstance().escribirLog(HiloCliente.class, "Error recibiendo el comando", Level.SEVERE);
                } catch (ClassNotFoundException e1) {
                        AdministradorLogger.getInstance().escribirLog(HiloCliente.class, "Error buscando la clase", Level.SEVERE);
                        }finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
                if (socketCliente != null && !socketCliente.isClosed()) socketCliente.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
