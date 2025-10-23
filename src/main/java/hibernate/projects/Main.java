package hibernate.projects;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;

public class Main {

    private static EntityManagerFactory entityManagerFactory;

    public static EntityManagerFactory getEntityManagerFactory() {
        try {
            if (entityManagerFactory == null)
                entityManagerFactory = Persistence.createEntityManagerFactory("cineversePU");

        } catch (PersistenceException e) {
            System.err.println("Error al construir l'EntityManagerFactory: " + e.getMessage());
            throw new ExceptionInInitializerError(e);
        }
        return entityManagerFactory;
    }

    public static void main(String[] args) {
        EntityManager entityManager = null;
        try {
            entityManager = getEntityManagerFactory().createEntityManager();

        } catch (Exception e) {
            System.out.println("Error durant l'execució del programa: " + e.getMessage());
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();

            if (entityManagerFactory != null && entityManagerFactory.isOpen())
                entityManagerFactory.close();
        }

    }
}