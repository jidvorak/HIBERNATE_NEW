import entityes.ActorEntity;
import entityes.DirectorEntity;
import entityes.MovieEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class AppMain {

    public static void main(String[] args){

        System.out.println("Hibernate test");

        Session session = DbConnect.getSession();
        Transaction transaction = session.beginTransaction();

        // selects2Tables(session);
        // selectsMtoMtables(session);
        // basicHql(session);
        // updateSimple(session);
        // updateList(session);

        Integer newMovieId = insertNewMovie(session, "Duna", 1);
        System.out.println("MOVIE ID = " + newMovieId);

        ActorEntity actor = session.find(ActorEntity.class, 4);
        actor.setAge(75);
        session.persist(actor);

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

    // zalozime novy film
    private static Integer insertNewMovie(Session session, String moviename, Integer dirId){
        DirectorEntity directorEntity = session.find(DirectorEntity.class, dirId); // nacteni rezizera d DB
        MovieEntity movieEntity = new MovieEntity(); // vytvoreni entity  film
        movieEntity.setName(moviename); // nastaveni jmena filmu
        movieEntity.setDirector(directorEntity); // nastaveni rezizera pro film
        MovieEntity savedMovie = (MovieEntity) session.merge(movieEntity); // ulozeni do db a vraceni ulozeneho filmu
        // metoda persisit take uklada jako merge, ale ta nevraci nove ulozenou entitu
        return savedMovie.getId(); // vratime ulozene id filmu
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
