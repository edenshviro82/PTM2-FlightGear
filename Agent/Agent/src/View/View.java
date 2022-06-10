package View;

import Controller.Commands;
import necessary_classes.Properties;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.Observable;

public class View extends Observable {
    private String Command;
    PrintWriter out2client;

    void runViewServer() {
        try {
            ServerSocket server = new ServerSocket(Integer.parseInt(Properties.map.get("backend_port")));
            System.out.println("Backend server is open");
            server.setSoTimeout(1000);
            String line;
            while(true) {
                try {
                    Socket client = server.accept();
                    System.out.println("Client is connected to my service");
                    BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    OutputStream out = client.getOutputStream();
                    out2client = new PrintWriter(out);
                    line = in.readLine();
                    System.out.println(line); // for debug
                    this.setCommand(line);
                    this.setChanged();
                    this.notifyObservers();
                }
                catch(SocketTimeoutException e) { }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getCommand() { return Command; }
    public void setCommand(String command) { Command = command; }

    public PrintWriter getOut2client() { return out2client; }
}
