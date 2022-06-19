package Model;

import necessary_classes.FlightData;
import necessary_classes.Location;
import necessary_classes.Plane;

import java.io.IOException;

public interface ModelAPI {
    //get methods/////////////////////
    public String getAileron() throws IOException;

    //set methods/////////////////////
    public void setAileron(float value);

    public String getElevators() throws IOException;

    public void setElevators(float value);

    public String getRudder() throws IOException;

    public void setRudder(float value);

    public String getThrottle() throws IOException;

    public void setThrottle(float value);

    public String getBrakes() throws IOException;

    public void setBrakes(float value);

    public String getAlt() throws IOException;

    public String getHeading() throws IOException;

    public String getAirSpeed() throws IOException;

    public String getRoll() throws IOException;

    public String getPitch() throws IOException;

    public Location getLocation() throws IOException, ClassNotFoundException;

    public FlightData getFlight() throws IOException, ClassNotFoundException;

    public Plane getPlane() throws IOException, ClassNotFoundException;

    public String getStream() throws IOException;

    public void endFlight();

    public void startFlight();
}
