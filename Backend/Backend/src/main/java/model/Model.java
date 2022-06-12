package model;

import command.Command;
import necessary_classes.FlightData;
import necessary_classes.TimeSeries;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

public class Model extends Observable implements Model_api {
    private dbhandler db ;

    public Model() {
        this.db =new dbhandler();
    }

    @Override
    public int getActivePlanes() {

        return db.getActivePlanes();
    }

    @Override
    public Map<String, Double> getMilesPerMonth(int month) {
        return db.getMilesPerMonth(month);
    }

    @Override
    public HashMap<Integer, Double> getMilesPerMonthYear() {
        return db.getMilesPerMonthYear();
    }

    @Override
    public double getFleetsize() {
        return db.getFleetsize();
    }

    @Override
    public void getPlaneproper() {
        db.getPlaneproper();
    }

    @Override
    public TimeSeries getFlightRecord(String id) throws IOException, ClassNotFoundException {
        return db.getFlightRecord(id);
    }

    @Override
    public void setFlightData(String fid, String pid, byte[] ts) {
        db.setFlightData(fid,pid,ts);
    }

    @Override
    public void setFinishedFlight(String pid , String fid , FlightData flightData) throws IOException {
        db.setFinishedFlight(pid, fid, flightData);
    }

    @Override
    public boolean isFirstFlight(String pid) {
        return db.isFirstFlight(pid);
    }

    @Override
    public Date dateFirstFlight(String pid) {
        return db.dateFirstFlight(pid);
    }
}
