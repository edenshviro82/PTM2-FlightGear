package controller;
import command.Command;
import model.Model;
import view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class Controller implements Observer {
    Model m ;
    View v ;
    HashMap<String,Command> commandMap;
    Commands c ;
    ExecutorService es ;
    private boolean stop ;
    public Controller(Model m, View v){
        this.m=m;
        m.addObserver(this);
        this.v=v;
        v.addObserver(this);
        this.es = Executors.newFixedThreadPool(5);
        this.c = new Commands(m,v);
    }

    private void initCommandMap() {
        this.commandMap = new HashMap<String, Command>();
        commandMap.put("set aileron", c.new setAileronCommand());
        commandMap.put("set rudder", c.new setRudderCommand());
        commandMap.put("set throttle", c.new setThrottleCommand());
        commandMap.put("set elevators", c.new setElevatorCommand());
        commandMap.put("set breaks", c.new setBrakesCommand());
        commandMap.put("get breaks", c.new getBrakesCommand());
        commandMap.put("get aileron", c.new getAileronCommand());
        commandMap.put("get rudder", c.new getRudderCommand());
        commandMap.put("get throttle", c.new getThrottleCommand());
        commandMap.put("get elevators", c.new getElevatorCommand());
        commandMap.put("get alt", c.new getAltCommand());
        //commandMap.put("get heading", c.new getHeadingCommand());
        commandMap.put("get airspeed", c.new getAirspeedCommand());
        commandMap.put("get roll", c.new getRollCommand());
        commandMap.put("get pitch", c.new getPitchCommand());
        //commandMap.put("get location", c.new getLocationCommand());
        commandMap.put("get flight", c.new setFinishedFlight());
        commandMap.put("get stream", c.new getDataStreamCommand());
        commandMap.put("get MilesPerMonth", c.new getMilesPerMonthCommand());
        commandMap.put("get MilesPerMonthYear", c.new getMilesPerMonthYearCommand());
        commandMap.put("is FirstFlight", c.new isFirstFlightCommand());
        commandMap.put("date FirstFlight", c.new dateFirstFlightCommand());
        commandMap.put("get FleetSize", c.new getFleetSizeCommand());
    }

    public void openServer() throws IOException, ClassNotFoundException {
        ServerSocket ss = new ServerSocket(4999);
        Socket s = ss.accept();
        s.setSoTimeout(1000000);
        System.out.println("client has connected");
        PrintWriter out = new PrintWriter(s.getOutputStream());
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        while (true) {
            String str = in.readLine();
            String [] split = str.split(" ");
            String command = split[0]+" "+split[1];
            commandMap.get(command).execute(str);
            if (str.equals("bye"))
                break;
        }
    }
    @Override
    public void update(Observable o, Object arg) {
    }

}
