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
    private Object bytesToObject (byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }
    private byte[] objectToBytes(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }
    @Override
    public int getActivePlanes() {
        return 0;
    }

    @Override
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
        //} finally {
            //if (transaction.isActive()) {
                //transaction.rollback();
            //}
            //entityManager.close();
            //managerFactory.close();

        }
    //}


    @Override
    public HashMap<Integer, Double> getMilesPerMonthYear() {
//        Map<Integer,Map<String,Double>> monthMap= new HashMap<>();
//        int month = Integer.parseInt(Instant.now().toString().split("T")[0].split("-")[1]);
//        for(int i = 1; i<=month;i++){
//            Map<String, Double> map = getMilesPerMonth(i);
//            monthMap.put(i, map);
//        }
//        return monthMap;
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

    @Override
    public double getFleetsize() {
        double size = plane.getResultList().size();
        return size;
    }

    @Override
    public void getPlaneproper() {


    }

    @Override
    public TimeSeries getFlightRecord(String id) throws IOException, ClassNotFoundException {
        for (int i = 0; i < history.getResultList().size() ; i++) {
            if (history.getResultList().get(i).getFlightid().equals(id)){
               byte [] bytes = history.getResultList().get(i).getTimeseries();
               return (TimeSeries) this.bytesToObject(bytes);
            }
        }
        return null;
    }

    @Override
    public void setFlightData(String fid , String pid , byte[]ts) {
        transaction.begin();
        History history1 = new History();
        history1.setFlightid(fid);
        history1.setPlaneid(pid);
        history1.setTimeseries(ts);
        entityManager.persist(history1);
        transaction.commit();

    }

    @Override
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

    @Override
    public boolean isFirstFlight(String pid) {
        for (int i = 0; i < plane.getResultList().size() ; i++) {
            if (plane.getResultList().get(i).getPlaneid().equals(pid))
                return true;
        }
        return false;
    }

    @Override
    public Date dateFirstFlight(String pid) {
        for (int i = 0; i < plane.getResultList().size() ; i++) {
            if (plane.getResultList().get(i).getPlaneid().equals(pid))
                return plane.getResultList().get(i).getFirstflight();
        }
        return null;
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException {
        dbhandler db = new dbhandler();
//        Map<String, Double> map =db.getMilesPerMonth(9);
//        Map<Integer,Double> map1 = db.getMilesPerMonthYear();
//        double fs = db.getFleetsize();
//        //Date date = "2022-08-08";
//        //db.setFinishedFlight("907","444",100.0,500.0,900.0);
//        int x= 2;
//        x= x+3;
//        TimeSeries timeSeries = new TimeSeries();
//        timeSeries.getDataStreams().add("hello");
//        timeSeries.getDataStreams().add("Matias");
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        ObjectOutputStream obj = new ObjectOutputStream(out);
//        obj.writeObject(timeSeries);
//        byte[] bit = out.toByteArray();
//        ByteArrayInputStream in = new ByteArrayInputStream(bit);
//        ObjectInputStream is = new ObjectInputStream(in);
//        TimeSeries ts2 = (TimeSeries) is.readObject();
//        ts2.getDataStreams().forEach(s -> System.out.println(s));
//        //ArrayList<String> stringArrayList = new ArrayList<>();
//        //stringArrayList.add("Matias,Katlani,Amit,Yoter");
//        //timeSeries.setDataStreams(stringArrayList);
//        //db.setFlightData("666","888",timeSeries.tsToByteArray());
//        //TimeSeries timeSeries = new TimeSeries();
//        //byte[] bytes = db.getFlightRecord("666");
//       // TimeSeries amit =(TimeSeries) timeSeries.deserialize(bytes);
//        String str = "Matias The King !!!!!!!";

//        TimeSeries timeSeries = new TimeSeries();
//        timeSeries.getDataStreams().add("Matias,Amit,Taltal,Katlni");
//        timeSeries.getDataStreams().add("Matias , kosomo");
//        db.setFlightData("999","987", db.objectToBytes(timeSeries));

        TimeSeries ts2 = db.getFlightRecord("999");
        ts2.getDataStreams().forEach(s -> System.out.println(s));









    }
}
