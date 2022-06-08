import DB.Fly;
import DB.Plane;
import DB.Time;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory managerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = managerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Time fly = new Time();
            fly.setId("123");
            entityManager.persist(fly);
            transaction.commit();

        }finally {
            if (transaction.isActive()){
                transaction.rollback();
            }
            entityManager.close();
            managerFactory.close();

        }
    }
}
