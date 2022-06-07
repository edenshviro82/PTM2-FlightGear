package model;

import java.util.Vector;

public class StatLib {

	// simple average
	public static float avg(float[] x) {
		float sum=0;
		for (int i=0; i<x.length; i++)
			sum+=x[i];
		return sum/x.length;
	}

	// returns the variance of X and Y
	public static float var(float[] x) {
		float u = avg(x);  //value of expectancy
		float Var=0;
		for (int i = 0; i < x.length; i++)
			Var+= x[i]*x[i];
		Var/=x.length;
		return Var-(u*u);
	}

	// returns the covariance of X and Y
	public static float cov(float[] x, float[] y) {
		float avgX = avg(x);
		float avgY = avg(y);
		float avgXY=0;
		for (int i=0;i<x.length;i++)
			avgXY+=x[i]*y[i];

		avgXY/=x.length;
		float Cov = avgXY-(avgY * avgX);
		return Cov;
	}

	// returns the Pearson correlation coefficient of X and Y
	public static float pearson(float[] x, float[] y){
		return (float) (cov(x,y)/(Math.sqrt(var(x))*Math.sqrt(var(y))));
	}

	// performs a linear regression and returns the line equation
	public static Line linear_reg(Point[] points) {
		float[] arrX = new float[points.length];
		float[] arrY = new float[points.length];
		float lineA,lineB,avgX,avgY;
		for (int i =0; i< points.length; i++) {
			arrX[i] = points[i].x;
			arrY[i] = points[i].y;
		}
		avgX = avg(arrX);
		avgY = avg(arrY);
		lineA = cov(arrX,arrY) / var(arrX);
		lineB = avgY-lineA*avgX;
		Line eq = new Line(lineA,lineB);
		return  eq;
	}

	// returns the deviation between point p and the line equation of the points
	public static float dev(Point p,Point[] points) {
		return dev(p,linear_reg(points));
	}

	// returns the deviation between point p and the line
	public static float dev(Point p,Line l) {
		return Math.abs(p.y-l.f(p.x));
	}
}