package necessary_classes;

import java.io.Serializable;
import java.sql.Date;

public class FlightData implements Serializable {
    private Date startTime;
    private Date endTime;
    private String planeId;
    private String flightId;

    public String getPlaneId() {
        return planeId;
    }

    public void setPlaneId(String planeId) {
        this.planeId = planeId;
    }

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    private float maxSpeed;
    private float maxAltitude;
    private double miles;
    private Location flyFrom;
    private Location flyTo;
    private TimeSeries ts;

    public FlightData() {
        this.ts = new TimeSeries();
        this.miles = 0;
    }

    //getters/////////////////////////////////////////////////////////////////////////
    public java.sql.Date getStartTime() { return startTime; }
    public Date getEndTime() {
        return endTime;
    }
    public float getMaxSpeed() {
        return maxSpeed;
    }
    public float getMaxAltitude() {
        return maxAltitude;
    }
    public double getMiles() { return miles; }
    public Location getFlyFrom() {
        return flyFrom;
    }
    public Location getFlyTo() {
        return flyTo;
    }
    public TimeSeries getTs() {
        return ts;
    }

    //setters///////////////////////////////////////////////////////////////////////
    public void setStartTime(Date startTime) { this.startTime = startTime; }
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }
    public void setMaxAltitude(float maxAltitude) {
        this.maxAltitude = maxAltitude;
    }
    public void setMiles(double miles) { this.miles = miles; }
    public void setTs(TimeSeries ts) {
        this.ts = ts;
    }
    public void setFlyFrom(Location flyFrom) {
        this.flyFrom = flyFrom;
    }
    public void setFlyTo(Location flyTo) {
        this.flyTo = flyTo;
    }
}
