package hibernate.projects.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class WeaponCard extends Card {

    @Column(name = "distance")
    private int distance;
}
