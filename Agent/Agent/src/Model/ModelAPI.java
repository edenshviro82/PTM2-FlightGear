package Model;

import necessary_classes.FlightData;
import necessary_classes.Location;

import java.io.IOException;

public interface ModelAPI {
    //set methods/////////////////////
    public void setAileron(float value);
    public void setElevators(float value);
    public void setRudder(float value);
    public void setThrottle(float value);
    public void setBreaks(float value);

    //get methods/////////////////////
    public String getAileron() throws IOException;
    public String getElevators() throws IOException;
    public String getRudder() throws IOException;
    public String getThrottle() throws IOException;
    public String getBreaks() throws IOException;
    public String getAlt() throws IOException;
    public String getHeading() throws IOException;
    public String getAirSpeed() throws IOException;
    public String getRoll() throws IOException;
    public String getPitch() throws IOException;
    public Location getLocation() throws IOException, ClassNotFoundException;
    public FlightData getFlight() throws IOException, ClassNotFoundException;
    public String getStream() throws IOException;
    public void endFlight();
}
