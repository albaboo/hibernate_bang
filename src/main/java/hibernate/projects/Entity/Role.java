package hibernate.projects.Entity;

import java.util.List;

import hibernate.projects.Enum.TypeRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "type")
    private TypeRole type;

    @Column(name = "objective")
    private String objective;

    /** Relaciones */

    @OneToMany(mappedBy = "role")
    private List<Player> players;

}
