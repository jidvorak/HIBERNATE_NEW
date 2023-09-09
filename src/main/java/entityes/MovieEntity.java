package entityes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="a_movie")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovieEntity {

    @Id
    @Column(name="id_movie")
    private Integer id;

    @Column(name="name")
    private String name;

    @OneToOne
    @JoinColumn(name="director_id")
    DirectorEntity director;

}
