package hibernate.projects;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Main {
    
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        try {
            if (sessionFactory == null) {
                sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
            }
        } catch (HibernateException e) {
            System.err.println("Error al construir la Session Factory " + e.getMessage());
            throw new ExceptionInInitializerError(e);
        }
        return sessionFactory;
    }

    public static void main(String[] args) {
       try (Session session = getSessionFactory().openSession()) {

       } catch (Exception e) {
			System.out.println("Error durant l'execució del programa " + e.getMessage());
		} finally { 
			if (sessionFactory != null && sessionFactory.isOpen()) {
				sessionFactory.close();
			}
		}


    }
}