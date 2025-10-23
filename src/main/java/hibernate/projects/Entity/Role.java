package hibernate.projects.Entity;

import java.util.List;
import java.util.Set;

import hibernate.projects.Enum.TypeRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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

    @ManyToMany
    @JoinTable(name = "player_game", joinColumns = @JoinColumn(name = "player_id"), inverseJoinColumns = @JoinColumn(name = "game_id"))
    private Set<Game> games;
}
