import entityes.*;
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
                .addAnnotatedClass(PlayerEntity.class)
                .addAnnotatedClass(TeamEntity.class)
                .buildSessionFactory();
        Session session = sessionFactory.openSession();
        return session;
    }
}
