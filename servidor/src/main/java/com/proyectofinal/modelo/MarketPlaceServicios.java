package com.proyectofinal.modelo;

import java.io.IOException;
import java.util.List;

import com.proyectofinal.excepciones.AlreadyRegisteredUser;

public class MarketPlaceServicios {

    private static MarketPlaceServicios instancia;
    private VendedorCRUD vendedorCRUD;  // Instancia de VendedorCRUD para la gestión de vendedores
    private ProductoCRUD productoCRUD;
    private PublicacionCrud publicacionCrud;

    public MarketPlaceServicios(){
        this.vendedorCRUD = new VendedorCRUD();
        this.productoCRUD = new ProductoCRUD();
        this.publicacionCrud = new PublicacionCrud();
    }

    // Método para obtener la única instancia de la clase
    public static MarketPlaceServicios getInstance(){
        if (instancia == null) {
            instancia = new MarketPlaceServicios();
        }
        return instancia;
    }

    // Registrar un nuevo vendedor usando VendedorCRUD
    public void registrarVendedor(Vendedor nuevoVendedor) throws IOException, AlreadyRegisteredUser {
        vendedorCRUD.registrarVendedor(nuevoVendedor);
    }

    // Método para buscar un vendedor por nombre y cédula, delegando a VendedorCRUD
    public Vendedor buscarVendedor(String nombre, int cedula) {
        return vendedorCRUD.buscarVendedor(nombre, cedula);  // Delegar a VendedorCRUD
    }

    public void eliminarProducto(Vendedor vendedor, Producto producto) throws IOException {
        vendedor.getProductos().removeIf(p -> producto.getCodigo().equals(p.getCodigo()));
        productoCRUD.eliminarProducto(producto);
        vendedorCRUD.actualizarVendedor(vendedor);
        AdministradorArchivo.eliminarImagen(producto.getImagenNombre());
    }

    public void editarProducto(Vendedor vendedor, Producto producto) throws IOException{
        vendedor.getProductos().removeIf(p -> producto.getCodigo().equals(p.getCodigo()));
        vendedor.getProductos().add(producto);
        productoCRUD.actualizarProducto(producto);
        vendedorCRUD.actualizarVendedor(vendedor);
    }

    public List<Publicacion> cargarPublicaciones() {
        return publicacionCrud.obtenerTodosLasPublicaciones();
    }
    
}
