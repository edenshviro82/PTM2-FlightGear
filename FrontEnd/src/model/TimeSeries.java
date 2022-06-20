package model;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;

public class TimeSeries {
	Vector [] _table;

	public TimeSeries(String csvFileName) {
		try {
			File file = new File(csvFileName);
			Scanner inputStream = new Scanner(file);
			//inputStream.next(); ------> to ignore the first line
			String headLines = inputStream.next();
			String[] colNames = headLines.split(",");
			this._table = new Vector[colNames.length];
			for (int i=0; i<colNames.length;i++)
			{
				this._table[i] = new Vector();
				this._table[i].add(colNames[i]);
			}
			while (inputStream.hasNext())
			{
				String data = inputStream.next();
				String[] values = data.split(",");

				for (int i=0; i< values.length;i++) {
					Float relevantValue = Float.parseFloat(values[i]); //-------> cast the sting of values to double
					this._table[i].add(relevantValue);
				}
			}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	public float [] getFloatArray(int index) {
		if (index<0 || index>this._table.length)
			return null;

		float[] array = new float[_table[index].size()-1];
		for (int i=1; i<_table[index].size();i++)
			array[i-1] = (float) _table[index].get(i);

		return array;
	}
	public String getColName (int index) {
		return (String) this._table[index].get(0);
	}

	public Point [] getPointsArray(int index1,int index2) {
		float [] arrayX = this.getFloatArray(index1);
		float [] arrayY = this.getFloatArray(index2);
		if (arrayX == null || arrayY == null)
			return  null;
		Point[] pointarray = new Point[arrayX.length];
		for (int i=0; i<pointarray.length; i++)
		{
			pointarray[i] = new Point(arrayX[i],arrayY[i]);
		}
		return pointarray;
	}

	int getColIndexByName(String name) {
		for (int i=0;i<this._table.length; i++) {
			if (this._table[i].get(0).equals(name))
				return i;
		}
		return -1;
	}

}
