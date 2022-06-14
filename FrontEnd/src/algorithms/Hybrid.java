//package algorithms;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Random;
//
//import algorithms.TimeSeries.Feature;
//
//public class Hybrid implements TimeSeriesAnomalyDetector
//{
//	HashMap<String, algorithms.Linear> Linear = new HashMap<>();
//	HashMap<String, Zscore> ZScore=new HashMap<>();
//	HashMap<String, Circle> hybrid = new HashMap<>();
//	private Random rand = new Random();
//	HashMap<String,String> mapL=new HashMap<>();
//
//
//	@Override
//	public void learnNormal(TimeSeries ts) {
//		ArrayList<Point> points = new ArrayList<>();
//		ArrayList<CorrelatedFeatures> correlated = StatLib.FindCorrelatedFeatures(ts).correlated;
//		for (CorrelatedFeatures correlatedFeature : correlated) {
//			Feature f1 = ts.getFeatureByName1(correlatedFeature.f1);
//			Feature f2 = ts.getFeatureByName1(correlatedFeature.f2);
//			String name=f1.getName()+"-"+f2.getName();
//			
//			if(Math.abs(correlatedFeature.correlation)>=0.95)
//			{
//				TimeSeries t1 = new TimeSeries(f1,f2);
//				algorithms.Linear l= new Linear();
//				l.learnNormal(t1);
//				Linear.put(name,l);
//				mapL.put(f1.getNameId(),f2.getNameId());
//
//
//			}
//			else if(Math.abs(correlatedFeature.correlation)<0.5) {
//				TimeSeries t2 = new TimeSeries(f1,f2);
//				Zscore z = new Zscore();
//				z.learnNormal(t2);
//				ZScore.put(name, z);
//			}
//			else {
//				TimeSeries t3 = new TimeSeries(f1,f2);
//				for(int i=0;i<f1.size;i++)
//				{
//					Point p=new Point(f1.examples.get(i),f2.examples.get(i));
//					points.add(p);
//				}
//				hybrid.put(name,findMinimumCircle(points));
//				points=new ArrayList<>();
//			}
//		}
//		for (Feature fe : StatLib.FindCorrelatedFeatures(ts).notCorrelated) {
//			TimeSeries ti = new TimeSeries(fe);
//			Zscore z = new Zscore();
//			z.learnNormal(ti);
//			ZScore.put(fe.name, z);
//		}
//	}
//
//	@Override
//	public List<AnomalyReport> detect(TimeSeries ts) {
//		int index=1;
//		ArrayList<AnomalyReport> resultList = new ArrayList<AnomalyReport>();
//		for (int i = 0; i < ts.table.size(); i++) {
//			for (int j = 0; j < ts.table.size(); j++) {
//				Feature f = ts.getTable().get(i);
//				Feature f1 = ts.getTable().get(j);
//				TimeSeries t = new TimeSeries(f,f1);
//				String name= f.getName()+"-"+f1.getName();
//				if(hybrid.containsKey(name)) {
//					ArrayList<Point> points = new ArrayList<>();
//					for(int k=0;k<f1.size;k++)
//					{
//						Point p=new Point(f.examples.get(k),f1.examples.get(k));
//						points.add(p);
//					}
//					for (Point point : points) {
//						if(!this.hybrid.get(name).isContainsPoint(point)) {
//							String description = f.nameId + "-" + f1.nameId;
//							String description1 = f1.nameId + "-" + f.nameId;
//							AnomalyReport report = new AnomalyReport(description,index);
//							AnomalyReport report1 = new AnomalyReport(description1,index);
//							if(!StatLib.isContain(resultList,report) && !StatLib.isContain(resultList,report1))
//								resultList.add(report);
//						}
//						index++;
//					}
//				}
//				else if(Linear.containsKey(name))
//				{
//					List<AnomalyReport> reports =this.Linear.get(name).detect(t);
//					for (AnomalyReport anomalyReport : reports) {
//						AnomalyReport anomalyReport1 = new AnomalyReport(StatLib.RevString(anomalyReport.description), anomalyReport.timeStep);
//						if(!StatLib.isContain(resultList,anomalyReport) && !StatLib.isContain(resultList,anomalyReport1))
//							resultList.add(anomalyReport);
//					}
//				}
//				else if(this.ZScore.containsKey(name)){
//					List<AnomalyReport> reports =this.ZScore.get(name).detect(t);
//					for (AnomalyReport anomalyReport : reports) {
//						AnomalyReport anomalyReport1 = new AnomalyReport(StatLib.RevString(anomalyReport.description), anomalyReport.timeStep);
//						if(!StatLib.isContain(resultList,anomalyReport) && !StatLib.isContain(resultList,anomalyReport1))
//							resultList.add(anomalyReport);
//					}
//				}	
//			}	
//		}
//		for (Feature f : StatLib.FindCorrelatedFeatures(ts).notCorrelated) {
//			if (ZScore.containsKey(f.name)) {
//				TimeSeries t = new TimeSeries(f);
//				List<AnomalyReport> reports =this.ZScore.get(f.name).detect(t);
//				for (AnomalyReport anomalyReport : resultList) {
//					if(!StatLib.isContain(resultList,anomalyReport))
//						resultList.add(anomalyReport);	
//				}
//			}
//		}
//		resultList.sort((x,y)->{
//			return (int) (x.timeStep-y.timeStep);
//		});
//		return resultList;
//	}
//
//	public Circle findMinimumCircle(final List<Point> points) {
//		return WelezAlgo(points, new ArrayList<Point>());
//    }
//    private Circle WelezAlgo(final List<Point> points, final List<Point> R) {
//    	Circle minimumCircle = null;
//		if (R.size() == 3) {
//			minimumCircle = new Circle(R.get(0), R.get(1), R.get(2));
//		}
//		else if (points.isEmpty() && R.size() == 2) {
//			minimumCircle = new Circle(R.get(0), R.get(1));
//		}
//		else if (points.size() == 1 && R.isEmpty()) {
//			minimumCircle = new Circle(points.get(0).x, points.get(0).y, 0);
//		}
//		else if (points.size() == 1 && R.size() == 1) {
//			minimumCircle = new Circle(points.get(0), R.get(0));
//		}
//		else {
//			Point p = points.remove(rand.nextInt(points.size()));
//			minimumCircle = WelezAlgo(points, R);
//			if (minimumCircle != null && !minimumCircle.isContainsPoint(p)) {
//				R.add(p);
//				minimumCircle = WelezAlgo(points, R);
//				R.remove(p);
//				points.add(p);
//			}
//		}
//		return minimumCircle;
//    }
//
//	public HashMap<String, String> getMapL() {
//		return mapL;
//	}
//
//}