package model;
import DB.Flights;
import DB.History;
import DB.Plane;
import necessary_classes.FlightData;
import necessary_classes.TimeSeries;
import javax.persistence.*;
import java.io.*;
import java.time.Instant;
import java.util.*;

public class dbhandler  implements dbhandler_api  {
    EntityManagerFactory managerFactory ;
    EntityManager entityManager ;
    EntityTransaction transaction ;
    TypedQuery<Flights> fly;
    TypedQuery<History> history;
    TypedQuery<Plane> plane;

    public dbhandler() {
         managerFactory = Persistence.createEntityManagerFactory("default");
         entityManager = managerFactory.createEntityManager();
         transaction = entityManager.getTransaction();
         fly =  entityManager.createNamedQuery("getFlights", Flights.class);
         history = entityManager.createNamedQuery("getHistory",History.class);
         plane = entityManager.createNamedQuery("getPlane",Plane.class);
    }
    //encoding
    private Object bytesToObject (byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }
    //decoding
    private byte[] objectToBytes(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }

    @Override //get active planes via agnet
    public int getActivePlanes() {
        return 0;
    }

    @Override //returns avg miles per month of all planes
    public HashMap<String, Double> getMilesPerMonth(int month) {
            HashMap<String, Double> map = new HashMap<>();
            for (int i = 0; i < fly.getResultList().size(); i++) {
                String id = fly.getResultList().get(i).getPlaneid();
                double miles = fly.getResultList().get(i).getMiles();
                if ((!map.containsKey(id)) && fly.getResultList().get(i).getStartTime().toLocalDate().getMonth().getValue() == month) {
                    map.put(id, miles);
                } else if (fly.getResultList().get(i).getStartTime().toLocalDate().getMonth().getValue() == month) {
                    miles = miles + map.get(id);
                    map.put(id, miles);
                }
            }
            return map;
        }



    @Override // returns a map of miles per month of all planes for each month
    public HashMap<Integer, Double> getMilesPerMonthYear() {
        HashMap<Integer,Double> avgPerMonth = new HashMap<>();
        int month = Integer.parseInt(Instant.now().toString().split("T")[0].split("-")[1]);
        for (int i=1; i<=month; i++) {
            HashMap<String,Double> perMonth = getMilesPerMonth(i);
            if (perMonth.size()==0){
                avgPerMonth.put(i,0.0);
            }
            else {
                final double[] sum = {0};
                perMonth.forEach((s, aDouble) -> sum[0] += perMonth.get(s));
                double avg = sum[0] / perMonth.size();
                avgPerMonth.put(i, avg);
                sum[0] = 0;
            }
        }
        return avgPerMonth;
    }

    @Override // get fleet size n each month until desired month
    public HashMap<Integer, Integer> getFleetSize(int month) {
        HashMap<Integer,Integer> map = new HashMap<>();
        for (int i = 1; i <=month ; i++) {
            map.put(i,0);
        }
        for (int i= 0; i<plane.getResultList().size();i++)
        {
            String id = plane.getResultList().get(i).getPlaneid();
            for (int j=1; j<=month;j++)
            {
                if(activeplane(id,j))
                {
                    map.put(j,map.get(j)+1);
                }
            }
        }
        return map;
    }
    //a help function to the function above
    public boolean activeplane (String id,int month)
    {
        for(int i=0; i<fly.getResultList().size();i++)
        {
            if((fly.getResultList().get(i).getPlaneid().equals(id))&& (fly.getResultList().get(i).getStartTime().toLocalDate().getMonth().getValue()==month))
                return true;
        }
        return false;
    }



    @Override // pulls a flight record according to flight id as a timeseries
    public TimeSeries getFlightRecord(String id) throws IOException, ClassNotFoundException {
        for (int i = 0; i < history.getResultList().size() ; i++) {
            if (history.getResultList().get(i).getFlightid().equals(id)){
               byte [] bytes = history.getResultList().get(i).getTimeseries();
               return (TimeSeries) this.bytesToObject(bytes);
            }
        }
        return null;
    }

    @Override// a function to save a finished flight record in DataBase , use in function below
    public void setFlightData(String fid , String pid , byte[]ts) {
        transaction.begin();
        History history1 = new History();
        history1.setFlightid(fid);
        history1.setPlaneid(pid);
        history1.setTimeseries(ts);
        entityManager.persist(history1);
        transaction.commit();
    }

    @Override //a function to save a finished flight in DataBase- flights table , plane table, and history table
    public void setFinishedFlight(String pid, String fid, FlightData flightData) throws IOException {
        transaction.begin();
        Flights flights = new Flights();
        if (isFirstFlight(pid)) {
            Plane plane1 =new Plane();
            plane1.setPlaneid(pid);
            //plane1.setFirstflight();
            entityManager.persist(plane1);
            flights.setFlightid(fid);
            flights.setPlaneid(pid);
            //flights.setStartTime(st);
            //flights.setEndTime(et);
            //flights.setFlyFrom();
            //flights.setFlyTo();
            flights.setMiles(flightData.getMiles());
            flights.setMaxSpeed((double) flightData.getMaxSpeed());
            flights.setMaxAlitude((double) flightData.getMaxAltitude());
            setFlightData(fid,pid ,this.objectToBytes(flightData.getTs()));
            entityManager.persist(flights);
            transaction.commit();
        }
        else {
            flights.setFlightid(fid);
            flights.setPlaneid(pid);
            //flights.setStartTime(st);
            //flights.setEndTime(et);
            //flights.setFlyFrom();
            //flights.setFlyTo();
            flights.setMiles(flightData.getMiles());
            flights.setMaxSpeed((double) flightData.getMaxSpeed());
            flights.setMaxAlitude((double) flightData.getMaxAltitude());
            entityManager.persist(flights);
            transaction.commit();
        }

    }

    @Override // a help function to other function
    public boolean isFirstFlight(String pid) {
        for (int i = 0; i < plane.getResultList().size() ; i++) {
            if (plane.getResultList().get(i).getPlaneid().equals(pid))
                return true;
        }
        return false;
    }

    @Override //a help function to other function
    public Date dateFirstFlight(String pid) {
        for (int i = 0; i < plane.getResultList().size() ; i++) {
            if (plane.getResultList().get(i).getPlaneid().equals(pid))
                return plane.getResultList().get(i).getFirstflight();
        }
        return null;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        dbhandler db = new dbhandler();
        HashMap<Integer,Integer> map = db.getFleetSize(6);
        int x = 1;
        int y = 2;
        System.out.println(x+y);
    }
}
