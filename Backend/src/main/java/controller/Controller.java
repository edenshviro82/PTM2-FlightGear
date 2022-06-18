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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Controller implements Observer {
    Model m ;
    View v ;
    HashMap<String,Command> commandMap;
    HashMap<String,Runnable> viewdMap;
    Commands c ;
    ExecutorService es  ;
    public static ConcurrentHashMap<String, AgentStreamers> activePlanes= new ConcurrentHashMap<>();
    private boolean stop ;
    public Controller(Model m, View v){
        this.m=m;
        m.addObserver(this);
        this.v=v;
        v.addObserver(this);
       // this.es = Executors.newSingleThreadExecutor();
        this.es = Executors.newFixedThreadPool(10);
        this.c = new Commands(m,v);
        initCommandMap();
        this.es.execute(this::openAgentsServer);
        this.es.execute(() -> {
            try {
                openServer();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        this.es.execute(this::openAgentsStreamServer);
    }
    //Command map .

    private void initCommandMap() {
        this.commandMap = new HashMap<String, Command>();
        commandMap.put("set agent", c.new setAgentCommand());
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
        //commandMap.put("get flight", c.new setFinishedFlight());
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
        commandMap.put("get planes", c.new getPlanePositionCommand());
        // command for view
        commandMap.put("shut down", c.new shutDown());
        commandMap.put("do reset", c.new reset());
        commandMap.put("active threads", c.new listOfThreads());
        commandMap.put("waiting tasks", c.new listOfTasks());
        commandMap.put("active planes", c.new ListOfActiveAgents());
    }
    private void inniteViewMap(){
        this.viewdMap = new HashMap<>();
        viewdMap.put("shut down",()->{
            this.v.sendResponse("server is closed");
            this.es.shutdown();

        });
        viewdMap.put("active threads",()->{
            if (es instanceof ThreadPoolExecutor){
                this.v.sendResponse(""+((ThreadPoolExecutor)es).getActiveCount());
            }


        });
        viewdMap.put("waiting tasks",()->{
            if (es instanceof ThreadPoolExecutor){
                this.v.sendResponse(""+((ThreadPoolExecutor)es).getTaskCount());
            }


        });
        viewdMap.put("active planes", ()->{
            this.v.sendResponse(""+activePlanes.size());

        });
    }

    // open frontend server
    public void openServer() throws IOException, ClassNotFoundException {
        ServerSocket ss = new ServerSocket(7070);
        System.out.println("server is running....");
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
            if (commandMap.get(command) != null) {
                es.execute(() -> {
                    try {
                        commandMap.get(command).execute(str);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                });
            }
            //commandMap.get(command).execute(str);
            if (str.equals("bye"))
                break;
        }
    }
    // open agents server and maps each agent port to his id via map
    public void openAgentsServer(){
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(6060);
            System.out.println("agents server is open.....");
            while(true)
                try {
            System.out.println(" waiting for agent....");
            Socket agent = ss.accept();
            System.out.println("agent has connected");
//            BufferedReader in = new BufferedReader(new InputStreamReader(agent.getInputStream()));
//            String agentId = in.readLine();
//            System.out.println(agentId);
            PrintWriter outOp = new PrintWriter(agent.getOutputStream(),true);
            BufferedReader inOp = new BufferedReader(new InputStreamReader(agent.getInputStream()));
            ObjectInputStream obj = new ObjectInputStream(agent.getInputStream());
//            if (Controller.activePlanes.containsKey(agentId)) {
//                Controller.activePlanes.get(agentId).setObjectInputStream(obj).
//                        setOperationOut(outOp).setOperationIn(inOp);
//            }
//            else{
//                AgentStreamers agentStreamer = new AgentStreamers(inOp,outOp,obj);
//                Controller.activePlanes.put(agentId,agentStreamer);
//            }
            Controller.activePlanes.put("100",new AgentStreamers(inOp,outOp,obj));

            commandMap.get("set agent").execute("set agent 100");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public void openAgentsStreamServer(){
        ServerSocket stream = null;
        try {
            stream = new ServerSocket(5050);
            System.out.println("agents stream server is open.....");
            while(true)
                try {
                    System.out.println(" waiting for agent...");
                    Socket agent = stream.accept();
                    System.out.println("agent has connected");
                    //BufferedReader in = new BufferedReader(new InputStreamReader(agent.getInputStream()));
                    //String agentId = in.readLine();
                    //System.out.println(agentId);
                    //if (Controller.activePlanes.containsKey(agentId))
                    //{
                    //    Controller.activePlanes.get(agentId).setStream(agent);
                    //}
                    //else{
                    //    AgentSockets as =new AgentSockets(agent,null);
                    //    Controller.activePlanes.put(agentId,as);
                    //}
                } catch (IOException e) {
                    e.printStackTrace();
                }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    @Override
    public void update(Observable o, Object arg) {
        if (o == this.v) {
            String str = this.v.getCommand();
            System.out.println(str);
            String [] split = str.split(" ");
            String command = split[0]+" "+split[1];
            if (viewdMap.get(command) != null) {
                es.execute(viewdMap.get(str));

            }
        }
        }

    //test only method
//    public void connectToFGAgent() {
//        try {
//            Socket Agent = new Socket("127.0.0.1",5404);
//            c.setAgentStreams(Agent);
//        } catch (IOException e) { e.printStackTrace(); }
//    }
    // test main to check connections
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Model m = new Model();
        View v = new View();
        Controller controller = new Controller(m,v);
    }
}
