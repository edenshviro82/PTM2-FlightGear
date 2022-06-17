package controller;
import command.Command;
import model.Model;
import view.View;
import java.io.*;
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
        this.es = Executors.newSingleThreadExecutor();
        //this.es = Executors.newFixedThreadPool(5);
        this.c = new Commands(m,v);
        initCommandMap();
    }
    //Command map .

    private void initCommandMap() {
        this.commandMap = new HashMap<String, Command>();
        commandMap.put("set aileron", c.new setAileronCommand());
        commandMap.put("set rudder", c.new setRudderCommand());
        commandMap.put("set throttle", c.new setThrottleCommand());
        commandMap.put("set elevators", c.new setElevatorCommand());
        commandMap.put("set brakes", c.new setBrakesCommand());
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
        commandMap.put("get brakes", c.new getBrakesCommand());
        commandMap.put("get FlightRecord", c.new getFlightRecord());
        commandMap.put("start flight", c.new startFlightCommand());
        commandMap.put("end flight", c.new endFlightCommand());
        // command for view
        commandMap.put("1", c.new endFlightCommand());
        commandMap.put("2", c.new endFlightCommand());
        commandMap.put("3", c.new endFlightCommand());
        commandMap.put("4", c.new endFlightCommand());
        commandMap.put("5", c.new endFlightCommand());

    }
    // met open server.
    public void openServer() throws IOException, ClassNotFoundException {
        ServerSocket ss = new ServerSocket(4999);
        Socket s = ss.accept();
        //try
        c.setFrontStreams(s);
        //end
        s.setSoTimeout(1000000);
        System.out.println("client has connected");
        PrintWriter out = new PrintWriter(s.getOutputStream());
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        while (true) {
            String str = in.readLine();
            System.out.println(str);
            String [] split = str.split(" ");
            String command = split[0]+" "+split[1];
            es.execute(()-> {
                try {
                    commandMap.get(command).execute(str);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });
            //commandMap.get(command).execute(str);
            if (str.equals("bye"))
                break;
        }
    }

    @Override
    public void update(Observable o, Object arg) {
    }
    //test only method
    public void connectToFGAgent() {
        try {
            Socket Agent = new Socket("127.0.0.1",5404);
            c.setAgentStreams(Agent);
        } catch (IOException e) { e.printStackTrace(); }
    }
    // test main to check connections
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Model m = new Model();
        View v = new View();
        Controller c = new Controller(m,v);
        c.connectToFGAgent();
        c.openServer();
    }
}
