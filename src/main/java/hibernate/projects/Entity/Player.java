package hibernate.projects.Entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "current_life")
    private int currentLife;

    @Column(name = "max_life")
    private int maxLife;

    @Column(name = "def_distance_modifier")
    private int defDistanceModifier;

    @Column(name = "off_distance_modifier")
    private int offDistanceModifier;

    /** Relaciones */

    @Column(name = "role")
    private Role role;

    @Column(name = "weapon")
    private WeaponCard weapon;

    @Column(name = "equipments")
    private List<EquipmentCard> equipments;

    @Column(name = "cards_use")
    private List<UseCard> uses;

}
