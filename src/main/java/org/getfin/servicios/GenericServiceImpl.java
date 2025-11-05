package org.getfin.servicios;
import org.getfin.dao.GenericDAOImpl;
import org.getfin.dao.IGenericDAO;
import org.hibernate.SessionFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenericServiceImpl<T> implements IGenericService<T> {

    private IGenericDAO<T> dao;
    private Class<T> cl;
    SessionFactory session;

    public GenericServiceImpl(Class<T> cl, SessionFactory session) {
        this.cl = cl;
        this.session = session;
        dao = new GenericDAOImpl<>(cl,session);
    }

    @Override
    public List<T> getAll() {
        return dao.query("FROM "+cl.getSimpleName(),null)==null?null:dao.query("FROM "+cl.getSimpleName(),null);
    }

    @Override
    public void deleteAll() {

    }

    @Override
    public T getById(int id) {
        return dao.get(cl,id);
    }

    @Override
    public T getId(Integer id) {
        return dao.get(cl,id);
    }

    @Override
    public T getByName(String name) {
        String field = "nombre";
        if (cl.getSimpleName().equals("Usuario")) {
            field = "nombreUsuario";
        }
        String hql = "FROM " + cl.getSimpleName() + " e WHERE e." + field + " = :n";
        Map<String, Object> params = new HashMap<>();
        params.put("n", name);
        List<T> list = dao.query(hql, params);
        return (list != null && !list.isEmpty()) ? list.get(0) : null;

    }

    @Override
    public T get(Class<T> cl, Integer id) {
        return (T) dao.get(cl,id);
    }

    @Override
    public T save(T object) {
        return (T) dao.save(object);
    }

    @Override
    public void update(T object) {
        dao.update(object);

    }

    @Override
    public void delete(T object) {
        dao.delete(object);

    }

    @Override
    public List<T> query(String hsql, Map<String, Object> params) {
        return (List<T>) dao.query(hsql,params);
    }

    @Override
    public T findOne(String hsql, Map<String, Object> params) {
        List<T> list = dao.query(hsql, params);
        return (list != null && !list.isEmpty()) ? list.get(0) : null;
    }

}
