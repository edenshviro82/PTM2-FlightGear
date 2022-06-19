package necessary_classes;

import java.io.Serializable;

public class FlightData implements Serializable {
    private String startTime;
    private String endTime;
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
    public String getStartTime() {
        return startTime;
    }

    //setters///////////////////////////////////////////////////////////////////////
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
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
}
