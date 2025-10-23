package hibernate.projects.Entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "player")
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

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "weapon_id", referencedColumnName = "id")
    private WeaponCard weapon;

    @OneToMany(mappedBy = "equippedPlayer", cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = EquipmentCard.class)
    private List<EquipmentCard> equipments;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Card> hand;

}
