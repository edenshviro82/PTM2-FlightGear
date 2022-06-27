package algorithms;

import algorithms.*;
import necessary_classes.TimeSeries;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LinearAnomalyDetector implements TimeSeriesAnomalyDetector {

    public List<StrongCorrelatedFeatures> features;
    float minCor;

    public LinearAnomalyDetector() {
        this.minCor = (float) 0.95;
        this.features = new LinkedList<>();
    }

    LinearAnomalyDetector(float correlation) {
        this.minCor = correlation;
        this.features = new LinkedList<>();
    }

    public void setMinCor(float minCor) {
        this.minCor = minCor;
    }

    @Override
    public void learnNormal(TimeSeries ts) {
        //get the number of columns
        int size = ts.dataStreams.get(0).split(",").length;
        for (int i=0; i<size;i++)
        {
            //finding the highest correlated columns for col a,b,c...
            int c=-1;
            float m=0,p;
            for (int j=i+1; j<size; j++) {
                p = Math.abs(StatLib.pearson(ts.getColByIndex(i),ts.getColByIndex(j)));
                if (p>m) {
                    m = p;
                    c = j;
                }
            }
            //make sure the correlation is higher than 0.95 (or any other limit we choose)
            if (m>this.minCor && c!= -1)
            {
                float maxdev = 0;
                float curdev;
                //creating line for relevant i and j cols
                Line reg = StatLib.linear_reg(ts.getPointsArray(i,c));
                //creating array of points to check distance from the line
                ArrayList<Point> pointscheck =  ts.getPointsArray(i,c);
                for (int k=0; k< pointscheck.size(); k++) {
                    //calc each point distance from line
                    curdev = StatLib.dev(pointscheck.get(k),reg);
                    //get the value of the most distant point
                    if (curdev > maxdev)
                        maxdev = curdev;
                }
                maxdev*=1.1;
                //creating Correlated Feature with the parameters we calculated and insert him to our linked list
                StrongCorrelatedFeatures cl = new StrongCorrelatedFeatures(ts.getColName(i),ts.getColName(c),m,reg,maxdev);
                this.features.add(cl);
            }
        }
    }


    @Override
    public List<AnomalyReport> detect(TimeSeries ts) {
        LinkedList<AnomalyReport> detection = new LinkedList<>();
        for (int i = 0; i<this.features.size(); i++) {
            //get the correlated features names
            String col1 = this.features.get(i).feature1;
            String col2 = this.features.get(i).feature2;
            //get the cols of the correlated features
            ArrayList<Point> check = ts.getPointsArray(ts.getColIndexByName(col1), ts.getColIndexByName(col2));
            for (int time = 0; time < check.size(); time++) {
                if (StatLib.dev(check.get(time), this.features.get(i).lin_reg) > this.features.get(i).threshold)
                {
                    AnomalyReport ar = new AnomalyReport(col1+"-"+col2, time+1,new Point(check.get(time).x,check.get(time).y));
                    detection.add(ar);
                }
            }
        }
        return detection;
    }

    public List<StrongCorrelatedFeatures> getNormalModel(){
        return this.features;
    }
}

