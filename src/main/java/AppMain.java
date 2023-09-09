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
        basicHql(session);




        boolean vseDopaloOK = true;
        if(vseDopaloOK)
            transaction.commit();
        else
            transaction.rollback();

        session.close();
    }

    // *****************************************************************

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
