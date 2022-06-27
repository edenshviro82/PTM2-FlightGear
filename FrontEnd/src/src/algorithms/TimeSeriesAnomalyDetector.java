package algorithms;


import java.util.List;

import necessary_classes.*;

public interface TimeSeriesAnomalyDetector {
    void learnNormal(TimeSeries ts);
    List<AnomalyReport> detect(TimeSeries ts);
}
