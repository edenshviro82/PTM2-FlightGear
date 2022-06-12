package model;

import necessary_classes.FlightData;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public interface Model_api {
    public int getActivePlanes();
    public Map<String,Double > getMilesPerMonth(int month);
    public HashMap<Integer,Double> getMilesPerMonthYear();
    public double getFleetsize ();
    public void getPlaneproper();
    public byte [] getFlightRecord (String id);
    public void setFlightData (String fid , String pid , byte[]ts);
    public void setFinishedFlight(String pid , String fid , FlightData flightData) throws IOException;
    public boolean isFirstFlight(String pid);
    public Date dateFirstFlight(String pid);
}
