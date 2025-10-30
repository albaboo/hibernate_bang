package hibernate.projects.Controller;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import hibernate.projects.Entity.Game;
import hibernate.projects.Entity.Player;
import hibernate.projects.Entity.Role;
import hibernate.projects.Entity.WeaponCard;
import hibernate.projects.Enum.Suit;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;

public class GameDAO {

    public static Set<Player> listPlayers(EntityManager em, int idGame) {

        return null;
    }

    public static void showPlayers(EntityManager em, int idGame) {
        Set<Player> players = new HashSet<Player>();
        try {
            players = listPlayers(em, idGame);
            System.out.println("\n==================== LISTA DE JUGADORES ====================");
            for (Player player : players) {
                System.out.println("\t" + player.id + " - " + player.name);
            }
            System.out.println("============================================================");
        } catch (PersistenceException e) {
            System.err.println("\n\u001B[31mError durant la recuperació de dades: " + e.getMessage() + "\u001B[0m");

        }
    }

    public static void showGame(int idGame) {
        /**
         * Show:
         * Lives players & lives
         * EquipmentCard of each player
         * Roles found (Sheriff always)
         */
    }

    public static Game startGame(Scanner in, EntityManager em, EntityTransaction transaction) {
        Game game = new Game();
        game.players = new HashSet<>();
        boolean building = true;

        try {
            transaction = em.getTransaction();
            transaction.begin();

            game.startDate = new Date();
            em.persist(game);
            em.flush();

            while (building) {
                System.out.println("\n==================== MENÚ DE PREPARACIÓN ====================");
                System.out.println("\t1 - Añadir jugador a la partida");
                if (game.players.size() > 0)
                    System.out.println("\t2 - Quitar jugador de la partida");
                if (game.players.size() > 1)
                    System.out.println("\t3 - Continuar");
                System.out.println("=============================================================");
                System.out.print("\nElige una número: ");
                int option = in.nextInt();
                switch (option) {
                    case 1:
                        addPlayer(in, em, transaction, game.id);
                        break;

                    case 2:
                        removePlayer(in, em, transaction, game.id);
                        break;

                    case 3:
                        if (game.players.size() > 1)
                            building = false;
                        break;
                    default:
                        break;
                }
            }
            transaction.commit();

            List<Role> roles = em.createQuery("FROM Role", Role.class).getResultList();
            int roleIndex = 0;

            Suit[] suits = Suit.values();
            int suitIndex = 0;

            transaction = em.getTransaction();
            transaction.begin();

            for (Player player : game.players) {
                player.role = roles.get(roleIndex % roles.size());
                WeaponCard colt = new WeaponCard();
                colt.name = "Colt";
                colt.description = "Arma predeterminada";
                colt.distance = 1;
                colt.suit = suits[suitIndex % suits.length];
                colt.player = player;
                player.weapon = colt;
                suitIndex++;
                em.persist(colt);
                em.persist(player);
                roleIndex++;
            }

            em.flush();

            transaction.commit();

            // play(in, em, transaction);
        } catch (PersistenceException e) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();

            System.err.println("\n\u001B[31mError durant la inserció de dades:  " + e.getMessage() + "\u001B[0m");
        }
        return game;
    }

    private static Game addPlayer(Scanner in, EntityManager em, EntityTransaction transaction, int idGame) {
        boolean selecting = true;
        Game game = new Game();
        while (selecting) {
            showPlayers(em, idGame);
            System.out.println("\n\t0 - Volver atras");
            System.out.print("\nSelecciona un número de jugador: ");
            int option = in.nextInt();
            if (option == 0)
                selecting = false;
            else {
                Player selectedPlayer = em.find(Player.class, option);
                if (selectedPlayer == null)
                    System.err.println("\n\u001B[31mJugador no encontrado.\u001B[0m");
                else if (game.players.contains(selectedPlayer))
                    System.err.println("\n\u001B[31mJugador ya incluido en la partida.\u001B[0m");
                else {
                    selectedPlayer.games.add(game);
                    game.players.add(selectedPlayer);

                    System.out.println("Añadido: " + selectedPlayer.name);
                    selecting = false;
                }

            }

        }
        return game;

    }

    private static Game removePlayer(Scanner in, EntityManager em, EntityTransaction transaction, int idGame) {
        Game game = new Game();
        boolean selecting = true;
        while (selecting) {
            showPlayers(em, idGame);
            System.out.println("\n\t0 - Volver atras");
            System.out.print("\nSelecciona un número de jugador: ");
            int option = in.nextInt();
            if (option == 0)
                selecting = false;
            else {
                Player selectedPlayer = em.find(Player.class, option);
                if (selectedPlayer == null)
                    System.err.println("\n\u001B[31mJugador no encontrado.\u001B[0m");
                else if (!game.players.contains(selectedPlayer))
                    System.err.println("\n\u001B[31mJugador no incluido en la partida.\u001B[0m");
                else {
                    selectedPlayer.games.remove(game);
                    game.players.remove(selectedPlayer);

                    System.out.println("Eliminado: " + selectedPlayer.name);
                    selecting = false;
                }

            }

        }

        return game;
    }

    public static boolean checkVictory(int idGame) {

        return false;
    }

    public static Suit showCard(int idGame) {
        /**
         * BLABLABLA
         */

        return null;
    }

}
