package org.getfin.controlador;

import org.getfin.modelos.Cultivo;
import org.getfin.modelos.Visita;
import org.getfin.servicios.GenericServiceImpl;
import org.getfin.servicios.IGenericService;
import org.getfin.util.HibernateUtil;

import java.math.BigDecimal;
import java.util.List;

public class VisitaController {

    private static VisitaController instance;

    public VisitaController(){

    }
    public void guardarVisita(Visita visita){
        IGenericService<Visita> cliente = new GenericServiceImpl<>(Visita.class, HibernateUtil.getSessionFactory());
        cliente.save(visita);
    }
    public void eliminarVisita(Visita visita){
        IGenericService<Visita> cliente = new GenericServiceImpl<>(Visita.class, HibernateUtil.getSessionFactory());
        cliente.delete(visita);
    }
    public void editarVisita(Visita visita){
        IGenericService<Visita> cliente = new GenericServiceImpl<>(Visita.class, HibernateUtil.getSessionFactory());
        cliente.update(visita);
    }
    public List<Visita> getVisita() {
        IGenericService<Visita> clienteIGenericService= new GenericServiceImpl<>(Visita.class, HibernateUtil.getSessionFactory());
        return clienteIGenericService.getAll();
    }
    public int totalVisitasMes(int mes, int anio) {
        return getInstance()
                .getVisita()
                .stream()
                .filter(visita -> visita.getFecha().getMonthValue() == mes
                        && visita.getFecha().getYear() == anio)
                .mapToInt(visita -> visita.getTotal() != null ? visita.getTotal() : 0)
                .sum();
    }

    public static VisitaController getInstance() {
        if (instance == null) {
            instance = new VisitaController();
        }
        return instance;
    }
}
