package CriteriaApi;

import entityes.ActorEntity;
import org.hibernate.Session;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class CrApiTestClass {
    private Session session = null;

    public CrApiTestClass(Session aSession){
        session = aSession;
    }  // Hlavní třída si v konstruktoru převezme session

    public void runItWithWhere(String name, Integer ageVetsi){  // Tato metoda vybírá data z DB

        // skladani vyberovych podminek pomoci objektu a nikoli HQL retezcu

        // VYSVETLENÍ RŮZNÝCH VZTAHŮ MEZI API TŘÍDAMI A INTERFACE:
        // FIND - co se používá v klasickém HIBERNATE, je METODA v INTERFACE EntityManager (ta je v package javax.persistence),
        // ze kterého dědí naše INTERFACE Session
        // getCriteriaBuilder - platí to samé, co pro FIND. Taky tato metoda je v INTERFACE EntityManager, ze kterého dědí
        // naše Session interface
    // ------------------------------------------------------------------------------------------

        // Získání instance toho kde budeme tvořit kritéria. Metoda getCriteriaBuilder vrací objekt CriteriaBuilder.
        // Na tomto objektu pak mohu tvořit QUERY.
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        // Zde vytvoříme dotaz criteriaQuery. Zde je důležité určit odkud (z jakého typu Entity) budeme brát data- zde z ActorEntity.
        // metoda createQuery create a CriteriaQuery object with the specified result type - ActorEntity.class udává typ
        // výsledného objektu. Takto definováno: <T> CriteriaQuery<T> createQuery(Class<T> resultClass), kde
        // resultClass - je type of the query result.
        // CriteriaQuery už obsahuje metody SELECT, WHERE,...
        CriteriaQuery<ActorEntity> criteriaQuery = criteriaBuilder.createQuery(ActorEntity.class);
        // Nastavit root, protože ten SELECT může brát z více tabulek. Asi zde nastavím tu hlavní tab. Je to velmi komplexní.
        Root<ActorEntity> root = criteriaQuery.from(ActorEntity.class);

        List<Predicate> preList = new ArrayList<Predicate>(); // položky ve where

        if(name!=null)
            preList.add(criteriaBuilder.like(root.get("name"), name)); // pokud je nenulovy name dame do where
        if(ageVetsi!=null)
            preList.add(criteriaBuilder.greaterThan(root.get("age"), ageVetsi));  // pokud je nenulovy name dame do where

        // criteriaBuilder.equal() // to co je ve where = like...

        Predicate[] predicates = preList.toArray(new Predicate[0]);

        if(predicates.length>0)
            criteriaQuery.select(root).where(predicates);
        else
            criteriaQuery.select(root);

        Query query = session.createQuery(criteriaQuery);

        List<ActorEntity> actList = query.getResultList();

        actList.forEach(ac -> {
            System.out.println("name=" + ac.getName() + " age=" + ac.getAge());
        });
    }

}
