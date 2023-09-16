package entityes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "football_team")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TeamEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "team_name")
    private String name;

    @OneToMany
    @JoinColumn(name = "football_team_id")
    List<PlayerEntity> players;
}
