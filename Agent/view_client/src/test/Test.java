package test;

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
    HashMap<String, Runnable> getMap;
    HashMap<String, Consumer<Float>> setMap;

    public Test() throws IOException {
        Socket agent = new Socket("127.0.0.1", 5405);
        in = new Scanner(agent.getInputStream());
        out = new PrintWriter(agent.getOutputStream(),true);

        getMap = new HashMap<>();
        getMap.put("print stream", this::printStream);
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
    public void setAileron(float value) { out.println("set aileron " + value); }

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
    public void printStream() {
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < 11000) {
            out.println("print stream");
            System.out.println(in.next());
        }
    }


    public void startFlight() {
        out.println("start flight");
    }

    public void endFlight() {
        out.println("end flight");
    }
}
