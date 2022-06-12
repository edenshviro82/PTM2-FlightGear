package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;

public class Server {
    ServerSocket server;
    Socket fg;
    BufferedReader in;
    volatile boolean stop;

    public Server() {
        this.stop = false;
    }

    public void runServer() {
        try {
            server = new ServerSocket(5400);
            System.out.println("server is open");
            server.setSoTimeout(1000);
            while(!stop) {
                try {
                    Socket client = server.accept();
                    System.out.println("simulator is connected to my server");
                    in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    while(!stop) {
                        String[] line = in.readLine().split(",");
                        System.out.println(line.length);
                        for (int i=0; i<line.length; i++) {
                            System.out.print(line[i]+",");
                        }
                        System.out.println();
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                catch(SocketTimeoutException e) { }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
