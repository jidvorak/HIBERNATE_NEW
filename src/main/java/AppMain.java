import entityes.DirectorEntity;
import entityes.MovieEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class AppMain {

    public static void main(String[] args){

        System.out.println("Hibernate test");

        Session session = DbConnect.getSession();
        Transaction transaction = session.beginTransaction();

        MovieEntity movie = session.find(MovieEntity.class, 1);
        System.out.println("FILM-> " + movie.getId() + " - " + movie.getName() + " - " + movie.getDirectorId());

        DirectorEntity director = session.find(DirectorEntity.class, 2);
        System.out.println("REJZA-> " + director.getId() + " - " + director.getName());

        boolean vseDopaloOK = true;
        if(vseDopaloOK)
            transaction.commit();
        else
            transaction.rollback();

        session.close();
    }
}
