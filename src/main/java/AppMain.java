import CriteriaApi.CrApiTestClass;
import entityes.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.EntityManager;
import java.util.List;

public class AppMain {

    public static void main(String[] args){

        // nova verze


        System.out.println("Hibernate test");

        Session session = DbConnect.getSession();
        Transaction transaction = session.beginTransaction();

        EntityManager entityManager = session;

         //selects2Tables(session);
        // selectsMtoMtables(session);
        // basicHql(session);
        // updateSimple(session);
        // updateList(session);
        // addMovieAndItsActors(session);

        // ******************************************************************

        // NEW
//        selectJoinAndDelete(session);

        // pridani
        //addMovieAndItsActors(session);

        //CrApiTestClass crapi = new CrApiTestClass(session);
        //crapi.runItWithWhere("%J%", 28);

        // ******************************************************************

        /*
        ActorEntity ac = new ActorEntity();
        ac.setName("to delete");
        ac.setAge(44);
        ActorEntity acPersisted = (ActorEntity) session.merge(ac);
        //acPersisted.getId();*/


        // merge ulozi a vraci ulozeny objekt
        // persist pouze ulozi
        TeamEntity arsenal = new TeamEntity();
        arsenal.setName("Arsenal");
        TeamEntity savedArsenal = (TeamEntity) session.merge(arsenal);

        PlayerEntity saka = new PlayerEntity();
        saka.setName("Saka");
        saka.setTeam(savedArsenal);
        session.persist(saka);

        PlayerEntity martineli = new PlayerEntity();
        martineli.setName("Martineli");
        martineli.setTeam(savedArsenal);
        session.persist(martineli);


        List<PlayerEntity> players = session.createQuery("From PlayerEntity").list();
        players.forEach(playerEntity -> {
            System.out.println(playerEntity.getName());
            playerEntity.setName("random");
//            session.remove(playerEntity);
            session.persist(playerEntity);
        });



        transaction.commit();

        /*
        boolean vseDopaloOK = true;
        if(vseDopaloOK)
            transaction.commit();
        else
            transaction.rollback();
        */
        session.close();
    }

    // *****************************************************************

    private static void selectJoinAndDelete(Session session){

        // select JOIN
        String selectstring = "FROM ActorEntity actorAlias " + // select z tabulka actor (název se bere z entity)
                "JOIN actorAlias.movies movieAlias " + // Join movies je vlastnost entity ActorEntity
                "WHERE movieAlias.id = 6"; // id filmu

        // když pouřijeme výše popsaný JOIN, hibernate vrací pole objektů kde je herec i film entity
        List<?> actorsAndMovie = session.createQuery(selectstring).list(); // 5 záznamů typu (Object[]) v nwm je objec

        // smycka prez vracene zaznamy
        for(int cisloradku=0; cisloradku<actorsAndMovie.size();cisloradku++){


            Object[] poleDataRadku = (Object[]) actorsAndMovie.get(cisloradku); // načteme pole z řádku číslo i

            ActorEntity actor = (ActorEntity)poleDataRadku[0]; // nacteni herce z pole (z radku)
            MovieEntity movie = (MovieEntity)poleDataRadku[1]; // nacteni filmu z pole (radku)

            System.out.println("ac name=" + actor.getName() + " mo name=" + movie.getName());
            //session.remove(a);
        }

    }


    // nejjednoduzsi mazani
    private static void simpleDelete(Session session){
        // vyber entity herec z db s id 10
        ActorEntity ac = session.find(ActorEntity.class, 10);
        if(ac!=null) // pokud existuje v db
            session.remove(ac); // mazeme zazbnam dle entity
    }

    // Přidání filmu a svázání s herci vazbou m..n
    // doporucujií modifikovat entitu (pridavat do ni) která má anotaci @JoinTable
    private static void addMovieAndItsActors(Session session){
        MovieEntity newMovie = insertNewMovie(session, "Film - Herci nad 30", 1); // funkce vytvozi v db novy film
        System.out.println("ID NOVEHO FILMU=" + newMovie.getId());
        List<ActorEntity> olderActors = session.createQuery("from ActorEntity where age>30").list(); // vyber hescu starcich 30 let
        olderActors.forEach(actorEntity -> { // loop prez vybrane herce
            actorEntity.getMovies().add(newMovie); // kazdemu herci přidame vytvořeny film
            session.persist(actorEntity); // ulozime herce do db
        });

        // vytvorime noveho uzivatele a pridame mu take novy film
        ActorEntity newActor = new ActorEntity();
        newActor.setName("Novy herec pod 30 let");
        newActor.setAge(22);
        newActor.getMovies().add(newMovie);
        session.persist(newActor);
        /*
        // test select
        USE mydb;
        SELECT m.id_movie, m.name, a.id_actor, a.name, a.age
        FROM mydb.a_actor a, mydb.a_movie m, a_movie_actor ma
        where m.id_movie = ma.id_movie and ma.id_actor = a.id_actor and m.id_movie=6;
        * */
    }

