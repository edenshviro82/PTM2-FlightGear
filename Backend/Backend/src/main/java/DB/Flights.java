package DB;

import javax.persistence.*;
import java.sql.Date;
import java.util.Arrays;
import java.util.Objects;

@Entity
@NamedQuery(name = "getFlights",query = "select e from Flights e")

public class Flights {
    private Date startTime;
    private Double maxSpeed;
    private Double maxAlitude;
    private Date endTime;
    private byte[] flyFrom;
    private byte[] flyTo;
    private String flightid;
    private String planeid;
    private Double miles;

    @Basic

    @Column(name = "startTime")
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
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
    @Column(name = "maxAlitude")
    public Double getMaxAlitude() {
        return maxAlitude;
    }

    public void setMaxAlitude(Double maxAlitude) {
        this.maxAlitude = maxAlitude;
    }

    @Basic
    @Column(name = "endTime")
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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
    @Column(name = "flightid")
    public String getFlightid() {
        return flightid;
    }

    public void setFlightid(String flightid) {
        this.flightid = flightid;
    }

    @Basic
    @Column(name = "planeid")
    public String getPlaneid() {
        return planeid;
    }

    public void setPlaneid(String planeid) {
        this.planeid = planeid;
    }

    @Basic
    @Column(name = "miles")
    public Double getMiles() {
        return miles;
    }

    public void setMiles(Double miles) {
        this.miles = miles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flights flights = (Flights) o;
        return Objects.equals(startTime, flights.startTime) && Objects.equals(maxSpeed, flights.maxSpeed) && Objects.equals(maxAlitude, flights.maxAlitude) && Objects.equals(endTime, flights.endTime) && Arrays.equals(flyFrom, flights.flyFrom) && Arrays.equals(flyTo, flights.flyTo) && Objects.equals(flightid, flights.flightid) && Objects.equals(planeid, flights.planeid) && Objects.equals(miles, flights.miles);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(startTime, maxSpeed, maxAlitude, endTime, flightid, planeid, miles);
        result = 31 * result + Arrays.hashCode(flyFrom);
        result = 31 * result + Arrays.hashCode(flyTo);
        return result;
    }

    @Override
    public String toString() {
        return "Flights{" +
                "startTime=" + startTime +
                ", maxSpeed=" + maxSpeed +
                ", maxAlitude=" + maxAlitude +
                ", endTime=" + endTime +
                ", flyFrom=" + Arrays.toString(flyFrom) +
                ", flyTo=" + Arrays.toString(flyTo) +
                ", flightid='" + flightid + '\'' +
                ", planeid='" + planeid + '\'' +
                ", miles=" + miles +
                '}';
    }
}
