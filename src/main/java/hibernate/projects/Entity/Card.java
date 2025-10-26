package hibernate.projects.Entity;

import java.util.List;

import hibernate.projects.Enum.Suit;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "card")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @Column(name = "name")
    public String name;

    @Column(name = "description")
    public String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "suit")
    public Suit suit;

    /** Relaciones */

    @ManyToOne
    @JoinColumn(name = "player_id")
    public Player player;

    @ManyToMany(mappedBy = "playingCards")
    public List<Game> gamesPlaying;

    @ManyToMany(mappedBy = "discardedCards")
    public List<Game> gamesDiscarded;

}
