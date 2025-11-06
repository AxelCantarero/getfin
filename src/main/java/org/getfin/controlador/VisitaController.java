package org.getfin.controlador;

import org.getfin.modelos.Cultivo;
import org.getfin.modelos.Visita;
import org.getfin.servicios.GenericServiceImpl;
import org.getfin.servicios.IGenericService;
import org.getfin.util.HibernateUtil;

import java.util.List;

public class VisitaController {

    private static VisitaController instance;

    public VisitaController(){

    }
    public void guardarCultivo(Visita visita){
        IGenericService<Visita> cliente = new GenericServiceImpl<>(Visita.class, HibernateUtil.getSessionFactory());
        cliente.save(visita);
    }
    public void eliminarCultivo(Visita visita){
        IGenericService<Visita> cliente = new GenericServiceImpl<>(Visita.class, HibernateUtil.getSessionFactory());
        cliente.delete(visita);
    }
    public void editarCultivo(Visita visita){
        IGenericService<Visita> cliente = new GenericServiceImpl<>(Visita.class, HibernateUtil.getSessionFactory());
        cliente.update(visita);
    }
    public List<Visita> getCultivo() {
        IGenericService<Visita> clienteIGenericService= new GenericServiceImpl<>(Visita.class, HibernateUtil.getSessionFactory());
        return clienteIGenericService.getAll();
    }

    public static VisitaController getInstance() {
        if (instance == null) {
            instance = new VisitaController();
        }
        return instance;
    }
}
