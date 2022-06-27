package algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import necessary_classes.TimeSeries;

public class Zscore implements TimeSeriesAnomalyDetector {
    HashMap<String, WeakCorrelatedFeatures> map;

    public Zscore() {
        this.map = new HashMap<>();
    }

    @Override
    public void learnNormal(TimeSeries ts) {
        int size = ts.dataStreams.get(0).split(",").length;
        for (int i=0; i<size;i++) {
            //finding the highest correlated columns for col a,b,c...
            int c = -1;
            float m = 0, p;
            for (int j = i + 1; j < size; j++) {
                p = Math.abs(StatLib.pearson(ts.getColByIndex(i), ts.getColByIndex(j)));
                if (p > m) {
                    m = p;
                    c = j;
                }
            }
            //c is the index of most correlated col, m is the correlation between col i and col c

            //calculating deviation, expectation and zMax for col i
            float dev = (float) Math.sqrt(StatLib.var(ts.getColByIndex(i)));
            float exp = (float) StatLib.avg(ts.getColByIndex(i));
            float zMax = getZmax(ts.getColByIndex(i),exp,dev);
            //get the feature name of col i and the most correlated feature name (col c)
            String feature1 = ts.getColName(i);
            String feature2 = ts.getColName(c);
            map.put(feature1,new WeakCorrelatedFeatures(feature1,feature2,m,dev,exp,zMax));
        }
    }

    @Override
    public List<AnomalyReport> detect(TimeSeries ts) {
        //crate new anomaly report list
        LinkedList<AnomalyReport> detection = new LinkedList<>();
        //get the size of time series columns
        int size = ts.dataStreams.get(0).split(",").length;
        for (int i=0; i<size; i++) {
            ArrayList<Float> col = ts.getColByIndex(i);
            String colName = ts.getColName(i);

            //get the z max, col exp, col dev
            float zMax = map.get(colName).zMax;
            float dev = map.get(colName).deviation;
            float exp = map.get(colName).exp;

            for (int j=0; i<col.size(); j++) {
                //calc the current z value
                float z = Math.abs(col.get(j) - exp)/dev;
                //check if the value is unusual and put in a new anomaly report
                if (z>zMax)
                    detection.add(new AnomalyReport(colName,j));
            }
        }
        return detection;
    }

    public float getZmax(ArrayList<Float> featureCol,float exp, float dev) {
        float[] zMax = new float[1];
        zMax[0] = 0;
        featureCol.forEach(aFloat -> {
            //calculate current point z value
            float z = Math.abs(aFloat - exp)/dev;
            if (z > zMax[0])
                zMax[0] = z;
        });
        return zMax[0];
    }
}
