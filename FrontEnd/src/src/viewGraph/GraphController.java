package viewGraph;

import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.event.ChangeListener;

import algorithms.AnomalyReport;
import algorithms.Circle;
import algorithms.Hybrid;
import algorithms.LinearAnomalyDetector;
import algorithms.LinearRegression;
import algorithms.Point;
import model.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import model.Data;
import necessary_classes.*;

public class GraphController implements Initializable {
	
	public static String path;
	
    public @FXML LineChart leftGraph,rightGraph,detectionGraph;
    public @FXML StackPane stackPane;
    public XYChart.Series leftSeries,rightSeries,detectionSeries;
    
    public @FXML  ListView<String> listView;
    
    public Data data;
   
    public static TimeSeries ts;

    public static TimeSeries ts2;
    
    public static TimeSeries ts3;

    volatile boolean isSelect;
    
	public ExecutorService es;
	
	public String feature;
	
	public String feature2;
	
	public   ArrayList<Point> pointsArray;
	
	public float m;
	
	//public SimpleAnomalyDetector sad;
	public static LinearRegression sad;
	
	public StringProperty liveStr;
	
	public Hybrid h;
	
	public List<AnomalyReport> ar;
	
	
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
        leftSeries = new XYChart.Series();
        rightSeries = new XYChart.Series();
        detectionSeries= new XYChart.Series();
        leftGraph.setAnimated(false);
        rightGraph.setAnimated(false);
        detectionGraph.setAnimated(false);
        detectionGraph.setCreateSymbols(false);
        leftGraph.setCreateSymbols(false);
        rightGraph.setCreateSymbols(false);
        data = new Data();
        listView.getItems().addAll(data.fps);
        ts = new TimeSeries("./sources/reg_flight.csv");
        ts3=new TimeSeries("./sources/matias.csv");
        //ts2=new TimeSeries2("./sources/reg_flight.csv");
        es=Executors.newFixedThreadPool(8);
        feature=null;
        feature2=null;
        ar=new LinkedList<>();
        initar();
        
        
        
        es.execute(()->
        {
        	sad = new LinearRegression((float)0);
			liveStr=new SimpleStringProperty();
			
			sad.learnNormal(ts3);
			System.out.println("done learn normal");
			//sad.detect(ts);
			
		});
		
