package algorithms;

import necessary_classes.*;

import java.util.*;

public class Hybrid implements TimeSeriesAnomalyDetector {

    public ArrayList<Circle> circles = new ArrayList<>();
    public Map<String, String> whichAlgorithm = new HashMap<>();
    public ArrayList<CorrelatedFeatures> theCorrelatedFeatures = new ArrayList<>();
    public Map<String, Circle> whoCircle = new HashMap<>();


    public void HybridAlgorithm(TimeSeries timeSeries) {

        LinearRegression linearReg = new LinearRegression((float) 0.95);
        linearReg.learnNormal(timeSeries);
        List<CorrelatedFeatures> correlatedFeatures = linearReg.getNormalModel();
        for (CorrelatedFeatures features: correlatedFeatures)
        {
            whichAlgorithm.put(features.feature1, "LinearRegression");
            whichAlgorithm.put(features.feature2, "LinearRegression");
        }

        LinearRegression linearReg_hybrid = new LinearRegression((float) 0.5);
        linearReg_hybrid.learnNormal(timeSeries);
        List<CorrelatedFeatures> correlatedFeatures1 = linearReg_hybrid.getNormalModel();
        for (CorrelatedFeatures features: correlatedFeatures1)
        {
            if (!whichAlgorithm.containsKey(features.feature1)) {
                whichAlgorithm.put(features.feature1, "Hybrid");
                theCorrelatedFeatures.add(features);
            }
        }

        int colSize = timeSeries.dataStreams.get(0).split(",").length;

        for (int i=0; i<colSize; i++) {
            if (!whichAlgorithm.containsKey(timeSeries.getColName(i)))
                whichAlgorithm.put(timeSeries.getColName(i),"ZScore");
        }
    }

    @Override
    public void learnNormal(TimeSeries timeSeries) {
        for (CorrelatedFeatures features: theCorrelatedFeatures)
        {
            ArrayList<Float> column_i = timeSeries.getColByName(features.feature1);
            ArrayList<Float> column_j = timeSeries.getColByName(features.feature2);
            Vector<Point> pointsVector = new Vector<>();
            for (int t = 0; t < column_i.size(); t++) {
                pointsVector.add(new Point(column_i.get(t), column_j.get(t)));
            }
            Circle circle = Welzl.makeCircle(pointsVector);
            whoCircle.put(features.feature1, circle);
            circles.add(circle);
        }
    }

    @Override
    public List<AnomalyReport> detect(TimeSeries timeSeries) {
        List<AnomalyReport> anomalyReports = new ArrayList<AnomalyReport>();
        int k = 0;

        for (CorrelatedFeatures features: theCorrelatedFeatures)
        {
            ArrayList<Float> column_i = timeSeries.getColByName(features.feature1);
            ArrayList<Float> column_j = timeSeries.getColByName(features.feature2);
            Vector<Point> pointsVector = new Vector<>();
            for (int t = 0; t < column_i.size(); t++) {
                pointsVector.add(new Point(column_i.get(t), column_j.get(t)));
            }
            for (int h = 0; h < pointsVector.size(); h++) {
                if (!circles.get(k).contains(pointsVector.get(h))) {
                    anomalyReports.add(new AnomalyReport(features.feature1 + "-" + features.feature2, h));
                }
            }
            k++;
        }
        return anomalyReports;
    }
}