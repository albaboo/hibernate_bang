package hibernate.projects.Entity;

import java.util.List;

import hibernate.projects.Enum.Suit;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "card")
public abstract class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "suit")
    private Suit suit;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;
    

    @ManyToMany(mappedBy = "playing_cards")
    private List<Game> gamesAsPlaying;

    @ManyToMany(mappedBy = "discarded_cards")
    private List<Game> gamesAsDiscarded;
}
