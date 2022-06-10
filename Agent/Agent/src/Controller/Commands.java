package Controller;
import Model.Model;
import View.View;
import necessary_classes.Properties;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class Commands {

    //the shared state of all commands
    private class SharedSate {
        HashMap<String,String> properties;
        BufferedReader inFromBack;
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
            sharedSate.objectOutputStream.writeObject(sharedSate.m.getFlight());
        }
    }

    public class endFlightCommand implements Command {
        @Override
        public void execute(String input) throws IOException {
            sharedSate.m.endFlight();
        }
    }
    
}
