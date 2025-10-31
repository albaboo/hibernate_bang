package hibernate.projects.Controller;

import java.util.Arrays;
import java.util.List;

import hibernate.projects.Entity.EquipmentCard;
import hibernate.projects.Entity.UseCard;
import hibernate.projects.Enum.Suit;
import hibernate.projects.Enum.TypeEquipment;
import hibernate.projects.Enum.TypeUse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;

public class CardDAO {

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
