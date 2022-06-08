package DB;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Plane {
    private String id;
    private Double qof;
    private String rdate;

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "QOF")
    public Double getQof() {
        return qof;
    }

    public void setQof(Double qof) {
        this.qof = qof;
    }

    @Basic
    @Column(name = "Rdate")
    public String getRdate() {
        return rdate;
    }

    public void setRdate(String rdate) {
        this.rdate = rdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plane plane = (Plane) o;
        return Objects.equals(id, plane.id) && Objects.equals(qof, plane.qof) && Objects.equals(rdate, plane.rdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, qof, rdate);
    }
}
