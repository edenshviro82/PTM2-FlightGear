package algorithms;

import java.util.ArrayList;

public class StatLib {

    // simple average
    public static float avg(ArrayList<Float> x) {
        float sum=0;
        for (int i=0; i<x.size(); i++)
            sum+=x.get(i);
        return sum/x.size();
    }

    // returns the variance of X and Y
    public static float var(ArrayList<Float> x) {
        float u = avg(x);  //value of expectancy
        float Var=0;
        for (int i = 0; i < x.size(); i++)
            Var+= x.get(i)*x.get(i);
        Var/=x.size();
        return Var-(u*u);
    }

    // returns the covariance of X and Y
    public static float cov(ArrayList<Float> x, ArrayList<Float> y) {
        float avgX = avg(x);
        float avgY = avg(y);
        float avgXY=0;
        for (int i=0;i<x.size();i++)
            avgXY+=x.get(i)*y.get(i);

        avgXY/=x.size();
        float Cov = avgXY-(avgY * avgX);
        return Cov;
    }

    // returns the Pearson correlation coefficient of X and Y
    public static float pearson(ArrayList<Float> x, ArrayList<Float> y){
        return (float) (cov(x,y)/(Math.sqrt(var(x))*Math.sqrt(var(y))));
    }

    // performs a linear regression and returns the line equation
    public static Line linear_reg(ArrayList<Point> points) {
        ArrayList<Float> arrX = new ArrayList<>();
        ArrayList<Float> arrY = new ArrayList<>();
        float lineA,lineB,avgX,avgY;
        for (int i =0; i< points.size(); i++) {
            arrX.add(points.get(i).x);
            arrY.add(points.get(i).y);
        }
        avgX = avg(arrX);
        avgY = avg(arrY);
        lineA = cov(arrX,arrY) / var(arrX);
        lineB = avgY-lineA*avgX;
        Line eq = new Line(lineA,lineB);
        return  eq;
    }

    // returns the deviation between point p and the line equation of the points
    public static float dev(Point p,ArrayList<Point> points) {
        return dev(p,linear_reg(points));
    }

    // returns the deviation between point p and the line
    public static float dev(Point p,Line l) {
        return Math.abs(p.y-l.f(p.x));
    }
}