package test;

import necessary_classes.FlightData;
import necessary_classes.Location;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;
import java.util.function.Consumer;

public class Test {
    Scanner in;
    PrintWriter out;
    ObjectInputStream objectInputStream;
    HashMap<String, Runnable> getMap;
    HashMap<String, Consumer<Float>> setMap;

    public Test() throws IOException {
        Socket agent = new Socket("127.0.0.1", 5404);
        in = new Scanner(agent.getInputStream());
        out = new PrintWriter(agent.getOutputStream(),true);
        objectInputStream = new ObjectInputStream(agent.getInputStream());

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
        getMap.put("start flight",this::startFlight);
        getMap.put("end flight", this::endFlight);

        setMap = new HashMap<>();
        setMap.put("set aileron", this::setAileron);
        setMap.put("set elevators", this::setElevators);
        setMap.put("set rudder", this::setRudder);
        setMap.put("set throttle", this::setThrottle);
        setMap.put("set brakes", this::setBreaks);
    }

    //set methods//////////////////////////////////////////////////////////////////////////
    public void setAileron(float value) {
        out.println("set aileron " + value);
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
        out.println("set breaks " + value);
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
        out.println("get breaks");
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
        } catch (IOException | ClassNotFoundException e) { e.printStackTrace(); }
    }

    public void startFlight() {
        out.println("start flight");
    }

    public void endFlight() {
        out.println("end flight");
    }
}
