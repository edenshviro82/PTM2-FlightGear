package necessary_classes;

import java.io.Serializable;
import java.sql.Date;

//flight data object contains all the information we want to collect from the simulator, at the end of flight
//the object will be sent to backend
public class FlightData implements Serializable {
    private Date startTime;
    private Date endTime;
    private float maxSpeed;
    private float maxAltitude;
    private double miles;
    private Location flyFrom;
    private Location flyTo;
    private TimeSeries ts;
    private String planeId;
    private String flightId;

    public FlightData() {
        this.ts = new TimeSeries();
        this.miles = 0;
    }

    //getters/////////////////////////////////////////////////////////////////////////
    public Date getStartTime() {
        return startTime;
    }

    //setters///////////////////////////////////////////////////////////////////////
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public float getMaxAltitude() {
        return maxAltitude;
    }

    public void setMaxAltitude(float maxAltitude) {
        this.maxAltitude = maxAltitude;
    }

    public double getMiles() {
        return miles;
    }

    public void setMiles(double miles) {
        this.miles = miles;
    }

    public Location getFlyFrom() {
        return flyFrom;
    }

    public void setFlyFrom(Location flyFrom) {
        this.flyFrom = flyFrom;
    }

    public Location getFlyTo() {
        return flyTo;
    }

    public void setFlyTo(Location flyTo) {
        this.flyTo = flyTo;
    }

    public TimeSeries getTs() {
        return ts;
    }

    public void setTs(TimeSeries ts) {
        this.ts = ts;
    }

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
}
