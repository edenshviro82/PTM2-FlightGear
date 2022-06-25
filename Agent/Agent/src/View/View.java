package View;

import necessary_classes.Properties;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Observable;

public class View extends Observable {
    Socket waiting;
    Socket connected;
    private String Command;
    public Thread t;

    public View() {
        t = new Thread(this::runViewServer);
        t.start();
    }

    public void runViewServer() {
        try {
            ServerSocket server = new ServerSocket(Integer.parseInt(Properties.map.get("view_client_port")));
            while (true) {
                try {
                    System.out.println("view: waiting for client to connect...");
                    this.waiting = server.accept();
                    System.out.println("Client is connected to my service");
                    BufferedReader in = new BufferedReader(new InputStreamReader(waiting.getInputStream()));
                    this.setCommand(in.readLine());
                    this.connected = waiting;
                    this.setChanged();
                    this.notifyObservers();
                } catch (SocketTimeoutException e) {
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getConnected() {
        return connected;
    }

    public String getCommand() {
        return Command;
    }

    public void setCommand(String command) {
        Command = command;
    }
}
