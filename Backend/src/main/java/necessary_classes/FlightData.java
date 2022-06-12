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
    public String getStartTime() { return startTime; }
    public String getEndTime() {
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
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public void setEndTime(String endTime) {
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
