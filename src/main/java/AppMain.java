import entityes.ActorEntity;
import entityes.DirectorEntity;
import entityes.MovieEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Set;

public class AppMain {

    public static void main(String[] args){

        // nova verze


        System.out.println("Hibernate test");

        Session session = DbConnect.getSession();   // getSession nám vyrobí Session- taedy naše spojení s DB
        Transaction transaction = session.beginTransaction();  // Spustím první transakci

        EntityManager entityManager = session;

  //*********ZDE PŘÍKLADY ČTENÍ A VYHLEDÁVÁNÍ V ENTITY*****************
        selects2Tables(session);
        selectsMtoMtables(session);
        basicHql(session);
  //*********ZDE UŽ PŘÍKLADY NA ÚPRAVU ÚDAJÚ V TABULKÁCH- ENTITÁCH*********
        updateSimple(session);
        // updateList(session);
        // addMovieAndItsActors(session);

        // doresit
        /*List<ActorEntity> olderActors = session.createQuery("from ActorEntity where movies.id=6").list(); // vyber hescu starcich 30 let
        olderActors.forEach(ac -> { //
            System.out.println("ac name=" + ac.getName() + " ac id=" + ac.getId());
        });*/

        /*
        ActorEntity ac = new ActorEntity();
        ac.setName("to delete");
        ac.setAge(44);
        ActorEntity acPersisted = (ActorEntity) session.merge(ac);
        //acPersisted.getId();*/


         /*                           Zde se většinou v praxi dává rozhodování, jestli všechny akce dopadly OK nebo NG
        boolean vseDopaloOK = true;   // Většinou se to dělá přes VYJÍMKY
        if(vseDopaloOK)
            transaction.commit();
        else
            transaction.rollback();
        */

        transaction.commit();      // UKONČENÍ PRVNÍ TRANSAKCE SE ZÁPISEM ZMĚN
      //transaction.rollback();      // UKONČENÍ PRVNÍ TRANSAKCE BEZ ZÁPISU ZMĚN

        session.close();  // PO UKONČENÍ PROGRAMU BY SE SPOJENÍ S DB automaticky zavřelo, ale pokud chci pokračovat v programu
                          // tak mohu libovolně otevřít session a taky zavřít
    }

