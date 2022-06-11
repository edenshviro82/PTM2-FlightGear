package Controller;
import Model.Model;
import View.View;
import necessary_classes.FlightData;
import necessary_classes.Properties;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

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
            sharedSate.objectOutputStream.writeObject(sharedSate.m.getFlight());
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
    public class viewSetAileronCommand extends ViewCommand {

        public viewSetAileronCommand(String name) {super(name);}

        @Override
        public void execute(String text) throws IOException {
            sharedSate.m.setAileron(Float.parseFloat(text));
        }
    }

    public class viewSetThrottleCommand extends ViewCommand {

        public viewSetThrottleCommand(String name) {super(name);}

        @Override
        public void execute(String text) throws IOException {
            sharedSate.m.setThrottle(Float.parseFloat(text));
        }
    }

    public class viewSetElevatorsCommand extends ViewCommand {

        public viewSetElevatorsCommand(String name) {super(name);}

        @Override
        public void execute(String text) throws IOException {
            sharedSate.m.setElevators(Float.parseFloat(text));
        }
    }

    public class viewSetBrakesCommand extends ViewCommand {

        public viewSetBrakesCommand(String name) {super(name);}

        @Override
        public void execute(String text) throws IOException {
            sharedSate.m.setBrakes(Float.parseFloat(text));
        }
    }

    public class viewSetRudderCommand extends ViewCommand {

        public viewSetRudderCommand(String name) {super(name);}

        @Override
        public void execute(String text) throws IOException {
            sharedSate.m.setRudder(Float.parseFloat(text));
        }
    }

    public class viewPrintStreamCommand extends ViewCommand {
        PrintWriter out;

        public viewPrintStreamCommand(String name, PrintWriter out) {
            super(name);
            this.out = out;
        }

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
        HashMap <Integer,ViewCommand> map;
        Socket aClient;
        Scanner in;
        PrintWriter out;
        Commands c;

        public viewCLI(Socket client) throws IOException {
            this.out = new PrintWriter(aClient.getOutputStream());
            this.in = new Scanner(aClient.getInputStream());
            this.map = new HashMap<Integer, ViewCommand>();
            map.put(1,c.new viewSetAileronCommand("Set Aileron"));
            map.put(2,c.new viewSetElevatorsCommand("Set Elevators"));
            map.put(3,c.new viewSetThrottleCommand("Set Throttle"));
            map.put(4, c.new viewSetRudderCommand("Set Rudder"));
            map.put(5, c.new viewSetBrakesCommand("Set Brakes"));
            map.put(6,c.new viewPrintStreamCommand("Print stream",this.out));
        }

        @Override
        public void execute(String text) throws IOException {
            out.print("hello and welcome to Flight Gear Agent's view!\n");
            out.print("please enter the number of the command you want to execute\n");
            map.forEach((integer, viewCommand) -> out.print(integer + ". \t" + viewCommand.name + "\n"));
            int choice = in.nextInt();
            if (choice < 5) {
                out.println("please enter the value you want to sed (valid value is between -1 to 1)");
                map.get(choice).execute(in.next());
            }
            else
                map.get(choice).execute(text);
            aClient.close();
        }
    }
}
