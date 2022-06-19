package necessary_classes;

import java.io.Serializable;

public class Location implements Serializable {
    float longitude;
    float latitude;

    //constructors////////////////////////////////////////////////////////////
    public Location() {
    }

    public Location(float longitude, float latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    //getters////////////////////////////////////////////////////////////////
    public float getLongitude() {
        return longitude;
    }

    //setters////////////////////////////////////////////////////////////////
    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }
}