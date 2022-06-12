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
    Socket waiting;
    Socket connected;

    public View() {
        new Thread(this::runViewServer).start();
    }

    public void runViewServer() {
        try {
            ServerSocket server = new ServerSocket(Integer.parseInt(Properties.map.get("view_client_port")));
            System.out.println("waiting for client to connect...");
            server.setSoTimeout(1000);
            while(true) {
                try {
                    this.waiting = server.accept();
                    System.out.println("Client is connected to my service");
                    BufferedReader in = new BufferedReader(new InputStreamReader(waiting.getInputStream()));
                    this.setCommand(in.readLine());
                    this.connected = waiting;
                    this.setChanged();
                    this.notifyObservers();
                }
                catch(SocketTimeoutException e) { }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getConnected() { return connected; }

    public String getCommand() {return Command;}

    public void setCommand(String command) {Command = command;}
}
