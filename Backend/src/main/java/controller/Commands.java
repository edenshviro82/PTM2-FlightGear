package controller;

import command.Command;
import model.Model;
import necessary_classes.FlightData;
import view.View;
import java.io.*;

public class Commands {
    //the shared state of all commands
    private class SharedSate {
        //agent
        PrintWriter out2agent;
        BufferedReader inFromAgent;
        ObjectInputStream objectInputStream;

        //front
        PrintWriter out2front;
        BufferedReader inFromFront;
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

    //commands implementation//////////////////////////////////////////////////////////////
    //set commands
    public class setAileronCommand implements Command {
        @Override
        public void execute(String input) throws IOException {
            sharedSate.out2agent.println(input);
        }
    }

    public class setRudderCommand implements Command {
        @Override
        public void execute(String input) throws IOException {
            sharedSate.out2agent.println(input);
        }
    }

    public class setThrottleCommand implements Command {
        @Override
        public void execute(String input) throws IOException {
            sharedSate.out2agent.println(input);
        }
    }

    public class setBrakesCommand implements Command {
        @Override
        public void execute(String input) throws IOException {
            sharedSate.out2agent.println(input);
        }
    }

    public class setElevatorCommand implements Command {
        @Override
        public void execute(String input) throws IOException {
            sharedSate.out2agent.println(input);
        }
    }

    //get commands//////////////////////////////////////////////////////////////////////////
    public class getAileronCommand implements Command {
        @Override
        public void execute(String input) throws IOException {
            sharedSate.out2agent.println(input);
            sharedSate.out2front.println(sharedSate.inFromAgent.readLine());
        }
    }

    public class getLocationCommand implements Command {
        @Override
        public void execute(String input) throws IOException, ClassNotFoundException {
            sharedSate.out2agent.println(input);
            sharedSate.objectOutputStream.writeObject(sharedSate.objectInputStream.readObject());
        }
    }

    public class getRudderCommand implements Command {
        @Override
        public void execute(String input) throws IOException {
            sharedSate.out2agent.println(input);
            sharedSate.out2front.println(sharedSate.inFromAgent.readLine());
        }
    }

    public class getThrottleCommand implements Command {
        @Override
        public void execute(String input) throws IOException {
            sharedSate.out2agent.println(input);
            sharedSate.out2front.println(sharedSate.inFromAgent.readLine());
        }
    }

    public class getBrakesCommand implements Command {
        @Override
        public void execute(String input) throws IOException {
            sharedSate.out2agent.println(input);
            sharedSate.out2front.println(sharedSate.inFromAgent.readLine());
        }
    }

    public class getElevatorCommand implements Command {
        @Override
        public void execute(String input) throws IOException {
            sharedSate.out2agent.println(input);
            sharedSate.out2front.println(sharedSate.inFromAgent.readLine());
        }
    }

    public class getDataStreamCommand implements Command {
        @Override
        public void execute(String input) throws IOException, ClassNotFoundException {
            sharedSate.out2agent.println(input);
            sharedSate.objectOutputStream.writeObject(sharedSate.objectInputStream.readObject());
        }
    }

    public class getPlanePositionCommand implements Command {
        @Override
        public void execute(String input) throws IOException, ClassNotFoundException {
            sharedSate.out2agent.println(input);
            sharedSate.objectOutputStream.writeObject(sharedSate.objectInputStream.readObject());
        }
    }

    public class getFlightDataCommand implements Command {
        @Override
        public void execute(String input) throws IOException, ClassNotFoundException {
            FlightData flightData = (FlightData) sharedSate.objectInputStream.readObject();
            String pid = "777";
            String fid = "999";
            sharedSate.m.setFinishedFlight(pid , fid , flightData);
        }
    }

    public class getAirspeedCommand implements Command {
        @Override
        public void execute(String input) throws IOException {
            sharedSate.out2agent.println(input);
            sharedSate.out2front.println(sharedSate.inFromAgent.readLine());
        }
    }

    public class getFlightRecordCommand implements Command {
        @Override
        public void execute(String input) throws IOException {
            String str =sharedSate.inFromAgent.readLine();
            sharedSate.objectOutputStream.writeObject(sharedSate.m.getFlightRecord(str));
        }
    }

    public class getMilesPerMonthCommand implements Command {
        @Override
        public void execute(String input) throws IOException {
            int month = Integer.parseInt(sharedSate.inFromAgent.readLine());
            sharedSate.objectOutputStream.writeObject(sharedSate.m.getMilesPerMonth(month));
        }
    }
    public class getMilesPerMonthYearCommand implements Command {
        @Override
        public void execute(String input) throws IOException {
            sharedSate.objectOutputStream.writeObject(sharedSate.m.getMilesPerMonthYear());
        }
    }
    public class isFirstFlightCommand implements Command {
        @Override
        public void execute(String input) throws IOException {
            String str = sharedSate.inFromFront.readLine();
            sharedSate.objectOutputStream.writeObject(sharedSate.m.isFirstFlight(str));
        }
    }

    public class dateFirstFlightCommand implements Command {
        @Override
        public void execute(String input) throws IOException {
            String str = sharedSate.inFromFront.readLine();
            sharedSate.objectOutputStream.writeObject(sharedSate.m.dateFirstFlight(str));
        }
    }

    public class getFleetSizeCommand implements Command {
        @Override
        public void execute(String input) throws IOException {
            sharedSate.out2front.println(sharedSate.m.getFleetsize());
        }
    }

    public class getRollCommand implements Command {
        @Override
        public void execute(String input) throws IOException {
            sharedSate.out2agent.println(input);
            sharedSate.out2front.println(sharedSate.inFromAgent.readLine());
        }
    }

    public class getPitchCommand implements Command {
        @Override
        public void execute(String input) throws IOException {
            sharedSate.out2agent.println(input);
            sharedSate.out2front.println(sharedSate.inFromAgent.readLine());
        }
    }

    public class getAltCommand implements Command {
        @Override
        public void execute(String input) throws IOException {
            sharedSate.out2agent.println(input);
            sharedSate.out2front.println(sharedSate.inFromAgent.readLine());
        }
    }


}