package necessary_classes;

import java.io.Serializable;

public class Plane implements Serializable {
    public String plainId;
    public String flightID;
    public double heading;
    public double alt;
    public double speed;
    public Location location;

    public String getPlainId() {
        return plainId;
    }

    public void setPlainId(String plainId) {
        this.plainId = plainId;
    }

    public String getFlightID() {
        return flightID;
    }

    public void setFlightID(String flightID) {
        this.flightID = flightID;
    }

    public double getHeading() {
        return heading;
    }

    public void setHeading(double heading) {
        this.heading = heading;
    }

    public double getAlt() {
        return alt;
    }

    public void setAlt(double alt) {
        this.alt = alt;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
