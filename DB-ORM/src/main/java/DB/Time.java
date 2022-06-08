package DB;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Time {
    private String id;
    private Boolean timeSeries;

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "timeSeries")
    public Boolean getTimeSeries() {
        return timeSeries;
    }

    public void setTimeSeries(Boolean timeSeries) {
        this.timeSeries = timeSeries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Time time = (Time) o;
        return Objects.equals(id, time.id) && Objects.equals(timeSeries, time.timeSeries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, timeSeries);
    }
}
