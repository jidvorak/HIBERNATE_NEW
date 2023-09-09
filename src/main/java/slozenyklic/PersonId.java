package slozenyklic;

import javax.persistence.Column;
import javax.persistence.Embeddable;

// definice slozeneho primárního klíče
@Embeddable
public class PersonId {

    @Column(name = "id_firmy")
    private int idFirmy;

    @Column(name = "id_oddeleni")
    private int idOddeeleni;

}
