package Controller;

import Model.Model;
import View.View;
import necessary_classes.Properties;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Controller implements Observer{
    HashMap<String,Command> commandMap;
    Commands c;
    Model m;
    private volatile boolean stop;
    ExecutorService es;
    View v;


    public Controller(Model m, View v) {
        this.m = m;
        this.v = v;
        this.es = Executors.newSingleThreadExecutor();
        this.c = new Commands(m,v);
        this.initCommandMap();
    }

    public void runBackendServer() {
        try {
            ServerSocket server = new ServerSocket(Integer.parseInt(Properties.map.get("backend_port")));
            System.out.println("Backend server is open");
            server.setSoTimeout(1000);
            String line;
            while(!stop) {
                try {
                    Socket backend = server.accept();
                    System.out.println("Backend is connected to my service");
                    BufferedReader in = new BufferedReader(new InputStreamReader(backend.getInputStream()));
                    OutputStream out = backend.getOutputStream();
                    this.c.setOutputStream(out);
                    while(!(line = in.readLine()).equals("bye")) {
                        System.out.println(line); // for debug
                        //command example: get aileron , set aileron 1
                        String[] split = line.split(" ");
                        String command = split[0] + " " +split[1];
                        if (commandMap.containsKey(command)) {
                            String finalLine = line;
                            es.execute(()-> {
                                try {
                                    commandMap.get(command).execute(finalLine);
                                } catch (IOException | ClassNotFoundException e) { e.printStackTrace();}
                            });
                        }
                    }
                    this.stop = true;
                }
                catch(SocketTimeoutException e) { }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initCommandMap() {
        this.commandMap = new HashMap<String, Command>();
        commandMap.put("set aileron", c.new setAileronCommand());
        commandMap.put("set rudder", c.new setRudderCommand());
        commandMap.put("set throttle", c.new setThrottleCommand());
        commandMap.put("set elevators", c.new setElevatorsCommand());
        commandMap.put("set breaks", c.new setBreaksCommand());
        commandMap.put("get breaks", c.new getBreaksCommand());
        commandMap.put("get aileron", c.new getAileronCommand());
        commandMap.put("get rudder", c.new getRudderCommand());
        commandMap.put("get throttle", c.new getThrottleCommand());
        commandMap.put("get elevators", c.new getElevatorsCommand());
        commandMap.put("get alt", c.new getAltCommand());
        commandMap.put("get heading", c.new getHeadingCommand());
        commandMap.put("get airspeed", c.new getAirSpeedCommand());
        commandMap.put("get roll", c.new getRollCommand());
        commandMap.put("get pitch", c.new getPitchCommand());
        commandMap.put("get location", c.new getLocationCommand());
        commandMap.put("get flight", c.new getFlightCommand());
        commandMap.put("get stream", c.new getStreamCommand());
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
