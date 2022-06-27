package algorithms;
import java.util.Collection;

public class Circle {

    private static final double EPSILON = 1 + 1e-14;


    public final Point center;
    public final double radius;


    public Circle(Point c, double r) {
        this.center = c;
        this.radius = r;
    }


    public boolean contains(Point p) {
        return center.distance(p) <= radius * EPSILON;
    }


    public boolean contains(Collection<Point> ps) {
        for (Point p : ps) {
            if (!contains(p))
                return false;
        }
        return true;
    }


    public String toString() {
        return String.format("Circle(x=%g, y=%g, r=%g)", center.x, center.y, radius);
    }


}