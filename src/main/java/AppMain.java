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

        //selects2Tables(session);
        //selectsMtoMtables(session);
        //basicHql(session);
        //updateSimple(session);
        //updateList(session)

        Integer newMovieId = insertNewMovie(session, "Duna", 1);
        System.out.println("MOVIE ID = " + newMovieId);

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

    private static Integer insertNewMovie(Session session, String moviename, Integer dirId){
        DirectorEntity directorEntity = session.find(DirectorEntity.class, dirId);
        MovieEntity movieEntity = new MovieEntity();
        movieEntity.setName(moviename);
        movieEntity.setDirector(directorEntity);
        MovieEntity savedMovie = (MovieEntity) session.merge(movieEntity);
        return savedMovie.getId();
    }

    private static void updateMovieDirector(Session session, Integer movieId, Integer dirId){
        MovieEntity movie = session.find(MovieEntity.class, movieId);
        DirectorEntity director = session.find(DirectorEntity.class, dirId);
        movie.setDirector(director);
        session.persist(movie);
    }

    private static void updateList(Session session){
        List<MovieEntity> movies = session.createQuery("from MovieEntity m").list();
        movies.forEach(movieEntity -> {
            movieEntity.setName(movieEntity.getName() + " - m");
            session.persist(movieEntity);
        });
    }
    private static void updateSimple(Session session){

        DirectorEntity director = session.find(DirectorEntity.class, 1);
        DirectorEntity director2 = session.find(DirectorEntity.class, 2);

        director.setName("Miloš Forman");
        director2.setName("Francis Ford Copola");

        session.persist(director);
        session.persist(director2);

        System.out.println("----------ulozeno---------");
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
