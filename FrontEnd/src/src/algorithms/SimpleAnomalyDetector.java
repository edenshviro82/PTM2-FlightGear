//package algorithms;
//
//import java.lang.reflect.Array;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Vector;
//
//import necessary_classes.*;
//
//public class SimpleAnomalyDetector implements TimeSeriesAnomalyDetector {
//
//	public List<CorrelatedFeatures> _features;
//	public float _minCor;
//
//	 public SimpleAnomalyDetector() {
//		this._minCor = (float) 0.9;
//		this._features = new LinkedList<>();
//	}
//
//	SimpleAnomalyDetector(float correlation) {
//		this._minCor = correlation;
//		this._features = new LinkedList<>();
//	}
//
//	public void set_minCor(float _minCor) {
//		this._minCor = _minCor;
//	}
//
//	@Override
//	public void learnNormal(TimeSeries2 ts) {
//		for (int i=0; i<ts.table.length;i++)
//		{
//			//finding the highest correlated columns for col a,b,c...
//			int c=-1;
//			float m=0,p;
//			for (int j=i+1; j<ts.table.length; j++)
//			{
//				p = Math.abs(StatLib.pearson(ts.getFloatArray(i),ts.getFloatArray(j)));
//				if (p>m)
//				{
//					m = p;
//					c = j;
//				}
//			}
//			//make sure the correlation is higher than 0.9 (or any other limit we choose)
//			if (m>this._minCor && c!= -1)
//			{
//				float maxdev = 0;
//				float curdev;
//				Line reg = StatLib.linear_reg(ts.getPointsArray(i,c)); //creating line for relevant i and j cols
//				Point [] pointscheck =  ts.getPointsArray(i,c);        //creating array of points to check distance from the line
//				for (int k=0; k< pointscheck.length; k++) {
//					curdev = StatLib.dev(pointscheck[k],reg);          //calc each point distance from line
//					if (curdev > maxdev)
//						maxdev = curdev;                              //get the value of the most distant point
//				}
//				maxdev*=1.1;
//				//creating Correlated Feature with the parameters we calculated and insert him to our linked list
//				
//				System.out.println(ts.getColName(i)+ "colname");
//				CorrelatedFeatures cl = new CorrelatedFeatures(ts.getColName(i),ts.getColName(c),m,reg,maxdev);
//				this._features.add(cl);
//			}
//		}
//	}
//
//
//	@Override
//	public List<AnomalyReport> detect(TimeSeries2 ts) {
//		LinkedList<AnomalyReport> detection = new LinkedList<>();
//		for (int i=0; i<this._features.size(); i++) {
//			String col1 = this._features.get(i).feature1;
//			String col2 = this._features.get(i).feature2;
//			Point[] check = ts.getPointsArray(ts.getColIndexByName(col1), ts.getColIndexByName(col2));
//				for (int time = 0; time < check.length; time++) {
//					if (StatLib.dev(check[time], this._features.get(i).lin_reg) > this._features.get(i).threshold)
//					{
//						AnomalyReport ar = new AnomalyReport(col1+"-"+col2, time+1);
//						detection.add(ar);
//					}
//				}
//		}
//		return detection;
//	}
//
//	public void learnNormal(TimeSeries2 ts, String feature) {
//		for (int i=0; i<ts.table.length;i++)
//		{
//			//finding the highest correlated columns for col a,b,c...
//			int c=-1;
//			float m=0,p;
//			for (int j=i+1; j<ts.table.length; j++)
//			{
//				
//				p = Math.abs(StatLib.pearson(ts.getFloatArray(i),ts.getFloatArray(j)));
//				if (p>m)
//				{
//					m = p;
//					c = j;
//				}
//			}
//			//make sure the correlation is higher than 0.9 (or any other limit we choose)
//			if (m>this._minCor && c!= -1)
//			{
//				float maxdev = 0;
//				float curdev;
//				Line reg = StatLib.linear_reg(ts.getPointsArray(i,c)); //creating line for relevant i and j cols
//				Point [] pointscheck =  ts.getPointsArray(i,c);        //creating array of points to check distance from the line
//				for (int k=0; k< pointscheck.length; k++) {
//					curdev = StatLib.dev(pointscheck[k],reg);          //calc each point distance from line
//					if (curdev > maxdev)
//						maxdev = curdev;                              //get the value of the most distant point
//				}
//				maxdev*=1.1;
//				//creating Correlated Feature with the parameters we calculated and insert him to our linked list
//				CorrelatedFeatures cl = new CorrelatedFeatures(ts.getColName(i),ts.getColName(c),m,reg,maxdev);
//				this._features.add(cl);
//			}
//		}		
//	}
//
//
//}
//
//
