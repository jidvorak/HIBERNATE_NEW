package entityes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Table(name = "players")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PlayerEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "player_name")
    private String name;

    @OneToOne
    @JoinColumn(name = "football_team_id")
    TeamEntity team;



}
