import entityes.ActorEntity;
import entityes.DirectorEntity;
import entityes.MovieEntity;
import entityes.TestEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DbConnect {

    public static Session getSession() {
        SessionFactory sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(MovieEntity.class)
                .addAnnotatedClass(DirectorEntity.class)
                .addAnnotatedClass(ActorEntity.class)
                .addAnnotatedClass(TestEntity.class)
                .buildSessionFactory();
        Session session = sessionFactory.openSession();
        return session;
    }
}
