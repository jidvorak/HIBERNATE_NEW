package entityes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="a_director")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DirectorEntity {

    @Id
    @Column(name="director_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="name")
    private String name;

    @OneToMany
    @JoinColumn(name="director_id")
    List<MovieEntity> movies;

}
