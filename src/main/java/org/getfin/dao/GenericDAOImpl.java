package org.getfin.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Map;

public class GenericDAOImpl<T> implements  IGenericDAO<T>{
    private Class<T> cl;
    private SessionFactory sessionFactory;

    public GenericDAOImpl(Class<T> cl, SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        if(sessionFactory == null){
            throw new RuntimeException("sessionFactory is null");
        }
    }

    @Override
    public T get(Class<T> cl, Integer id) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        T element = (T) session.get(cl, id);
        session.getTransaction().commit();
        return element;
    }

    @Override
    public T save(T object) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(object);
        session.getTransaction().commit();
        return object;
    }

    @Override
    public void update(T object) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(object);
        session.getTransaction().commit();
    }

    @Override
    public void delete(T object) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.delete(object);
        session.getTransaction().commit();

    }

    @Override
    public List<T> query(String hsql, Map<String, Object> params){
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            Query query = session.createQuery(hsql);
            if(params != null){
                for(String i: params.keySet()){
                    query.setParameter(i, params.get(i));
                }
            }

            List<T> result = null;
            if((!hsql.toUpperCase().contains("DELETE"))
                    && (!hsql.toUpperCase().contains("UPDATE"))
                    && (!hsql.toUpperCase().contains("INSERT"))
            ) {
                result = query.list();
            }
            session.getTransaction().commit();
            return result;
        }

    @Override
    public T findOne(String hsql, Map<String, Object> params) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query<T> query = session.createQuery(hsql, cl);
        if (params != null) {
            for (String key : params.keySet()) {
                query.setParameter(key, params.get(key));
            }
        }
        return query.uniqueResult();
    }

}

