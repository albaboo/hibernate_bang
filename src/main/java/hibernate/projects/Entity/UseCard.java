package hibernate.projects.Entity;

import hibernate.projects.Enum.TypeUse;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "use_card")
public class UseCard extends Card {

    @Column(name = "type")
    public TypeUse type;

    /** Relaciones */

}
