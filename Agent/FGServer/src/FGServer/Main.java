package FGServer;

public class Main {
    public static void main (String[] args) {
        new Properties("properties.txt");
        FGServer main = new FGServer();
        main.runModelServer();
    }
}
