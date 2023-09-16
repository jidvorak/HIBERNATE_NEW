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
    }

    public void runItWithWhere(String name, Integer age){
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<ActorEntity> criteriaQuery = criteriaBuilder.createQuery(ActorEntity.class);
        Root<ActorEntity> root = criteriaQuery.from(ActorEntity.class);
        List<Predicate> preList = new ArrayList<Predicate>();
        if(name!=null)
            preList.add(criteriaBuilder.like(root.get("name"), name));
        if(age!=null)
            preList.add(criteriaBuilder.greaterThan(root.get("age"), age));

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
