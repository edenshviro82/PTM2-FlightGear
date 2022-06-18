package model;

import necessary_classes.FlightData;
import necessary_classes.TimeSeries;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public interface Model_api {
    public Map<String,Double > getMilesPerMonth(int month);
    public HashMap<Integer,Double> getMilesPerMonthYear();
    public HashMap<Integer, Integer> getFleetSize (int month);
    public TimeSeries getFlightRecord (String id) throws IOException, ClassNotFoundException;
    public void setFlightData (String fid , String pid , byte[]ts);
    public void setFinishedFlight( FlightData flightData) throws IOException;
    public boolean isFirstFlight(String pid);
    public Date dateFirstFlight(String pid);
}
