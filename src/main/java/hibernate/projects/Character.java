package hibernate.projects;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;


@Entity
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "role")
    private Role role;

    @Column(name = "weapon")
    private String weapon;

    @Column(name = "equipments")
    private List<String> equipments;

    @Column(name = "cards_use")
    private List<String> cardsUse;

}
