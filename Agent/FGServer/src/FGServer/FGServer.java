package FGServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


//FGServer class will wrap the data collector class and will be responsible for open servers to flight gear
//simulator and the agent's model, and act by given stream of date from flight gear or any request from agent
public class FGServer implements ServerService{
    private DataCollector dataCollector;
    private ExecutorService es;
    private HashMap<String,Runnable> commandMap;
    private volatile boolean stop;
    Socket flightGear;
    //model's service members
    private PrintWriter out2model;
    private ObjectOutputStream objectOutputStream;

    public FGServer() {
        this.dataCollector = new DataCollector();
        this.initCommandMap();
        es = Executors.newFixedThreadPool(3);
        es.execute(this::runFgServer);
    }

    //servers methods////////////////////////////////////////////////////////////////////////

    //this method is responsible for establish connection with flight gear simulator
    private void runFgServer() {
        try {
            ServerSocket server = new ServerSocket(Integer.parseInt(Properties.map.get("fgPort")));
            System.out.println("flight gear server is open");
            flightGear = server.accept();
            System.out.println("simulator is connected to my server");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //given a "start flight" instruction from the agent, we will start to collect data from fight gear
    private void startFlight() {
        this.stop = false;
        while (!stop) {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(flightGear.getInputStream()));
                this.dataCollector.initFlightData(in.readLine());
                while(!stop) {
                    String stream = in.readLine();
                    es.execute(()-> dataCollector.updateFlightData(stream));
                }
            } catch (IOException e) { e.printStackTrace(); }
        }
    }

    private void endFlight() {
        this.stop = true;
        this.dataCollector.completeFlightData();
    }

    public void runModelServer() {
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
                        if (commandMap.containsKey(line)) {
                            String finalLine = line;
                            es.execute(()-> commandMap.get(finalLine).run());
                        }
                    }
                }
                catch(SocketTimeoutException e) { }
            }
        } catch (IOException e) { e.printStackTrace();}
    }

    //init methods/////////////////////////////////////////////////////////////

    private void initCommandMap() {
        this.commandMap = new HashMap<>();
        commandMap.put("get aileron", this::getAileron);
        commandMap.put("get rudder", this::getRudder);
        commandMap.put("get throttle", this::getThrottle);
        commandMap.put("get brakes", this::getBrakes);
        commandMap.put("get elevators", this::getElevators);
        commandMap.put("get alt", this::getAlt);
        commandMap.put("get heading", this::getHeading);
        commandMap.put("get airspeed", this::getAirSpeed);
        commandMap.put("get roll", this::getRoll);
        commandMap.put("get pitch", this::getPitch);
        commandMap.put("get location", this::getLocation);
        commandMap.put("get flight", this::getFlight);
        commandMap.put("get stream", this::getStream);
        commandMap.put("get plane", this::getPlane);
        commandMap.put("start flight",this::startFlight);
        commandMap.put("end flight",this::endFlight);
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
    public void getBrakes() {
        out2model.println(dataCollector.getBrakes());
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
    public void getPlane() {
        try {
            objectOutputStream.writeObject(dataCollector.getPlane());
            objectOutputStream.flush();
        } catch (IOException e) {e.printStackTrace();}
    }

    @Override
    protected void finalize() throws Throwable {
        this.out2model.close();
        this.objectOutputStream.close();
        flightGear.close();
        this.es.shutdown();
    }
}

