package hibernate.projects.Entity;

import hibernate.projects.Enum.TypeEquipment;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "equipment_card")
public class EquipmentCard extends Card {

    @Column(name = "type")
    private TypeEquipment type;

    @Column(name = "distance_modifier")
    private int distanceModifier;

    /** Relaciones */

    @ManyToOne
    @JoinColumn(name = "equipped_player_id")
    private Player equippedPlayer;
}
