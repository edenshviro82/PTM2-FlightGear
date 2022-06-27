package algorithms;

public class Point {
    public final float x,y;
    public Point(float x, float y) {
        this.x=x;
        this.y=y;
    }

    public float distance(Point other) {
        return (float) Math.sqrt(Math.pow((this.x - other.x),2) + Math.pow((this.y - other.y),2));
    }

    public Point subtract(Point p) {
        return new Point(x - p.x, y - p.y);
    }

    public float cross(Point p) {
        return x * p.y - y * p.x;
    }


    public String toString() {
        return String.format("Point(%g, %g)", x, y);
    }
}
