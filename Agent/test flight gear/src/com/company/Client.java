package com.company;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;

public class Client {
    //hash map for properties
    HashMap<String,String> properties;
    Socket fg;
    PrintWriter out2fg;

    public Client(String propertiesFileName) {
        properties = new HashMap<>();
        try {
            //read the properties txt and put the values in the hashmap
            BufferedReader in = new BufferedReader(new FileReader(propertiesFileName));
            String line;
            while((line=in.readLine())!=null) {
                String[] sp = line.split(",");
                properties.put(sp[0],sp[1]);
            }
            in.close();
        } catch (IOException e) {e.printStackTrace();}
    }

    public void clientConnect() {
        try {
            fg = new Socket(properties.get("ip"), Integer.parseInt(properties.get("port")));
            out2fg = new PrintWriter(fg.getOutputStream(),true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setAileron(float x) {
        String command = properties.get("aileron");
        out2fg.println(command+" "+x);
    }
    public void setElevators(float x) {
        String command = properties.get("elevators");
        out2fg.println(command+" "+x);
    }
    public void setRudder(float x) {
        String command = properties.get("rudder");
        out2fg.println(command+" "+x);
    }
    public void setThrottle(float x) {
        String command = properties.get("throttle");
        out2fg.println(command+" "+x);
    }
    public void setBreaks(float x) {
        String command = properties.get("breaks");
        out2fg.println(command+" "+x);
    }

    @Override
    public void finalize() {
        try {
            out2fg.close();
            fg.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
