package DB;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Arrays;
import java.util.Objects;

@Entity
public class Fly {
    private String startTime;
    private String endTime;
    private Double maxSpeed;
    private Double maxAltitude;
    private byte[] flyFrom;
    private byte[] flyTo;
    private String id;

    @Basic
    @Column(name = "startTime")
    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @Basic
    @Column(name = "endTime")
    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Basic
    @Column(name = "maxSpeed")
    public Double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(Double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    @Basic
    @Column(name = "maxAltitude")
    public Double getMaxAltitude() {
        return maxAltitude;
    }

    public void setMaxAltitude(Double maxAltitude) {
        this.maxAltitude = maxAltitude;
    }

    @Basic
    @Column(name = "flyFrom")
    public byte[] getFlyFrom() {
        return flyFrom;
    }

    public void setFlyFrom(byte[] flyFrom) {
        this.flyFrom = flyFrom;
    }

    @Basic
    @Column(name = "flyTo")
    public byte[] getFlyTo() {
        return flyTo;
    }

    public void setFlyTo(byte[] flyTo) {
        this.flyTo = flyTo;
    }

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fly fly = (Fly) o;
        return Objects.equals(startTime, fly.startTime) && Objects.equals(endTime, fly.endTime) && Objects.equals(maxSpeed, fly.maxSpeed) && Objects.equals(maxAltitude, fly.maxAltitude) && Arrays.equals(flyFrom, fly.flyFrom) && Arrays.equals(flyTo, fly.flyTo) && Objects.equals(id, fly.id);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(startTime, endTime, maxSpeed, maxAltitude, id);
        result = 31 * result + Arrays.hashCode(flyFrom);
        result = 31 * result + Arrays.hashCode(flyTo);
        return result;
    }
}
