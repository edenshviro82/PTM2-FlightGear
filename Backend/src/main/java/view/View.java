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
    public PrintWriter printWriterView ;
    public View() {
        new Thread(this::runViewServer).start();
    }

    public void runViewServer() {
        try {

            ServerSocket server = new ServerSocket(5090);
            System.out.println("view: waiting for backend view to connect...");
            server.setSoTimeout(1000);
            while (Controller.run) {
                try {
                    this.waiting = server.accept();
                    System.out.println("backend view is connected to my service");
                    BufferedReader in = new BufferedReader(new InputStreamReader(waiting.getInputStream()));
                    this.printWriterView = new PrintWriter(waiting.getOutputStream(), true);
                    while (Controller.run) {
                        String inCommand = in.readLine();
                        this.setCommand(inCommand);
                        this.connected = waiting;
                        this.setChanged();
                        this.notifyObservers();
                    }
                    }
                catch(SocketTimeoutException e){
                    }
                }
            } catch(IOException e){
                e.printStackTrace();
            }

    }


    public Socket getConnected() { return connected; }

    public String getCommand() {return Command;}
    public void sendResponse (String response) {
        this.printWriterView.println(response);
    }

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
