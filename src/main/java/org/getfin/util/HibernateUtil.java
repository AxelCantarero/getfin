package org.getfin.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HibernateUtil {
    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;
    private static final Logger logger = Logger.getLogger(HibernateUtil.class.getName());


    public static SessionFactory getSessionFactory() {

        if (sessionFactory == null) {
            try {
                registry = new StandardServiceRegistryBuilder().configure().build();
                MetadataSources sources = new MetadataSources(registry);
                MetadataSources metadataSources = new MetadataSources(registry);
                Metadata metadata = sources.getMetadataBuilder().build();
                sessionFactory = metadata.getSessionFactoryBuilder().build();
            }catch (Exception e){
                e.printStackTrace();
                if (registry!=null){
                    StandardServiceRegistryBuilder.destroy(registry);
                }
            }
        }
        return sessionFactory;
    }

    public static boolean isSessionFactoryAvailable() {
        try {
            return getSessionFactory() != null;
        } catch (Exception e) {
            logger.log(Level.WARNING, "SessionFactory no disponible", e);
            return false;
        }
    }
}
