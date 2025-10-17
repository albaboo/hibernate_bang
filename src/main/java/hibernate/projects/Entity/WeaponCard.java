package hibernate.projects.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "weapon_card")
public class WeaponCard extends Card {

    @Column(name = "distance")
    private int distance;
}