		es.execute(()->
		{
			h=new Hybrid();
			h.HybridAlgorithm(ts);
			h.learnNormal(ts);
			System.out.println("done learn normal hynrid");

		});
		

    }
	
	
	
	
	private void initar() {
		ar.add(new AnomalyReport("throttle-aileron", 20, new Point(20,30)));
		ar.add(new AnomalyReport("throttle-elevator", 20, new Point(23,3)));
		ar.add(new AnomalyReport("pitch-deg-thtottle", 20, new Point(5,5)));
		ar.add(new AnomalyReport("throttle-pitchdeg", 20, new Point(25,15)));

		
	}
	
	
	 private XYChart.Series<String, Number> minimalCircleSeries(Circle c) {

         XYChart.Series series = new XYChart.Series();
         javafx.scene.shape.Circle pointCircle = new javafx.scene.shape.Circle(c.center.x, c.center.y,19);
         XYChart.Data<String, Number> data = new XYChart.Data<>(""+c.center.x, c.center.y);
         data.setNode(pointCircle);
         series.getData().add(data);
         return series;
}




	public void display()
	{
		if(path==null)
		{
			ts = new TimeSeries("./sources/reg_flight.csv");
	      //  ts2=new TimeSeries2("./sources/reg_flight.csv");
		}
//		else {
//			ts=new TimeSeries(path);
//			//ts2=new TimeSeries2(path);
//		}		
		
		
		
		leftGraph.getData().clear();
		leftSeries.getData().clear();
	    rightGraph.getData().clear();
	    rightSeries.getData().clear();
	    
		System.out.println("beg");
		feature = this.listView.getSelectionModel().getSelectedItem();
		  
		 for(int i=0;i<sad.features.size();i++)
		    {
			    //System.out.println("f1 "+sad.features.get(i).feature1.toString());

		    	if(sad.features.get(i).feature1.equals(feature)||sad.features.get(i).feature2.equals(feature))
		    	{
		    		if(sad.features.get(i).feature1.equals(feature))
		    		{
		    			feature2=sad.features.get(i).feature2;
		    		}
		    		else
		    		{
		    			feature2=sad.features.get(i).feature1;

		    		}
		    		
		    	}
		    }
		   
		   
		 
		 String algo=h.whichAlgorithm.get(feature);
		   
		   
		   if(algo.equals("Hybrid")) {
    		   System.out.println("Hybrid");
    		   Circle c=h.whoCircle.get(feature);
    	       XYChart.Series<String, Number> series = minimalCircleSeries(c);
    	       System.out.println( c.toString());
    		   Platform.runLater(()->
    			{	
    			        detectionGraph.setData(FXCollections.observableArrayList(series));
    			        	
    			});
    		   
    	   }
    	  
		   
		   if(algo.equals("LinearRegression")) {
    		   System.out.println("LinearRegression");
    		   pointsArray = ts.getPointsArray(ts.getColIndexByName(feature), ts.getColIndexByName(feature2));
    		   m=((pointsArray.get(pointsArray.size()-1).y))-(pointsArray.get(0).y)/((pointsArray.get(pointsArray.size()-1).x)-(pointsArray.get(0).x));
    		  
    		   es.execute(()->{
//    		
//    			   try {
//    			   XYChart.Series<String,Number> setL5 = new XYChart.Series<>();
//    			   ArrayList<Point> pointsArray = ts.getPointsArray(ts.getColIndexByName(feature), ts.getColIndexByName(feature2));
//    			   System.out.println("iodfjguodkfl"+pointsArray.size());
//    			   for(int i=0; i<pointsArray.size(); i++)
//    			   {
//    				   
//					Thread.sleep(250);
//					
//    			   //XYChart.Data<String,Number> d3 = new XYChart.Data<>(""+pointsArray.get(i).x, pointsArray.get(i).y);
//    			   XYChart.Data<String,Number> d3 = new XYChart.Data<>(""+i,5);
//    			   setL5.getData().add(d3);
//    			   Platform.runLater(()->
//   					{	
//   						detectionGraph.setData(FXCollections.observableArrayList(setL5));
//   		        
//   					});
//    			   
//    			   
//    			   }
//    			   
//    			   } catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
    		   
    		   	}) ;

    		   
    	   }
    	   if(algo.equals("Zscore")) {
    		   System.out.println("Zscore");

    	   }
		   
		 
	
    	   
    	   
    	   
    	   
    	   
    	   
    	   
    	   
    	   
    	  es.execute(()->{
		
			isSelect=false;
			   try {
					Thread.sleep(251);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			   
			   
			isSelect=true;
		    //sad.learnNormal(ts);
		    
		    
		 
		    
		    
	       XYChart.Series<String,Number> setL3 = new XYChart.Series<>();
	       XYChart.Series<String,Number> setL4 = new XYChart.Series<>();
     	   XYChart.Series<String,Number> setL5 = new XYChart.Series<>();
      	   XYChart.Series<String,Number> setL6 = new XYChart.Series<>();
      	   setL5.setName("linear regression");
      	   setL6.setName("excaption");

		   ArrayList<Float> f=new ArrayList<>();
		   ArrayList<Float> f2=new ArrayList<>();
		   f=ts.getColByName(feature);
		   f2=ts.getColByName(feature2);
		  
		  
		   
		   
		   for (int i=0;i<f.size();i++) {
		    if(isSelect) {
	    	   try {
				Thread.sleep(250);
		        XYChart.Data<String,Number> d = new XYChart.Data<>(""+i, f.get(i));
		        setL3.getData().add(d);
		       
		        Platform.runLater(()->
				{	
		        leftGraph.setData(FXCollections.observableArrayList(setL3));
		        
				});
	    		} catch (InterruptedException e) {
					e.printStackTrace();
				}
	    	   
	    	   if(feature2!=null && !feature2.equals(feature)) {
	    	   XYChart.Data<String,Number> d2 = new XYChart.Data<>(""+i, f2.get(i));
	    	   setL4.getData().add(d2);
	    	   Platform.runLater(()->
				{	
		        rightGraph.setData(FXCollections.observableArrayList(setL4));
		        	
				});
	    	  
	    	 
	    	   }
	    	   
	    	   if(algo.equals("LinearRegression")) {
	    		   System.out.println("LinearRegression");
			 
	    	XYChart.Data<String,Number> d3 = new XYChart.Data<>(""+i*m,i*m);	
	    	if(ar.size()>i) {
	    		XYChart.Data<String,Number> d4 = new XYChart.Data<>(""+ar.get(i).point.x,ar.get(i).point.y);	
		    	setL6.getData().add(d4);
	    	}
	    	setL5.getData().add(d3);
	    	
			   Platform.runLater(()->
				{	
		        detectionGraph.setData(FXCollections.observableArrayList(setL5,setL6));
		        	
				});
			
			   
	    	   }
	    	   
	    	   
	    	   
	    	   
	    	   
		    }
	       }		

		});
	    
	    
	    
	    
	    
	    
	    
	    
	}




//	public void display2() {
//		
//		
//		Vector table[]=null;
//
//		
//		String namesParams[]=data.fps;
//		for(int i=0;i<namesParams.length;i++)
//		{
//			table[i].add(i, namesParams[i]);
//		}
//		
//		
//		String line=liveStr.getValue();
//		String sp[]= line.split(",");		
//		for(int i=0;i<sp.length;i++)
//		{
//			table[i].add(i, sp[i]);
//		}
//		//ts2=new TimeSeries2(table);
//
//		String feature = this.listView.getSelectionModel().getSelectedItem();
//	    es.execute(()->{
//		
//			isSelect=false;
//			   try {
//					Thread.sleep(251);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			   
//			   
//			isSelect=true;
//			String feature2=null;
//		    sad.learnNormal(ts2);
//		    
//		    for(int i=0;i<sad._features.size();i++)
//		    {
//			    System.out.println("f1 "+sad._features.get(i).feature1.toString());
//
//		    	if(sad._features.get(i).feature1.equals(feature)||sad._features.get(i).feature2.equals(feature))
//		    	{
//		    		if(sad._features.get(i).feature1.equals(feature))
//		    		{
//		    			feature2=sad._features.get(i).feature2;
//		    		}
//		    		else
//		    		{
//		    			feature2=sad._features.get(i).feature1;
//
//		    		}
//		    		
//		    	}
//		    }
//		    
//		    
//	       XYChart.Series<String,Number> setL3 = new XYChart.Series<>();
//	       XYChart.Series<String,Number> setL4 = new XYChart.Series<>();
//		   ArrayList<Float> f=new ArrayList<>();
//		   ArrayList<Float> f2=new ArrayList<>();
//		   f=ts.getColByName(feature);
//		   f2=ts.getColByName(feature2);
//		   
//		  
//	       for (int i=0;i<f.size();i++) {
//		    if(isSelect) {
//	    	   try {
//				Thread.sleep(250);
//		        XYChart.Data<String,Number> d = new XYChart.Data<>(""+i, f.get(i));
//		        setL3.getData().add(d);
//		       
//		        Platform.runLater(()->
//				{	
//		        leftGraph.setData(FXCollections.observableArrayList(setL3));
//		        
//				});
//	    		} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//	    	   
//	    	   if(feature2!=null) {
//	    	   XYChart.Data<String,Number> d2 = new XYChart.Data<>(""+i, f2.get(i));
//	    	   setL4.getData().add(d2);
//	    	   Platform.runLater(()->
//				{	
//		        rightGraph.setData(FXCollections.observableArrayList(setL4));
//		        
//				});
//	    	 
//	    	   }
//			}
//	       }
//
//		});
//		
//		
//	}
}
