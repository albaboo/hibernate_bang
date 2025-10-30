package hibernate.projects;

import java.util.Date;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.Arrays;
import java.util.List;

import hibernate.projects.Entity.Game;
import hibernate.projects.Entity.Player;
import hibernate.projects.Entity.Role;
import hibernate.projects.Entity.WeaponCard;
import hibernate.projects.Entity.EquipmentCard;
import hibernate.projects.Entity.UseCard;
import hibernate.projects.Enum.TypeRole;
import hibernate.projects.Enum.Suit;
import hibernate.projects.Enum.TypeEquipment;
import hibernate.projects.Enum.TypeUse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;

public class Main {

    private static EntityManagerFactory emFactory;

    private static Game game;

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

    private static void checkRoles(EntityManager em, EntityTransaction transaction) {
        try {
            transaction = em.getTransaction();
            transaction.begin();

            for (TypeRole type : TypeRole.values()) {
                Long count = em.createQuery("SELECT COUNT(r) FROM Role r WHERE r.type = :type", Long.class)
                        .setParameter("type", type)
                        .getSingleResult();
                if (count == 0) {
                    Role role = new Role();
                    role.type = type;
                    role.objective = type.objective;
                    em.persist(role);
                }
            }
            em.flush();
            transaction.commit();
        } catch (PersistenceException e) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
            System.err.println("\u001B[31mError comprobando roles: " + e.getMessage() + "\u001B[0m");
        }
    }

    private static void checkCards(EntityManager em, EntityTransaction transaction) {
        try {
            transaction = em.getTransaction();
            transaction.begin();

            final int NUMBER_CARDS = 80;

            Long total = em.createQuery("SELECT COUNT(c) FROM Card c", Long.class).getSingleResult();
            Long existing = total != null ? total : 0;

            Suit[] suits = Suit.values();
            int suitIndex = 0;

            for (TypeEquipment type : TypeEquipment.values()) {
                Long totalEquipment = em
                        .createQuery("SELECT COUNT(e) FROM EquipmentCard e WHERE e.type = :type", Long.class)
                        .setParameter("type", type)
                        .getSingleResult();
                if (totalEquipment == 0) {
                    EquipmentCard equipmentCard = new EquipmentCard();
                    equipmentCard.name = type.name();
                    equipmentCard.description = type.description;
                    equipmentCard.type = type;
                    if (type == TypeEquipment.HORSE || type == TypeEquipment.TELESCOPIC_SIGHT)
                        equipmentCard.distanceModifier = 1;
                    else
                        equipmentCard.distanceModifier = 0;

                    equipmentCard.suit = suits[suitIndex % suits.length];
                    suitIndex++;
                    em.persist(equipmentCard);
                    existing++;
                }
            }

            if (existing < NUMBER_CARDS) {
                Long missing = NUMBER_CARDS - existing;
                List<TypeUse> uses = Arrays.asList(TypeUse.values());
                int useCount = uses.size();
                int created = 0;
                int index = 0;
                while (created < missing) {
                    TypeUse type = uses.get(index % useCount);
                    UseCard useCard = new UseCard();
                    useCard.name = type.name();
                    useCard.description = type.description;
                    useCard.type = type;
                    em.persist(useCard);

                    useCard.suit = suits[suitIndex % suits.length];
                    suitIndex++;
                    created++;
                    index++;
                }
            }

            em.flush();
            transaction.commit();
        } catch (PersistenceException e) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
            System.err.println("\u001B[31mError comprobando cartas: " + e.getMessage() + "\u001B[0m");
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        EntityManager em = null;
        try {
            em = getEntityManagerFactory().createEntityManager();
            EntityTransaction transaction = em.getTransaction();

            checkRoles(em, transaction);
            checkCards(em, transaction);

            boolean playing = true;
            while (playing) {
                System.out.println("\n========== MENÚ PRINCIPAL ==========");
                System.out.println("\t1 - Jugar");
                System.out.println("\t2 - Listar jugadores");
                System.out.println("\t3 - Añadir jugador");
                System.out.println("\t4 - Salir");
                System.out.println("====================================");
                System.out.print("\nElige una número: ");
                int option = in.nextInt();
                switch (option) {
                    case 1:
                        if (selectAll(em).size() > 1)
                            startGame(in, em, transaction);
                        else
                            System.err.println("\n\u001B[31mNo hay jugadores suficientes registrados\u001B[0m");
                        break;

                    case 2:
                        if (selectAll(em).size() > 0)
                            showPlayers(em);
                        else
                            System.err.println("\n\u001B[31mNo hay jugadores suficientes registrados\u001B[0m");
                        break;

                    case 3:
                        addPlayer(em, transaction, in);
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
            System.err.println("\n\u001B[31mError durant l'execució del programa: " + e.getMessage() + "\u001B[0m");
        } finally {
            in.close();

            if (em != null && em.isOpen())
                em.close();

            if (emFactory != null && emFactory.isOpen())
                emFactory.close();
        }

    }

    private static void startGame(Scanner in, EntityManager em, EntityTransaction transaction) {
        game = new Game();
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
                        addPlayerToGame(em, in, transaction);
                        break;

                    case 2:
                        removePlayerToGame(em, in, transaction);
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

            play(in, em, transaction);
        } catch (PersistenceException e) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();

            System.err.println("\n\u001B[31mError durant la inserció de dades:  " + e.getMessage() + "\u001B[0m");
        }
    }

    private static void play(Scanner in, EntityManager em, EntityTransaction transaction) {

    }

    private static void showPlayers(EntityManager em) {
        Set<Player> players = new HashSet<Player>();
        try {
            players = selectAll(em);
            System.out.println("\n==================== LISTA DE JUGADORES ====================");
            for (Player player : players) {
                System.out.println("\t" + player.id + " - " + player.name);
            }
            System.out.println("============================================================");
        } catch (PersistenceException e) {
            System.err.println("\n\u001B[31mError durant la recuperació de dades: " + e.getMessage() + "\u001B[0m");

        }
    }

    private static void showPlayersInGame(EntityManager em) {
        try {
            System.out.println("\n==================== LISTA DE JUGADORES ====================");
            for (Player player : game.players) {
                System.out.println("\t" + player.id + " - " + player.name);
            }
            System.out.println("============================================================");
        } catch (PersistenceException e) {
            System.err.println("\n\u001B[31mError durant la recuperació de dades: " + e.getMessage() + "\u001B[0m");

        }
    }

    private static Set<Player> selectAll(EntityManager em) {
        Set<Player> players = new HashSet<>(em.createQuery("FROM Player", Player.class).getResultList());

        return players;
    }

    private static void addPlayer(EntityManager em, EntityTransaction transaction, Scanner in) {
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

    private static void addPlayerToGame(EntityManager em, Scanner in, EntityTransaction transaction) {
        boolean selecting = true;
        while (selecting) {
            showPlayers(em);
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

    }

    private static void removePlayerToGame(EntityManager em, Scanner in, EntityTransaction transaction) {
        boolean selecting = true;
        while (selecting) {
            showPlayersInGame(em);
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
    }
}
