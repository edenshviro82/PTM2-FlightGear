package Controller;
import Model.Model;
import View.View;
import necessary_classes.Location;
import necessary_classes.Properties;
import java.io.IOException;


public class Main {

        public static void main(String[] args) throws IOException, InterruptedException {
                new Properties("properties.txt");
                Model m = new Model();
                View v = new View();
                Controller c = new Controller(m,v);
                c.runBackendServer();


//                m.setAileron(1);
//                m.setElevators(1);
//                m.setRudder(1);
//                m.setThrottle(1);
//                m.setBrakes(1);
//                Thread.sleep(10000);
//                System.out.println("alt: " + m.getAlt());
//                System.out.println("aileron: " + m.getAileron());
//                System.out.println("throttle: " + m.getThrottle());
//                System.out.println("rudder: " + m.getRudder());
//                System.out.println("pitch: " + m.getPitch());
//                System.out.println("brakes: " + m.getBrakes());
//                System.out.println("elevators: " + m.getElevators());
//                System.out.println("heading: " + m.getHeading());
//                System.out.println("air speed: " + m.getAirSpeed());
//                System.out.println("roll: " + m.getRoll());
//                System.out.println(m.getStream());
//                try {
//                        Location loc = m.getLocation();
//                        System.out.println("location: " + loc.getLatitude() + ", " + loc.getLongitude());
////                        System.out.println("flight:\n" + m.getFlight());
//                } catch (ClassNotFoundException e) {
//                        e.printStackTrace();
//                }
        }
}
