//package algorithms;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class Zscore implements TimeSeriesAnomalyDetector{
//	ArrayList<Double> var = new ArrayList<Double>();
//
//	@Override
//	public void learnNormal(TimeSeries ts) {
//		// TODO Auto-generated method stub
//		for (TimeSeries.Feature feature : ts.getTable()) {
//			double maximum = -1;
//			double zScore;
//			double var_avg;
//			double std;
//			int i ;
//			for (i=2; i < feature.getExamples().size(); i++) {
//				std=  Math.sqrt(StatLib.var(StatLib.FloatListToFloatArr(feature.getExamples().subList(0, i))));
//				var_avg = StatLib.avg( StatLib.FloatListToFloatArr( feature.getExamples().subList(0, i)));
//				for (int j = 0; j < i; j++) {
//					if (std != 0 ) {
//						zScore = Math.abs( feature.getExamples().get(j) - var_avg) / std;
//					}
//					else {
//						zScore = 0;
//					}
//					if (zScore > maximum) {
//						maximum = zScore;
//					}
//				}	
//			}
//			var.add(maximum);
//			maximum = -1;
//		}
//	}
//		
//
//	@Override
//	public List<AnomalyReport> detect(TimeSeries ts) {
//		ArrayList<AnomalyReport> arrayL = new ArrayList<AnomalyReport>();
//		// TODO Auto-generated method stub
//		double x_avg;
//		double std;
//		double zScore;
//		for (int i = 0; i < ts.getTable().size(); i++) {
//			TimeSeries.Feature feature = ts.getTable().get(i);
//			for (int j = 2; j < feature.getExamples().size(); j++) {
//				 x_avg = StatLib.avg( StatLib.FloatListToFloatArr( feature.getExamples().subList(0, j)));
//				 std =  Math.sqrt(StatLib.var(StatLib.FloatListToFloatArr(feature.getExamples().subList(0, j))));
//				if (std != 0 ) {
//					for (int s = 0; s < j; s++) {
//						 zScore = Math.abs(( feature.getExamples().get(s) - x_avg) )/ std;
//						if (zScore > var.get(i)) {
//							AnomalyReport ar = new AnomalyReport(feature.getNameId(), s+1);
//							if (!StatLib.isContain(arrayL, ar)) {
//								arrayL.add(ar);
//							}
//						}
//					}
//				}
//			}	
//		}
//		return arrayL;
//	}
//
//
//}
