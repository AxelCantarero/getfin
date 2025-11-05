package org.getfin.servicios;

import org.getfin.dao.IGenericDAO;

import java.util.List;

public interface IGenericService<T> extends IGenericDAO<T> {
    List<T> getAll();
    void deleteAll();
    T getById(int id);
    T getId(Integer id);
    T getByName(String name);
}