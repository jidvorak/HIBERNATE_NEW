import entityes.DirectorEntity;
import entityes.MovieEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class AppMain {

    public static void main(String[] args){

        System.out.println("Hibernate test");

        Session session = DbConnect.getSession();
        Transaction transaction = session.beginTransaction();

        selects2Tables(session);


        boolean vseDopaloOK = true;
        if(vseDopaloOK)
            transaction.commit();
        else
            transaction.rollback();

        session.close();
    }

    // *****************************************************************

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
