package main;

public class Main {

    public static void main(String[] args) {
        Client FGClient = new Client("FGClient_properties.txt");
        FGClient server = new FGClient(FGClient);
        server.runServer();
    }
}
