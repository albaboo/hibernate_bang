package hibernate.projects.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "weapon")
public class WeaponCard extends Card {

    @Column(name = "distance")
    private int distance;

    /** Relacions */

    @OneToOne(mappedBy = "weapon")
    private Player player;
}
