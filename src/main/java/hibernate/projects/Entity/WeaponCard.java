package hibernate.projects.Entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "weapon_card")
public class WeaponCard extends Card {

    @Column(name = "distance")
    private int distance;

    /** Relacions */

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="player_id")
    private WeaponCard weapon;
}
