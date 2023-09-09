package entityes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="a_director")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DirectorEntity {

    @Id
    @Column(name="director_id")
    private Integer id;

    @Column(name="name")
    private String name;

}