    // *****************************************************************

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
        List<MovieEntity> movies = session.createQuery("from MovieEntity m").list(); // nacti list filmů- tj. těch MovieEntity
        movies.forEach(movieEntity -> { // smycka přes vsechny nactene entity
            movieEntity.setName(movieEntity.getName() + " - m"); // nastav entite film jmeno
            session.persist(movieEntity); // uloz do db
        });
    }
    // jednoducha zmena jmena dvou reziseru
    private static void updateSimple(Session session){
        DirectorEntity director = session.find(DirectorEntity.class, 1); // nacteni z db , DIRECTOR S ID=1
        DirectorEntity director2 = session.find(DirectorEntity.class, 2); // nacteni z db , DIRECTOR S ID=2

        director.setName("Miloš Forman"); // zmena jmena, ale zatím je to uloženo v cache paměti, ještě jsem nezapsal do DB
        director2.setName("Francis Ford Copola"); // zmena jmena

        session.persist(director); // uloží do DB entitu (zde director), které jsme změnili jméno
        session.persist(director2); // ulozeni do db

    }

    private static void basicHql(Session session){  // Příklad na použití HQL query

        // HQL https://www.javatpoint.com/hql

        // POZOR: Důležitá teorie ohledně HQL jazyka: v HQL query je název java entity (zde MovieEntity), a NE jméno
        // databázové tabulky

       //List<MovieEntity> movies = session.createQuery("from MovieEntity m").list(); // Takto by se pak ve forEach zobrazily všechny movies
                                                                                       // Ten list() to jen převede na list
        //List<MovieEntity> movies = session.createQuery("from MovieEntity m where m.id>3").list();
        List<MovieEntity> movies = session.createQuery("from MovieEntity m where m.id>3 ORDER by m.id DESC").list();
        // Je dobré tam použít tu anotaci (označení) m, tj. něco jako proměnnou, protože pak v dalších příkazech mohu použít
        // místo MovieEntity jen toto kratší označení.

        System.out.println("----------------------------------");
        movies.forEach(movieEntity -> {  // zde procházím kolekci movies, kterou jsem vytvořil pomocí HQL query,
                                         // movieEntity je proměnná pro jednotlivé prvky kolekce movies
            System.out.println("film=" + movieEntity.getName() + " id=" + movieEntity.getId());
            // A zde pro každý prvek movieEntity z kolekce movies zjistím, zda obsahuje aspoň jednoho herce,
            // a následně projdu tuto kolekci herců
            if(movieEntity.getActors().size()>0){  // Opět přes GETTER získám seznam herců pro danou Movie entitu
                movieEntity.getActors().forEach(acEntity -> {
                    System.out.println("       - actor=" + acEntity.getName());
                });
            }
        });
    }
    private static void selectsMtoMtables(Session session){
        // session má metodu find, která pracuje s tabulkama. První argument řekne v jaké tabulce chci najít řádek s id=1.
        // Druhý argument právě udává, jaké id  hledám. Metoda find vrací typ ActorEntity.
        // Zde chci zjistit a vypsat actor s id=1, a vypsat jeho filmy
        ActorEntity actor = session.find(ActorEntity.class, 1);
        System.out.println("----------------------------------");
        System.out.println("Actor-> ac name=" + actor.getName());
        // podmínka testuje, že velikost kolekce movies má aspoň 1 prvek
        if(actor.getMovies().size()>0){  // actor je objekt typu ActorEntity, ve kterém uložen výsledek vyhledání.
                                         // Tento objekt obsahuje GETTER getMovies pro vlastnost(field) movies (je typu Set<MovieEntity>)
            actor.getMovies().forEach(movieEntity -> {
                System.out.println("       - movie=" + movieEntity.getName());
            });
        }
        // Zde chci zjistit a vypsat movie s id=1, a vypsat herce kteří v něm hrají
        MovieEntity movie = session.find(MovieEntity.class, 1);
        System.out.println("Movie-> m name=" + movie.getName());

        if(movie.getActors().size()>0){
            movie.getActors().forEach(acEntity -> {
                System.out.println("       - actor=" + acEntity.getName());
            });
        }
    }


    private static void printMovieAndDirector(Session session, Integer idMovie) {
        // Je to v podstatě stejné jako metoda: selects2Tables
        MovieEntity movie = session.find(MovieEntity.class, idMovie); // idMovie je objekt, a tak lze použít jako 2. argument
        System.out.println("----------------------------------");
        System.out.println("movieID=" + movie.getId() + "; movieNAME=" + movie.getName() + " directorID=" + movie.getDirector().getId() + " ; directorNAME=" + movie.getDirector().getName());
        System.out.println("----------------------------------");
    }

    private static void selects2Tables(Session session){
        // session má metodu find, která pracuje s tabulkama. První argument řekne v jaké tabulce chci najít řádek s id=1.
        // Druhý argument právě udává, jaké id  hledám. Metoda find vrací typ MovieEntity.
        // Metoda find má deklaraci: public abstract <T> T find(Class<T> aClass, Object o ), tj. vrací objekt stejného typu
        // jako je typ třídy prvního argumentu
        MovieEntity movie = session.find(MovieEntity.class, 1);
        System.out.println("----selects2Tables------------------------------");
        // zde se už pak obracím na objekt movie, který je typu MovieEntity, a beru jeho GETTERY.
        // POZOR: U director musím takto: movie.getDirector().getName(), protože movie.getDirector() vrací DirectorEntity,
        // a na to zavolám ještě getName()
        System.out.println("FILM-> idfilmu=" + movie.getId() + ";  jmenofilmu=" + movie.getName() + "; directorname=" + movie.getDirector().getName());
        System.out.println("----------------------------------");

        // Zde chci zjistit a vypsat režiséra s id=2, a vypsat jeho filmy
        DirectorEntity director = session.find(DirectorEntity.class, 2);
        System.out.println("REJZA-> id=" + director.getId() + "; jmeno=" + director.getName());
        // První podmínka testuje, že režisér je přiřazen aspoň k nějakému filmu, a druhá podmínka testuje, že velikost
        // kolekce movies má aspoň 1 prvek
        // tj. getMovies je GETTER pro class field movies (je typu List<MovieEntity>) a je z class DirectorEntity
        if(director.getMovies()!=null && director.getMovies().size()>0){
            // projdu všechny prvky kolekce movies (použiju pro ně v Lambdě proměnnou movieEntity)
            // a vytisknu jméno každého prvku - tj. přesně jméno zadané ve sloupci Name naší tabulky MovieEntity,
            // tj. jméno filmu
            director.getMovies().forEach(movieEntity -> {
                System.out.println("           - film=" + movieEntity.getName() );
            });
        }
    }


}
