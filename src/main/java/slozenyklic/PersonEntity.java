package slozenyklic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="person")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonEntity {

    // slozeny primární klíč (lze řešit i anotací @IdClass)
    @EmbeddedId
    private PersonId id;

    @Column(name="name")
    private String name;

    // a dalsi sloupce

}
