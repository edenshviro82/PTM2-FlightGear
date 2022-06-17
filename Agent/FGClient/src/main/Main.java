package main;

import java.io.File;
import java.net.URL;

public class Main {

    public static void main(String[] args) {
        Client FGClient = new Client("properties.txt");
        FGClient server = new FGClient(FGClient);
        server.runServer();
    }
}