    // zalozime novy film
    private static MovieEntity insertNewMovie(Session session, String moviename, Integer dirId){
        DirectorEntity directorEntity = session.find(DirectorEntity.class, dirId); // nacteni rezizera d DB
        MovieEntity movieEntity = new MovieEntity(); // vytvoreni entity  film
        movieEntity.setName(moviename); // nastaveni jmena filmu
        movieEntity.setDirector(directorEntity); // nastaveni rezizera pro film
        MovieEntity savedMovie = (MovieEntity) session.merge(movieEntity); // ulozeni do db a vraceni ulozeneho filmu
        // metoda persisit take uklada jako merge, ale ta nevraci nove ulozenou entitu
        return savedMovie; // vratime ulozene id filmu
    }

    // zmena rezisera filmu
    private static void updateMovieDirector(Session session, Integer movieId, Integer dirId){
        MovieEntity movie = session.find(MovieEntity.class, movieId); // nacteni filmu z db
        DirectorEntity director = session.find(DirectorEntity.class, dirId); // nasteni rezisera z db
        movie.setDirector(director); // nastaveni rezisera pro film
        session.persist(movie); // ulozeni filmu do db
    }

    // projde vice zaznamu a vsechny updatuje
    private static void updateList(Session session){
        List<MovieEntity> movies = session.createQuery("from MovieEntity m").list(); // ncti list filmu
        movies.forEach(movieEntity -> { // smycka prez vsechny nactene entity
            movieEntity.setName(movieEntity.getName() + " - m"); // nastav entite film jmeno
            session.persist(movieEntity); // uloz do db
        });
    }
    // jednoducha zmena jmena dvou reziseru
    private static void updateSimple(Session session){
        DirectorEntity director = session.find(DirectorEntity.class, 1); // nacteni z db
        DirectorEntity director2 = session.find(DirectorEntity.class, 2); // nacteni z db

        director.setName("Miloš Forman"); // zmena jmena
        director2.setName("Francis Ford Copola"); // zmena jmena

        session.persist(director); // ulozeni do db
        session.persist(director2); // ulozeni do db

    }

    private static void basicHql(Session session){

        // HQL https://www.javatpoint.com/hql

        // v HQL query je název java entity (MovieEntity)

        //List<MovieEntity> movies = session.createQuery("from MovieEntity m").list();
        //List<MovieEntity> movies = session.createQuery("from MovieEntity m where m.id>3").list();
        List<MovieEntity> movies = session.createQuery("from MovieEntity m where m.id>3 ORDER by m.id DESC").list();

        movies.forEach(movieEntity -> {
            System.out.println("film=" + movieEntity.getName() + " id=" + movieEntity.getId());
            if(movieEntity.getActors().size()>0){
                movieEntity.getActors().forEach(acEntity -> {
                    System.out.println("       - actor=" + acEntity.getName());
                });
            }
        });
    }
    private static void selectsMtoMtables(Session session){
        ActorEntity actor = session.find(ActorEntity.class, 1);
        System.out.println("Actor-> ac name=" + actor.getName());

        if(actor.getMovies().size()>0){
            actor.getMovies().forEach(movieEntity -> {
                System.out.println("       - movie=" + movieEntity.getName());
            });
        }

        MovieEntity movie = session.find(MovieEntity.class, 1);
        System.out.println("Movie-> m name=" + movie.getName());

        if(movie.getActors().size()>0){
            movie.getActors().forEach(acEntity -> {
                System.out.println("       - actor=" + acEntity.getName());
            });
        }
    }


    private static void printMovieAndDirector(Session session, Integer idMovie) {
        MovieEntity movie = session.find(MovieEntity.class, idMovie);
        System.out.println("----------------------------------");
        System.out.println("movieID=" + movie.getId() + "; movieNAME=" + movie.getName() + " directorID=" + movie.getDirector().getId() + " ; directorNAME=" + movie.getDirector().getName());
        System.out.println("----------------------------------");
    }

    private static void selects2Tables(Session session){

        MovieEntity movie = session.find(MovieEntity.class, 1);

        System.out.println("----------------------------------");
        System.out.println("FILM-> idfilmu=" + movie.getId() + ";  jmenofilmu=" + movie.getName() + "; directorname=" + movie.getDirector().getName());
        System.out.println("----------------------------------");


        DirectorEntity director = session.find(DirectorEntity.class, 2);
        System.out.println("REJZA-> id=" + director.getId() + "; jmeno=" + director.getName());

        if(director.getMovies()!=null && director.getMovies().size()>0){
            director.getMovies().forEach(movieEntity -> {
                System.out.println("           - film=" + movieEntity.getName() );
            });
        }
    }


}
