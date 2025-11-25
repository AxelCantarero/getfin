package org.getfin.controlador;

import org.getfin.modelos.Transaccion;
import org.getfin.modelos.Usuario;
import org.getfin.servicios.GenericServiceImpl;
import org.getfin.servicios.IGenericService;
import org.getfin.util.HibernateUtil;

import java.util.List;

public class TransaccionController {

    private static TransaccionController instance;

    public TransaccionController(){

    }
    public void guardarTransaccion(Transaccion transaccion){
        IGenericService<Transaccion> clientes = new GenericServiceImpl<>(Transaccion.class, HibernateUtil.getSessionFactory());
        clientes.save(transaccion);
    }
    public void eliminarTransaccion(Transaccion transaccion){
        IGenericService<Transaccion> clientes = new GenericServiceImpl<>(Transaccion.class, HibernateUtil.getSessionFactory());
        clientes.delete(transaccion);
    }
    public void editarTransaccion(Transaccion cliente){
        IGenericService<Transaccion> clientes = new GenericServiceImpl<>(Transaccion.class, HibernateUtil.getSessionFactory());
        clientes.update(cliente);
    }
    public List<Transaccion> getTransacciones() {
        IGenericService<Transaccion> clienteIGenericService= new GenericServiceImpl<>(Transaccion.class, HibernateUtil.getSessionFactory());
        return clienteIGenericService.getAll();
    }

    public static TransaccionController getInstance() {
        if (instance == null) {
            instance = new TransaccionController();
        }
        return instance;
    }
}
