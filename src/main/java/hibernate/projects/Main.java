package hibernate.projects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import hibernate.projects.Entity.Game;
import hibernate.projects.Entity.Player;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;

public class Main {

    private static EntityManagerFactory emFactory;

    public static EntityManagerFactory getEntityManagerFactory() {
        try {
            if (emFactory == null)
                emFactory = Persistence.createEntityManagerFactory("projectBang");

        } catch (PersistenceException e) {
            System.err.println("Error al construir l'EntityManagerFactory: " + e.getMessage());
            throw new ExceptionInInitializerError(e);
        }
        return emFactory;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        EntityManager em = null;
        try {
            em = getEntityManagerFactory().createEntityManager();
            EntityTransaction transaction = em.getTransaction();

            boolean playing = true;
            while (playing) {
                System.out.println("\n===== MENÚ PRINCIPAL =====");
                System.out.println("\t1 - Jugar");
                System.out.println("\t2 - Listar jugadores");
                System.out.println("\t3 - Añadir jugador");
                System.out.println("\t4 - Salir");
                System.out.println("==========================");
                System.out.print("\nElige una número: ");
                int option = in.nextInt();
                switch (option) {
                    case 1:
                        play(in, em, transaction);
                        break;

                    case 2:
                        showPlayers(em, transaction);

                    case 3:
                        addPlayer();
                        break;

                    case 4:
                        playing = false;
                        break;
                    default:
                        break;
                }

            }

            System.out.println("Closing...");

        } catch (Exception e) {
            System.out.println("Error durant l'execució del programa: " + e.getMessage());
        } finally {
            in.close();

            if (em != null && em.isOpen())
                em.close();

            if (emFactory != null && emFactory.isOpen())
                emFactory.close();
        }

    }

    private static void play(Scanner in, EntityManager em, EntityTransaction transaction) {
        transaction = em.getTransaction();
        transaction.begin();
        try {
            Game game = new Game();
            game.startDate = new Date();
            em.persist(game); // L'INSERT es fa amb PERSIST
            transaction.commit();
        } catch (PersistenceException e) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();

            System.err.println("Error durant la inserció de dades: " + e.getMessage());
        }
        boolean building = true;
        List<Player> players = new ArrayList<Player>();
        while (building) {
            System.out.println("\n===== MENÚ DE PREPARACIÓN =====");
            System.out.println("\t1 - Añadir jugador a la partida");
            System.out.println("\t2 - Quitar jugador de la partida");
            if (players.size() > 1)
                System.out.println("\t3 - Continuar");
            System.out.println("=================================");
            System.out.print("\nElige una número: ");
            int option = in.nextInt();
            switch (option) {
                case 1:
                    addPlayerToGame();
                    break;

                case 2:
                    removePlayerToGame();
                    break;

                case 3:
                    if (players.size() > 1)
                        building = false;
                    break;
                default:
                    break;
            }

        }
    }

    private static void showPlayers(EntityManager em, EntityTransaction transaction) {
        List<Player> players = new ArrayList<Player>();
        try {
            players = selectAll(em);

            for (Player player : players) {
                System.out.println(player.id + " - " + player.name);
            }

        } catch (PersistenceException e) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();

            System.err.println("Error durant la recuperació de dades: " + e.getMessage());

        }
    }

    private static List<Player> selectAll(EntityManager em) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        // CreateQuery permet fer consultes amb JPQL (similar a HQL)
        List<Player> players = em.createQuery("FROM player", Player.class).getResultList();
        transaction.commit();

        return players;
    }

    private static void addPlayer() {

    }

    private static void addPlayerToGame() {

    }

    private static void removePlayerToGame() {

    }
}