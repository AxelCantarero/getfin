package org.getfin.controlador;

import org.getfin.modelos.Cultivo;
import org.getfin.modelos.Producto;
import org.getfin.servicios.GenericServiceImpl;
import org.getfin.servicios.IGenericService;
import org.getfin.util.HibernateUtil;

import java.util.List;

public class ProductoController {

    private static ProductoController instance;

    public ProductoController(){

    }
    public void guardarCultivo(Producto producto){
        IGenericService<Producto> cliente = new GenericServiceImpl<>(Producto.class, HibernateUtil.getSessionFactory());
        cliente.save(producto);
    }
    public void eliminarCultivo(Producto producto){
        IGenericService<Producto> cliente = new GenericServiceImpl<>(Producto.class, HibernateUtil.getSessionFactory());
        cliente.delete(producto);
    }
    public void editarCultivo(Producto producto){
        IGenericService<Producto> cliente = new GenericServiceImpl<>(Producto.class, HibernateUtil.getSessionFactory());
        cliente.update(producto);
    }
    public List<Producto> getCultivo() {
        IGenericService<Producto> clienteIGenericService= new GenericServiceImpl<>(Producto.class, HibernateUtil.getSessionFactory());
        return clienteIGenericService.getAll();
    }

    public static ProductoController getInstance() {
        if (instance == null) {
            instance = new ProductoController();
        }
        return instance;
    }
}
