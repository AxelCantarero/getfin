package org.getfin.controlador;

import org.getfin.modelos.Animal;
import org.getfin.servicios.GenericServiceImpl;
import org.getfin.servicios.IGenericService;
import org.getfin.util.HibernateUtil;

import java.util.List;

public class AnimalController {

    private static AnimalController instance;

    public AnimalController(){
    }
    public void guardarAnimal(Animal animal){
        IGenericService<Animal> cliente = new GenericServiceImpl<>(Animal.class, HibernateUtil.getSessionFactory());
        cliente.save(animal);
    }
    public void eliminarAnimal(Animal animal){
        IGenericService<Animal> cliente = new GenericServiceImpl<>(Animal.class, HibernateUtil.getSessionFactory());
        cliente.delete(animal);
    }
    public void editarAnimal(Animal animal){
        IGenericService<Animal> cliente = new GenericServiceImpl<>(Animal.class, HibernateUtil.getSessionFactory());
        cliente.update(animal);
    }
    public List<Animal> getAnimal() {
        IGenericService<Animal> clienteIGenericService= new GenericServiceImpl<>(Animal.class, HibernateUtil.getSessionFactory());
        return clienteIGenericService.getAll();
    }

    public static AnimalController getInstance() {
        if (instance == null) {
            instance = new AnimalController();
        }
        return instance;
    }
    public int totalAnimal() {
        return getInstance().getAnimal().size();
    }


}
