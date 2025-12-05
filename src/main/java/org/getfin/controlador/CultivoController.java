package org.getfin.controlador;

import org.getfin.modelos.Animal;
import org.getfin.modelos.Cultivo;
import org.getfin.servicios.GenericServiceImpl;
import org.getfin.servicios.IGenericService;
import org.getfin.util.HibernateUtil;

import java.util.List;

public class CultivoController {

    private static CultivoController instance;

    public CultivoController(){

    }
    public void guardarCultivo(Cultivo cultivo){
        IGenericService<Cultivo> cliente = new GenericServiceImpl<>(Cultivo.class, HibernateUtil.getSessionFactory());
        cliente.save(cultivo);
    }
    public void eliminarCultivo(Cultivo cultivo){
        IGenericService<Cultivo> cliente = new GenericServiceImpl<>(Cultivo.class, HibernateUtil.getSessionFactory());
        cliente.delete(cultivo);
    }
    public void editarCultivo(Cultivo cultivo){
        IGenericService<Cultivo> cliente = new GenericServiceImpl<>(Cultivo.class, HibernateUtil.getSessionFactory());
        cliente.update(cultivo);
    }
    public List<Cultivo> getCultivo() {
        IGenericService<Cultivo> clienteIGenericService= new GenericServiceImpl<>(Cultivo.class, HibernateUtil.getSessionFactory());
        return clienteIGenericService.getAll();
    }

    public static CultivoController getInstance() {
        if (instance == null) {
            instance = new CultivoController();
        }
        return instance;
    }
    public int totalCultivo (){
        return getInstance().getCultivo().size();
    }

}
