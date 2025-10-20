package hibernate.projects.Entity;

import java.util.Date;
import java.util.List;

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
  private int id;

  @Column(name = "status")
  private String status;

  @Column(name = "turn")
  private int turn;

  @Column(name = "start_date")
  private Date startDate;

  /** Relaciones */

  @ManyToMany(mappedBy = "games")
  private Set<Player> players;


    @ManyToMany
    @JoinTable(
        name = "game_playing_cards",
        joinColumns = @JoinColumn(name = "game_id"),
        inverseJoinColumns = @JoinColumn(name = "card_id")
    )
    private List<Card> playingCards;

    @ManyToMany
    @JoinTable(
        name = "game_discarded_cards",
        joinColumns = @JoinColumn(name = "game_id"),
        inverseJoinColumns = @JoinColumn(name = "card_id")
    )
    private List<Card> discardedCards;

}
