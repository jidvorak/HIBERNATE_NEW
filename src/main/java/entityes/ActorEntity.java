package entityes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="a_actor")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActorEntity {

    @Id
    @Column(name="id_actor")
    private Integer id;

    @Column(name="name")
    private String name;

    // https://www.geeksforgeeks.org/hibernate-different-cascade-types/
    @ManyToMany(cascade = { CascadeType.DETACH })
    // https://www.baeldung.com/hibernate-many-to-many
    @JoinTable(
            name = "a_movie_actor",
            joinColumns = { @JoinColumn(name = "id_actor") },
            inverseJoinColumns = { @JoinColumn(name = "id_movie") })
    Set<MovieEntity> movies = new HashSet<>();

}
