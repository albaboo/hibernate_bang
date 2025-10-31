package hibernate.projects.Controller;

import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import hibernate.projects.Entity.Card;
import hibernate.projects.Entity.Player;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;

public class PlayerDAO {

    public static Set<Player> listPlayers(EntityManager em) {

        Set<Player> players = new HashSet<>(em.createQuery("FROM Player", Player.class).getResultList());

        return players;
    }

    public static void showPlayers(EntityManager em) {
        Set<Player> players = new HashSet<Player>();
        try {
            players = PlayerDAO.listPlayers(em);
            System.out.println("\n==================== LISTA DE JUGADORES ====================");
            for (Player player : players) {
                System.out.println("\t" + player.id + " - " + player.name);
            }
            System.out.println("============================================================");
        } catch (PersistenceException e) {
            System.err.println("\n\u001B[31mError durant la recuperació de dades: " + e.getMessage() + "\u001B[0m");

        }
    }

    public static void addPlayer(EntityManager em, EntityTransaction transaction, Scanner in) {
        boolean creating = true;
        in.nextLine();
        while (creating) {
            System.out.print("\nEscribe un nombre de jugador: ");
            String name = in.nextLine();
            if (name.length() > 0) {
                Player newPlayer = new Player();
                newPlayer.name = name;
                try {
                    transaction = em.getTransaction();
                    transaction.begin();
                    em.persist(newPlayer);
                    transaction.commit();
                    System.out.println("Añadido: " + newPlayer.name);
                    creating = false;
                } catch (PersistenceException e) {
                    if (transaction != null && transaction.isActive())
                        transaction.rollback();

                    System.err
                            .println("\n\u001B[31mError durant la inserció de dades:  " + e.getMessage() + "\u001B[0m");
                }
            }

        }

    }

    public List<Card> showHand(int idPlayer) {

        return null;
    }

    public void useBang(int idAttacker, int idObjective) {

    }

    public void discardCard(int idPlayer, int idCard) {

    }

    public void checkElimination(int idPlayer) {

    }

    public void stealCard(int idPlayer) {

    }

    public void passTurn(int idPlayer) {

    }

    public void equipCard(int idPlayer, int idCard) {

    }

    public int calculateDistance(int idPlayerOrigin, int idPlayerDestination) {

        return idPlayerOrigin - idPlayerDestination;
    }

    public boolean checkDistanceAttack(int idAttacker, int idObjective) {

        return false;
    }

}
