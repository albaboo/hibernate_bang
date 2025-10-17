package hibernate.projects.Entity;

import hibernate.projects.Enum.TypeEquipment;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class EquipmentCard extends Card {

    @Column(name = "type")
    private TypeEquipment type;

    @Column(name = "distance_modifier")
    private int distanceModifier;
}
