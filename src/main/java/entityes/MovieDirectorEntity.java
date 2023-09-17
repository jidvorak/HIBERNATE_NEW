package entityes;

import javax.persistence.*;

// entita reprezentuje přístup ke dvoum tabulkám - dodělat obsluhu.
@Entity
@Table(name="a_movie")
@SecondaryTable(name="a_director")
public class MovieDirectorEntity {

    @Id
    @Column(name="id_movie")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="name")
    private String name;

    @Column(name="name", table = "a_director")
    private String dirctorname;

}

/*
SELECT m.name film, d.name rejza
FROM mydb.a_movie m, mydb.a_director d
WHERE m.director_id=d.director_id;

*/
