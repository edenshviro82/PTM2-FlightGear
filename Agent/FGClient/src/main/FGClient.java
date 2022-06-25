package main;

import main.FGClientApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class FGClient implements FGClientApi {
    private final Client FlightGear;
    private final HashMap<String, Consumer<Float>> map;
    //server for the agent's model
    ServerSocket server;
    private volatile boolean stop;
    private ExecutorService es;

    public FGClient(Client cl) {
        this.es = Executors.newSingleThreadExecutor();
        this.FlightGear = cl;
        this.stop = false;
        this.map = new HashMap<>();
        map.put("aileron", (value) -> this.setAileron(value));
        map.put("throttle", (value) -> this.setThrottle(value));
        map.put("rudder", (value) -> this.setRudder(value));
        map.put("brakes", (value) -> this.setBrakes(value));
        map.put("elevators", (value) -> this.setElevators(value));
    }

    public void runServer() {
        try {
            server = new ServerSocket(Integer.parseInt(Client.properties.get("model_port")));
            System.out.println("server is open");
            server.setSoTimeout(1000);
            String line;
            while (!stop) {
                try {
                    Socket agent = server.accept();
                    System.out.println("Agent is connected to my service");
                    BufferedReader in = new BufferedReader(new InputStreamReader(agent.getInputStream()));
                    while (!(line = in.readLine()).equals("bye")) {
                        System.out.println(line);
                        //command example: set aileron 1
                        String[] split = line.split(" ");
                        String command = split[1];
                        Float value = Float.parseFloat(split[2]);
                        if (map.containsKey(command))
                            es.execute(() -> map.get(command).accept(value));
                    }
                    this.stop = true;
                } catch (SocketTimeoutException e) {
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setAileron(float value) {
        FlightGear.setAileron(value);
    }

    public void setThrottle(float value) {
        FlightGear.setThrottle(value);
    }

    public void setRudder(float value) {
        FlightGear.setRudder(value);
    }

    public void setBrakes(float value) {
        FlightGear.setBreaks(value);
    }

    public void setElevators(float value) {
        FlightGear.setElevators(value);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        this.server.close();
        this.es.shutdownNow();
    }
}
