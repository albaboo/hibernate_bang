package hibernate.projects.Entity;

import java.util.Date;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "game")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @Column(name = "status", columnDefinition = "VARCHAR(255) DEFAULT 'Start'")
    public String status = "Start";

    @Column(name = "turn", nullable = false, columnDefinition = "INT DEFAULT 0")
    public int turn = 0;

    @Column(name = "start_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    public Date startDate = new Date();

    @Column(name = "active", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    public boolean active = true;

    /** Relaciones */

    @ManyToMany(mappedBy = "games")
    public Set<Player> players = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "game_playing_cards", joinColumns = @JoinColumn(name = "game_id"), inverseJoinColumns = @JoinColumn(name = "card_id"))
    public Deque<Card> playingCards;

    @ManyToMany
    @JoinTable(name = "game_discarded_cards", joinColumns = @JoinColumn(name = "game_id"), inverseJoinColumns = @JoinColumn(name = "card_id"))
    public Deque<Card> discardedCards;

}
