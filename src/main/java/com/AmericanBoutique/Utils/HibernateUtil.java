package com.AmericanBoutique.Utils;

import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * HibernateUtil is a session factory helper class that builds a
 * secure connection to a database and permits CRUD operations on a table.
 * The session configuration derives from the hibernate.cfg.xml file in
 * the 'resources' folder.
 *
 *  <b style="color:red">WARNING! </b>
 *  <b>DO NOT MODIFY THIS CODE</b>
 */
public class HibernateUtil {
    private HibernateUtil() {
        // Utility classes should not have public constructors
    }

    /**
     * @Getter builds a standard getter method for the object
     * sessionFactory.
     */
    @Getter
    private static SessionFactory sessionFactory = buildSessionFactory();

    /**
     * Method builds a session factory from the 'hibernate.cfg.xml' file
     * in the 'resources' folder and returns a sessionFactory object.
     * @return
     */
    private static SessionFactory buildSessionFactory()
    {
        try
        {
            if (sessionFactory == null)
            {
                // Create the StandardServiceRegistry
                StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
                        .configure("hibernate.cfg.xml") // Load configuration from hibernate.cfg.xml file
                        .build();


                // Create MetadataSources
                Metadata metaData = new MetadataSources(standardRegistry)
                        .getMetadataBuilder()
                        .build();

                sessionFactory = metaData.getSessionFactoryBuilder().build();
            }
            return sessionFactory;
        } catch (Exception ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Closes the session factory.
     */
    public static void shutdown() {
        getSessionFactory().close();
    }

}

/*
                 Object-Relational Mapping (ORM) framework:
                 MetadataSources: is used to bootstrap the metadata for an application, Metadata in Hibernate refers to
                            information about the mapping between Java objects and database tables.
                 standardRegistry: is an instance of the StandardServiceRegistry class, this class is responsible for
                            bootstrapping and configuring Hibernate services. Services in Hibernate are components responsible
                            for various tasks, such as connection management, transaction management, etc.
                 new MetadataSources(standardRegistry): This creates a new instance of MetadataSources and associates it
                            with the provided standardRegistry. This is setting up the source of metadata for Hibernate.
                .getMetadataBuilder(): This method retrieves a MetadataBuilder instance from the MetadataSources. The
                            MetadataBuilder is used to build the metadata for the application.
                .build(): method is called on the MetadataBuilder to actually build the metadata. The Metadata object is
                            returned, which contains information about the mapping between Java objects and database tables.
                So, in summary, this code is initializing and building the metadata for a Hibernate application,
                which is crucial for mapping Java objects to database tables and performing ORM operations.
*/