package test;

import necessary_classes.FlightData;
import necessary_classes.Plane;
import necessary_classes.TimeSeries;

import java.io.*;
import java.net.Socket;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class Test {
    Scanner in;
    PrintWriter out;
    BufferedReader streamIn;
    ObjectInputStream objectInputStream;
    HashMap<String, Runnable> getMap;
    HashMap<String, Consumer<Float>> setMap;
    ExecutorService es = Executors.newSingleThreadExecutor();

    public Test() throws IOException {
        Socket agent = new Socket("127.0.0.1", 7070);
        in = new Scanner(agent.getInputStream());
        out = new PrintWriter(agent.getOutputStream(), true);
        objectInputStream = new ObjectInputStream(agent.getInputStream());

        Socket stream = new Socket("127.0.0.1",3030);
        streamIn = new BufferedReader(new InputStreamReader(stream.getInputStream()));

        getMap = new HashMap<>();
        getMap.put("get aileron", this::getAileron);
        getMap.put("get elevators", this::getElevators);
        getMap.put("get rudder", this::getRudder);
        getMap.put("get throttle", this::getThrottle);
        getMap.put("get brakes", this::getBreaks);
        getMap.put("get alt", this::getAlt);
        getMap.put("get heading", this::getHeading);
        getMap.put("get airspeed", this::getAirSpeed);
        getMap.put("get roll", this::getRoll);
        getMap.put("get pitch", this::getPitch);
        getMap.put("get stream", this::getStream);
        getMap.put("get location", this::getLocation);
        getMap.put("get flight", this::getFlight);
        getMap.put("get planes", this::getPlanes);
        getMap.put("start flight", this::startFlight);
        getMap.put("end flight", this::endFlight);
        getMap.put("1 1", this::getMilesPerMonth);
        getMap.put("2 2", this::getMilesPerMonthYear);
        getMap.put("3 3", this::getFleetSize);
        getMap.put("4 4", this::getFlightRecord);


        setMap = new HashMap<>();
        setMap.put("set aileron", this::setAileron);
        setMap.put("set elevators", this::setElevators);
        setMap.put("set rudder", this::setRudder);
        setMap.put("set throttle", this::setThrottle);
        setMap.put("set brakes", this::setBreaks);
        setMap.put("set agent", this::setAgent);
    }

    //set methods//////////////////////////////////////////////////////////////////////////
    public void setAileron(float value) {
        out.println("set aileron " + value);
    }

    public void setAgent(float value) {
        out.println("set agent " + (int)value);
    }

    public void setElevators(float value) {
        out.println("set elevators " + value);
    }

    public void setRudder(float value) {
        out.println("set rudder " + value);
    }

    public void setThrottle(float value) {
        out.println("set throttle " + value);
    }

    public void setBreaks(float value) {
        out.println("set brakes " + value);
    }

    //get methods///////////////////////////////////////////////////////////////////////////
    public void getAileron() {
        out.println("get aileron");
        System.out.println(in.next());
    }

    public void getElevators() {
        out.println("get elevators");
        System.out.println(in.next());
    }

    public void getRudder() {
        out.println("get rudder");
        System.out.println(in.next());
    }

    public void getThrottle() {
        out.println("get throttle");
        System.out.println(in.next());
    }

    public void getBreaks() {
        out.println("get brakes");
        System.out.println(in.next());
    }

    public void getAlt() {
        out.println("get alt");
        System.out.println(in.next());
    }

    public void getHeading() {
        out.println("get heading");
        System.out.println(in.next());
    }

    public void getAirSpeed() {
        out.println("get airspeed");
        System.out.println(in.next());
    }

    public void getRoll() {
        out.println("get roll");
        System.out.println(in.next());
    }

    public void getPitch() {
        out.println("get pitch");
        System.out.println(in.next());
    }

    public void getStream() {
        out.println("get stream");
        System.out.println(in.next());
    }

    public void getLocation() {
        out.println("get location");
        try {
            System.out.println(objectInputStream.readObject());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void getFlight() {
        out.println("get flight");
        try {
            FlightData flight = (FlightData) objectInputStream.readObject();
            System.out.println(flight.getFlyFrom());
            System.out.println(flight.getFlyTo());
            System.out.println(flight.getStartTime());
            System.out.println(flight.getEndTime());
            System.out.println(flight.getMaxAltitude());
            System.out.println(flight.getMaxSpeed());
            System.out.println(flight.getMiles());
            System.out.println(flight.getTs());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void getPlanes() {
        try {
            out.println("get planes");
            ArrayList<Plane> planes = (ArrayList<Plane>) objectInputStream.readObject();
            System.out.println("alt: " + planes.get(0).getAlt());
            System.out.println("planes id: " + planes.get(0).getPlainId());
            System.out.println("flight id: " + planes.get(0).getFlightID());
            System.out.println("heading: " + planes.get(0).getHeading());
            System.out.println(planes.get(0).getLocation());
            System.out.println("speed: " + planes.get(0).getSpeed());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void startFlight() {
        out.println("start flight");
        es.execute(()-> {
            while (true) {
                try {
                    System.out.println(streamIn.readLine());
                } catch (IOException e) {e.printStackTrace();}
            }
        });
    }

    public void endFlight() {
        out.println("end flight");
    }

    ///////////////////////////////////Backed/////////////////////


    public void getMilesPerMonth() {
        out.println("get MilesPerMonth");
        try {
            Map<String, Double> miles = (Map<String, Double>) objectInputStream.readObject();
            miles.forEach((s, aDouble) -> {
                System.out.println(s);
                System.out.println(aDouble);
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void getMilesPerMonthYear() {
        out.println("get MilesPerMonthYear");
        try {
            HashMap<Integer, Double> milesYear = (HashMap<Integer, Double>) objectInputStream.readObject();
            milesYear.forEach((s, aDouble) -> {
                System.out.println(s);
                System.out.println(aDouble);
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void getFleetSize() {
        out.println("get FleetSize");
        try {
            HashMap<Integer, Integer> fleetSize = (HashMap<Integer, Integer>) objectInputStream.readObject();
            fleetSize.forEach((s, aDouble) -> {
                System.out.println(s);
                System.out.println(aDouble);
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void getFlightRecord() {
        out.println("get FlightRecord 999");
        try {
            TimeSeries ts = (TimeSeries) objectInputStream.readObject();
            int num = ts.getSize();
            System.out.println(ts.getSize());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
//    public void setFinishedFlight(String pid , String fid , FlightData flightData) throws IOException{
//        out.println("get Flight "+pid+" "+fid+" "+flightData);
//
//    }
    //public boolean isFirstFlight(String pid);
    //public Date dateFirstFlight(String pid){}

}
