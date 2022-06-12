import DB.Flights;

import javax.persistence.*;
import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory managerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = managerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
//            Query amit = entityManager.createNativeQuery("select sum(miles) from FLIGHTS group by planeid ");
//           // TypedQuery<Flights> fly = entityManager.createNamedQuery("getFlights",Flights.class);
//            HashMap<String,Double> map = new HashMap<>();
//            //map.put(fly.getResultList().get(0).getFlightid(),fly.getResultList().get(0).getMiles());
//            //String id = fly.getResultList().get(0).getFlightid();
//            //System.out.println(id);
//            //System.out.println(map.get(id));
//
//            System.out.println(fly.getResultList().get(0).getStartTime().toLocalDate().getMonth().getValue());
//            int month = Integer.parseInt(Instant.now().toString().split("T")[0].split("-")[1]);
//            for (int i = 0; i < fly.getResultList().size(); i++) {
//                String id = fly.getResultList().get(i).getPlaneid();
//                double miles = fly.getResultList().get(i).getMiles();
//                if ((!map.containsKey(id))&&fly.getResultList().get(i).getStartTime().toLocalDate().getMonth().getValue()==month){
//                    map.put(id,miles);
//                }
//                else if (fly.getResultList().get(i).getStartTime().toLocalDate().getMonth().getValue()==month){
//                    miles = miles+map.get(id);
//                    map.put(id,miles);
//                }
//            }
//
//
//            //System.out.println(fly.getResultList().get(0).getStartTime().toLocalDate().getMonth().getValue());
//
//            //List<Flights> list = new LinkedList<Flights>(amit.getResultList());

        }finally {
            if (transaction.isActive()){
                transaction.rollback();
            }
            entityManager.close();
            managerFactory.close();

        }
    }
}
