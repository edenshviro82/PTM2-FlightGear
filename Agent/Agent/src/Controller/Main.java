package Controller;
import Model.Model;
import View.View;
import necessary_classes.Properties;
import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        new Properties("properties.txt");
        Model m = new Model();
        View v = new View();
        Controller c = new Controller(m, v);
    }
}
