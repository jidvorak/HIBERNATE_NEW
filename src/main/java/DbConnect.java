import entityes.ActorEntity;
import entityes.DirectorEntity;
import entityes.MovieEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DbConnect {

    public static Session getSession() {       // vrací objekt typu Session - tj. to vlastní napojení našeho PC na DB
  // Configuration is class, which consists of the properties and function files of Hibernate. The configuration object is
  // created only once during the application initialization. Configuration patří do org.hibernate.cfg.Configuration package.
  // Klasicky by se objekt vytvořil takto: Configuration cfg=new Configuration() a pak bych přečetl both mapping and configuration file
  // pomocí: cfg.configure()
  // SessionFactory interface je objekt , který lze získat pomocí objektu of Configuration class. It is a threadsafe
  // object and used by all the threads in the application.
  // Takto vytvořím SessionFactory objekt: SessionFactory factory=cfg.buildSessionFactory();
        SessionFactory sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(MovieEntity.class)
                .addAnnotatedClass(DirectorEntity.class)
                .addAnnotatedClass(ActorEntity.class)
                .buildSessionFactory();                // až tímto vytvořím SessionFactory objekt
  // A SessionFactory object is used to create a Session object, which is lightweight object. The Session object is not
  // threadsafe. It is used to execute CRUD operations (insert, delete, update, edit). It also holds the first-level
  // cache data in Hibernate.
  // Takto vytvořím SESSION objekt: Session session=factory.buildSession()
        Session session = sessionFactory.openSession();
        return session;
    }
}
