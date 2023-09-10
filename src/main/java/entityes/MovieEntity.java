package entityes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="a_movie")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovieEntity {

    @Id
    @Column(name="id_movie")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="name")
    private String name;

    @OneToOne
    @JoinColumn(name="director_id")
    DirectorEntity director;

    @ManyToMany(mappedBy = "movies") // nazev vlastnosti z ActorEntity (Set<MovieEntity> movies = new HashSet<>())
    private Set<ActorEntity> actors = new HashSet<>();

}
