package view;

import command.Command;
import controller.Controller;
import model.Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Observable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class View extends Observable  implements viewIF{

    private String Command;
    Socket waiting;
    Socket connected;

    public View() {
        new Thread(this::runViewServer).start();
    }

    public void runViewServer() {
        try {
            ServerSocket server = new ServerSocket(4040);
            System.out.println("view: waiting for backend view to connect...");
            server.setSoTimeout(1000);
            while(true) {
                try {
                    this.waiting = server.accept();
                    System.out.println("backend view is connected to my service");
                    BufferedReader in = new BufferedReader(new InputStreamReader(waiting.getInputStream()));
                    //PrintWriter out = new PrintWriter(waiting.
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





















    @Override
    public void shutDown() {


    }

    @Override
    public void reset() {

    }

    @Override
    public void listOfTasks() {


    }

    @Override
    public void listOfThreds() {

    }

    @Override
    public void listOfActiveAgents() {

    }
}
