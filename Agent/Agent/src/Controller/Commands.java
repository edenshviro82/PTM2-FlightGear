package Controller;

import Model.Model;
import View.View;
import necessary_classes.FlightData;
import necessary_classes.Plane;
import necessary_classes.Properties;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

public class Commands {

    //the shared state of all commands
    private class SharedSate {
        PrintWriter out2back;
        ObjectOutputStream objectOutputStream;
        Model m;
        View v;

        public SharedSate(Model m,View v) {
            this.m = m;
            this.v = v;
        }
    }

    //commands data members
    private SharedSate sharedSate;

    //commands constructor
    public Commands(Model m,View v) {
        this.sharedSate = new SharedSate(m,v);
    }

    public void setOutputStream(OutputStream out) throws IOException {
        this.sharedSate.out2back = new PrintWriter(out,true);
        this.sharedSate.objectOutputStream = new ObjectOutputStream(out);
    }

    //commands implementation//////////////////////////////////////////////////////////////
    //set commands
    public class setAileronCommand implements Command {
        @Override
        public void execute(String input) throws IOException {
            sharedSate.m.setAileron(Float.parseFloat(input.split(" ")[2]));
        }
    }

    public class setRudderCommand implements Command {
        @Override
        public void execute(String input) throws IOException {
            sharedSate.m.setRudder(Float.parseFloat(input.split(" ")[2]));
        }
    }

    public class setThrottleCommand implements Command {
        @Override
        public void execute(String input) throws IOException {
            sharedSate.m.setThrottle(Float.parseFloat(input.split(" ")[2]));
        }
    }

    public class setElevatorsCommand implements Command {
        @Override
        public void execute(String input) throws IOException {
            sharedSate.m.setElevators(Float.parseFloat(input.split(" ")[2]));
        }
    }

    public class setBreaksCommand implements Command {
        @Override
        public void execute(String input) throws IOException {
            sharedSate.m.setBrakes(Float.parseFloat(input.split(" ")[2]));
        }
    }

    //get commands//////////////////////////////////////////////////////////////////////////
    public class getAileronCommand implements Command {
        @Override
        public void execute(String input) throws IOException {
            sharedSate.out2back.println(sharedSate.m.getAileron());
        }
    }

    public class getRudderCommand implements Command {
        @Override
        public void execute(String input) throws IOException {
            sharedSate.out2back.println(sharedSate.m.getRudder());
        }
    }

    public class getThrottleCommand implements Command {
        @Override
        public void execute(String input) throws IOException {
            sharedSate.out2back.println(sharedSate.m.getThrottle());
        }
    }

    public class getBreaksCommand implements Command {
        @Override
        public void execute(String input) throws IOException {
            sharedSate.out2back.println(sharedSate.m.getBrakes());
        }
    }

    public class getElevatorsCommand implements Command {
        @Override
        public void execute(String input) throws IOException {
            sharedSate.out2back.println(sharedSate.m.getElevators());
        }
    }

    public class getAltCommand implements Command {
        @Override
        public void execute(String input) throws IOException {
            sharedSate.out2back.println(sharedSate.m.getAlt());
        }
    }

    public class getHeadingCommand implements Command {
        @Override
        public void execute(String input) throws IOException {
            sharedSate.out2back.println(sharedSate.m.getHeading());
        }
    }

    public class getAirSpeedCommand implements Command {
        @Override
        public void execute(String input) throws IOException {
            sharedSate.out2back.println(sharedSate.m.getAirSpeed());
        }
    }

    public class getRollCommand implements Command {
        @Override
        public void execute(String input) throws IOException {
            sharedSate.out2back.println(sharedSate.m.getRoll());
        }
    }

    public class getPitchCommand implements Command {
        @Override
        public void execute(String input) throws IOException {
            sharedSate.out2back.println(sharedSate.m.getPitch());
        }
    }

    public class getStreamCommand implements Command {
        @Override
        public void execute(String input) throws IOException {
            sharedSate.out2back.println(sharedSate.m.getStream());
        }
    }

    public class getLocationCommand implements Command {
        @Override
        public void execute(String input) throws IOException, ClassNotFoundException {
            sharedSate.objectOutputStream.writeObject(sharedSate.m.getLocation());
        }
    }

    public class getFlightCommand implements Command {
        @Override
        public void execute(String input) throws IOException, ClassNotFoundException {
            FlightData flight = (FlightData) sharedSate.m.getFlight();
            flight.setPlaneId(Properties.map.get("planeId"));
            flight.setFlightId(Properties.map.get("flightId"));
            System.out.println(flight.getPlaneId());
            System.out.println(Properties.map.get("planeId"));
            sharedSate.objectOutputStream.writeObject(flight);
        }
    }

    public class getPlaneCommand implements Command {
        @Override
        public void execute(String input) throws IOException, ClassNotFoundException {
            Plane plane = (Plane) sharedSate.m.getPlane();
            plane.setPlainId(Properties.map.get("plainId"));
            plane.setFlightID(Properties.map.get("flightId"));
            sharedSate.objectOutputStream.writeObject(plane);
        }
    }

    public class startFlightCommand implements Command {

        @Override
        public void execute(String input)  {
            sharedSate.m.startFlight();
        }
    }

    public class endFlightCommand implements Command {

        @Override
        public void execute(String input) {
            sharedSate.m.endFlight();
        }
    }


    //view commands////////////////////////////////////////////////////////////////////////////
    public class viewPrintStreamCommand implements Command {
        PrintWriter out;

        public viewPrintStreamCommand(PrintWriter out) { this.out = out; }

        @Override
        public void execute(String text) throws IOException {
            //get current time in milliseconds
            long start = System.currentTimeMillis();
            //get duration from properties txt file
            long duration = Long.parseLong(Properties.map.get("view_print_stream_duration(secs)")) *1000;
            //send the stream to the client
            while (System.currentTimeMillis() - start < duration)
                out.println(sharedSate.m.getStream());
        }
    }

    public class viewCLI implements Command {
        HashMap <String,Command> map;
        PrintWriter out;
        Commands c;

        public viewCLI(Socket client) throws IOException {
            this.out = new PrintWriter(client.getOutputStream());
            this.map = new HashMap<String, Command>();

            map.put("set aileron", new setAileronCommand());
            map.put("set elevators", new setElevatorsCommand());
            map.put("set throttle", new setThrottleCommand());
            map.put("set rudder", new setRudderCommand());
            map.put("set brakes", new setBreaksCommand());
            map.put("print stream", new viewPrintStreamCommand(this.out));
        }

        @Override
        public void execute(String text) throws IOException, ClassNotFoundException {
            System.out.println("from view: " + text);
            String[] split = text.split(" ");
            String command = split.length > 1? split[0] + " " + split[1] : split[0];
            if (this.map.containsKey(command))
                map.get(command).execute(text);
        }
    }
}
