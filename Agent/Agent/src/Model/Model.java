package Model;

import necessary_classes.FlightData;
import necessary_classes.Location;
import necessary_classes.Plane;
import necessary_classes.Properties;

import java.io.*;
import java.net.Socket;
import java.util.Observable;

public class Model extends Observable implements ModelAPI {
    //FGServer's streams
    public PrintWriter out2FGServer;
    public BufferedReader fromFGServer;
    ObjectInputStream objectInputStream;
    //FGClient's streams
    PrintWriter out2FGClient;

    public Model() {
        this.connectToFGClient();
        this.connectToFGServer();
    }

    //establishment methods////////////////////////////////////////////////////////////////
    public void connectToFGClient() {
        try {
            Socket FGClient = new Socket(Properties.map.get("FGClient_ip"), Integer.parseInt(Properties.map.get("FGClient_port")));
            out2FGClient = new PrintWriter(FGClient.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connectToFGServer() {
        try {
            Socket FGServer = new Socket(Properties.map.get("FGServer_ip"), Integer.parseInt(Properties.map.get("FGServer_port")));
            InputStream in = FGServer.getInputStream();
            out2FGServer = new PrintWriter(FGServer.getOutputStream(), true);
            fromFGServer = new BufferedReader(new InputStreamReader(in));
            objectInputStream = new ObjectInputStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //get methods///////////////////////////////////////////////////////////////////////////
    @Override
    public String getAileron() throws IOException {
        out2FGServer.println("get aileron");
        return fromFGServer.readLine();
    }

    //set methods//////////////////////////////////////////////////////////////////////////
    public void setAileron(float value) {
        out2FGClient.println("set aileron " + value);
    }

    @Override
    public String getElevators() throws IOException {
        out2FGServer.println("get elevators");
        return fromFGServer.readLine();
    }

    public void setElevators(float value) {
        out2FGClient.println("set elevators " + value);
    }

    @Override
    public String getRudder() throws IOException {
        out2FGServer.println("get rudder");
        return fromFGServer.readLine();
    }

    public void setRudder(float value) {
        out2FGClient.println("set rudder " + value);
    }

    @Override
    public String getThrottle() throws IOException {
        out2FGServer.println("get throttle");
        return fromFGServer.readLine();
    }

    public void setThrottle(float value) {
        out2FGClient.println("set throttle " + value);
    }

    @Override
    public String getBrakes() throws IOException {
        out2FGServer.println("get brakes");
        return fromFGServer.readLine();
    }

    public void setBrakes(float value) {
        out2FGClient.println("set brakes " + value);
    }

    @Override
    public String getAlt() throws IOException {
        out2FGServer.println("get alt");
        return fromFGServer.readLine();
    }

    @Override
    public String getHeading() throws IOException {
        out2FGServer.println("get heading");
        return fromFGServer.readLine();
    }

    @Override
    public String getAirSpeed() throws IOException {
        out2FGServer.println("get airspeed");
        return fromFGServer.readLine();
    }

    @Override
    public String getRoll() throws IOException {
        out2FGServer.println("get roll");
        return fromFGServer.readLine();
    }

    @Override
    public String getPitch() throws IOException {
        out2FGServer.println("get pitch");
        return fromFGServer.readLine();
    }

    @Override
    public String getStream() throws IOException {
        out2FGServer.println("get stream");
        return fromFGServer.readLine();
    }

    @Override
    public Location getLocation() throws IOException, ClassNotFoundException {
        out2FGServer.println("get location");
        return (Location) objectInputStream.readObject();
    }

    @Override
    public FlightData getFlight() throws IOException, ClassNotFoundException {
        out2FGServer.println("get flight");
        return (FlightData) objectInputStream.readObject();
    }

    public Plane getPlane() throws IOException, ClassNotFoundException {
        out2FGServer.println("get plane");
        return (Plane) objectInputStream.readObject();
    }

    //start,end methods
    public void startFlight() {
        out2FGServer.println("start flight");
    }

    public void endFlight() {
        out2FGServer.println("end flight");
    }

}
