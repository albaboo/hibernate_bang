package hibernate.projects.Entity;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
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

  @Column(name = "players")
  private List<Player> players;

  @Column(name = "playing_cards")
  private List<Card> playingCards;

  @Column(name = "discared_cards")
  private List<Card> discaredCards;

}
