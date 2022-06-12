package DB;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@NamedQuery(name = "getPlane",query = "select e from Plane e")
public class Plane {
    private String planeid;
    private Date firstflight;

    @Id
    @Column(name = "planeid")
    public String getPlaneid() {
        return planeid;
    }

    public void setPlaneid(String planeid) {
        this.planeid = planeid;
    }

    @Basic
    @Column(name = "firstflight")
    public Date getFirstflight() {
        return firstflight;
    }

    public void setFirstflight(Date firstflight) {
        this.firstflight = firstflight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plane plane = (Plane) o;
        return Objects.equals(planeid, plane.planeid) && Objects.equals(firstflight, plane.firstflight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(planeid, firstflight);
    }

    @Override
    public String toString() {
        return "Plane{" +
                "planeid='" + planeid + '\'' +
                ", firstflight=" + firstflight +
                '}';
    }
}
