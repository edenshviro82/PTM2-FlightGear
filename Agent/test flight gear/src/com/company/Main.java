package com.company;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Main {

    public static void main(String[] args) {
//        Client c = new Client("properties.txt");
//        c.clientConnect();
//        c.setAileron(1);
//        c.setBreaks(1);
//        c.setElevators(1);
//        c.setRudder(1);
//        try {Thread.sleep(3000);} catch (InterruptedException ignored) { }
//        c.setThrottle(1);
//        c.setAileron(-1);
//        c.setBreaks(-1);
//        c.setElevators(-1);
//        c.setRudder(-1);
//        c.setThrottle(-1);
        Server s = new Server();
        s.runServer();
    }
}
