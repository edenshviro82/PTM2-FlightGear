package FGServer;

public class Main {
    public static void main (String[] args) {
        new Properties("FGServer_properties.txt");
        FGServer main = new FGServer();
        main.runModelServer();
    }
}
