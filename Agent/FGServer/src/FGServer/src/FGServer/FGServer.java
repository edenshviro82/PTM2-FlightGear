package FGServer;

import java.beans.XMLEncoder;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;

public class FGServer implements ServerService{
    private DataCollector dataCollector;
    private ExecutorService es;
    private HashMap<String,Runnable> commandMap;
    private volatile boolean stop;
    //model's service date members
    private PrintWriter out2model;
    private ObjectOutputStream objectOutputStream;

    public FGServer() {
        this.dataCollector = new DataCollector();
        this.initCommandMap();
        es = Executors.newFixedThreadPool(3);
        es.execute(this::runFgServer);
        es.execute(this::runModelServer);
    }

    private void runFgServer() {
        try {
            ServerSocket server = new ServerSocket(Integer.parseInt(Properties.map.get("fgPort")));
            System.out.println("flight gear server is open");
            server.setSoTimeout(1000);
            while(!stop) {
                try {
                    Socket client = server.accept();
                    System.out.println("simulator is connected to my server");
                    BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    this.dataCollector.initFlightData(in.readLine());
                    while(!stop) {
                        String stream = in.readLine();
                        es.execute(()-> dataCollector.updateFlightData(stream));
                    }
                    //get the last stream from flight gear to set the last details flightData needs
                    String lastStream = dataCollector.getStream();
                    this.dataCollector.completeFlightData();
                }
                catch(SocketTimeoutException e) { }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void runModelServer() {
        try {
            ServerSocket server = new ServerSocket(Integer.parseInt(Properties.map.get("modelPort")));
            System.out.println("model server is open");
            server.setSoTimeout(1000);
            String line;
            while(!stop) {
                try {
                    Socket agent = server.accept();
                    System.out.println("Agent is connected to my service");
                    BufferedReader  in = new BufferedReader(new InputStreamReader(agent.getInputStream()));
                    OutputStream out = agent.getOutputStream();
                    out2model = new PrintWriter(new OutputStreamWriter(out),true);
                    objectOutputStream = new ObjectOutputStream(out);
                    while(!(line = in.readLine()).equals("bye")) {
                        System.out.println(line); // for debug
                        //command example: get aileron
                        String[] split = line.split(" ");
                        String command = split[1];
                        if (commandMap.containsKey(command))
                            es.execute(()-> commandMap.get(command).run());
                    }
                    this.stop = true;
                }
                catch(SocketTimeoutException e) { }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //init methods/////////////////////////////////////////////////////////////

    private void initCommandMap() {
        this.commandMap = new HashMap<>();
        commandMap.put("aileron", this::getAileron);
        commandMap.put("rudder", this::getRudder);
        commandMap.put("throttle", this::getThrottle);
        commandMap.put("breaks", this::getBreaks);
        commandMap.put("elevators", this::getElevators);
        commandMap.put("alt", this::getAlt);
        commandMap.put("heading", this::getHeading);
        commandMap.put("airspeed", this::getAirSpeed);
        commandMap.put("roll", this::getRoll);
        commandMap.put("pitch", this::getPitch);
        commandMap.put("location", this::getLocation);
        commandMap.put("flight", this::getFlight);
        commandMap.put("stream", this::getStream);
    }


    //get methods//////////////////////////////////////////////////////////////////
    @Override
    public void getAileron() {
        out2model.println(dataCollector.getAileron());
    }

    @Override
    public void getRudder() {
        out2model.println(dataCollector.getRudder());
    }

    @Override
    public void getThrottle() {
        out2model.println(dataCollector.getThrottle());
    }

    @Override
    public void getBreaks() {
        out2model.println(dataCollector.getBreaks());
    }

    @Override
    public void getElevators() {
        out2model.println(dataCollector.getElevators());
    }

    @Override
    public void getAlt() {
        out2model.println(dataCollector.getAlt());
    }

    @Override
    public void getHeading() {
        out2model.println(dataCollector.getHeading());
    }

    @Override
    public void getAirSpeed() {
        out2model.println(dataCollector.getAirSpeed());
    }

    @Override
    public void getRoll() {
        out2model.println(dataCollector.getRoll());
    }

    @Override
    public void getPitch() {
        out2model.println(dataCollector.getPitch());
    }

    @Override
    public void getStream() {
        out2model.println(dataCollector.getStream());
    }

    @Override
    public void getLocation() {
        try {
            objectOutputStream.writeObject(dataCollector.getLocation());
            objectOutputStream.flush();
        } catch (IOException e) { e.printStackTrace();}
    }

    @Override
    public void getFlight() {
        try {
            objectOutputStream.writeObject(dataCollector.getFlight());
            objectOutputStream.flush();
        } catch (IOException e) { e.printStackTrace(); }
    }

    @Override
    protected void finalize() throws Throwable {
        this.out2model.close();
        this.objectOutputStream.close();
        this.es.shutdown();
    }
}

