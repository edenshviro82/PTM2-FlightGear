package necessary_classes;

import algorithms.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;
import model.*;

public class TimeSeries implements Serializable {
   
	
    public ArrayList<String> dataStreams;


    public TimeSeries() {
        this.dataStreams = new ArrayList<>();
    }

    public TimeSeries(String csvFileName) {
        try {
            File file = new File(csvFileName);
            Scanner inputStream = new Scanner(file);
            this.dataStreams = new ArrayList<>();
            while (inputStream.hasNext())
                dataStreams.add(inputStream.next());
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getDataStreams() {
        return dataStreams;
    }
    public void setDataStreams(ArrayList<String> dataStreams) {
        this.dataStreams = dataStreams;
    }

    public ArrayList<Float> getColByIndex(int index) {
        ArrayList<Float> arr = new ArrayList<>();
        for (int i=1; i<dataStreams.size(); i++) {
            arr.add(Float.parseFloat(dataStreams.get(i).split(",")[index]));
        }
        return arr;
    }

    public ArrayList<Float> getColByName(String name) {
    	String[] names = this.dataStreams.get(0).split(",");
    	for (int i=0; i<names.length; i++) {
    		if (name.equals(names[i]))
    			return getColByIndex(i);
    	}
    	return null;
    }


    public String getColName (int index) {
        return (String) this.dataStreams.get(0).split(",")[index];
    }

    public ArrayList<Point> getPointsArray(int index1, int index2) {
        ArrayList<Float> arrayX = this.getColByIndex(index1);

        ArrayList<Float> arrayY = this.getColByIndex(index2);

     
        if (arrayX == null || arrayY == null)
            return  null;

        ArrayList<Point> pointArray = new ArrayList<>();
        
        for (int i=0; i<arrayX.size()-1; i++) {
        
        	pointArray.add(new Point(arrayX.get(i),arrayY.get(i)));
        
        }
        return pointArray;
    }


    public int getColIndexByName(String name) {
    	String[] names = this.dataStreams.get(0).split(",");
    	for (int i=0; i<names.length; i++) {
    		if (name.equals(names[i]))
    			return i;
    	}

        return -1;
    }

    public int getSize() { return dataStreams.size(); }

    public String getStream(int index) {
        if (index > 0 && index < dataStreams.size())
            return dataStreams.get(index);
        return null;
    }
}