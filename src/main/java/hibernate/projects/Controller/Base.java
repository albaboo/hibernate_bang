package hibernate.projects.Controller;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import hibernate.projects.Entity.EquipmentCard;
import hibernate.projects.Entity.Player;
import hibernate.projects.Entity.Role;
import hibernate.projects.Entity.UseCard;
import hibernate.projects.Enum.Suit;
import hibernate.projects.Enum.TypeEquipment;
import hibernate.projects.Enum.TypeRole;
import hibernate.projects.Enum.TypeUse;

public class Base {

    public static Set<Player> listPlayers(EntityManager em) {

        Set<Player> players = new HashSet<>(em.createQuery("FROM Player", Player.class).getResultList());
        return players;
    }

    public static void showPlayers(EntityManager em) {
        Set<Player> players = new HashSet<Player>();
        try {
            players = listPlayers(em);
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

    public static void checkRoles(EntityManager em, EntityTransaction transaction) {
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

    public static void checkCards(EntityManager em, EntityTransaction transaction) {
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

}
