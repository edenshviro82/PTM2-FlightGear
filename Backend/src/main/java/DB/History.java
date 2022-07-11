package DB;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

@Entity
@NamedQuery(name = "getHistory",query = "select e from History e")
public class History {
    private String planeid;
    private byte[] timeseries;
    private String flightid;

    @Basic
    @Column(name = "planeid")
    public String getPlaneid() {
        return planeid;
    }

    public void setPlaneid(String planeid) {
        this.planeid = planeid;
    }

    @Basic
    @Column(name = "timeseries")
    public byte[] getTimeseries() {
        return timeseries;
    }

    public void setTimeseries(byte[] timeseries) {
        this.timeseries = timeseries;
    }


    @Id
    @Column(name = "flightid")
    public String getFlightid() {
        return flightid;
    }

    public void setFlightid(String flightid) {
        this.flightid = flightid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        History history = (History) o;
        return Objects.equals(planeid, history.planeid) && Arrays.equals(timeseries, history.timeseries) && Objects.equals(flightid, history.flightid);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(planeid, flightid);
        result = 31 * result + Arrays.hashCode(timeseries);
        return result;
    }

    @Override
    public String toString() {
        return "History{" +
                "planeid='" + planeid + '\'' +
                ", timeseries=" + Arrays.toString(timeseries) +
                ", flightid='" + flightid + '\'' +
                '}';
    }
}
