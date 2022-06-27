package model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import necessary_classes.Location;
import necessary_classes.Plane;

public class Model extends Observable {

static ConcurrentHashMap<String, AtomicBoolean> activeTabsHash;

//General members

String propFileName;
HashMap<String, String> prop;
HashMap<String, String> streamprop;
HashMap<String, String> tabHash;
Socket agent,streamSock;// streamSock -> socket for the gauges stream (streaming all the time)
Socket tabSock;// tabSock -> the socket for all the usually request from backEnd 
String[] sp={};// for splitting the streaming info
PrintWriter out2agent,out2streamSock,out2TabSock;
Data data;
ExecutorService es;
public ObjectInputStream ob;
public BufferedReader opIn,streamIn;
volatile boolean stopStream;
public String currentId;
volatile boolean switchTab;
public StringProperty liveStr;


//fleet members
public ArrayList<Plane> planes;
public  HashMap<Integer,Integer> fleetSize;
public HashMap<Integer,Double> milesYear;
public HashMap<String,Double> miles;

//need to delete
public IntegerProperty isFleetPushed;
public IntegerProperty isMoniPushed;
public IntegerProperty isTelePushed;
public IntegerProperty isTimeCapsulePushed;


//moni members
public DoubleProperty moniAileron;
public DoubleProperty moniRudder;
public DoubleProperty moniThrottle;
public DoubleProperty moniElevators;


//tele members
public FloatProperty altitude,headDeg,rollDeg,longitude,verticalSpeed,airspeed;
boolean flightstart;



//the constructor read the file and connect to the back as a Socket
	public Model(String propFileName) {
		
		
		activeTabsHash = new ConcurrentHashMap<>();// hashmap that show which tab is active
		this.initActiveTabHash();
		es = Executors.newFixedThreadPool(9);
		stopStream=false;
		data=new Data();
		flightstart=false;
		liveStr=new SimpleStringProperty();

		//fleet
		fleetSize=new HashMap<>();
		milesYear=new HashMap<>();
		miles=new HashMap<>();
		planes= new ArrayList<>();
		
		//gauge
		longitude=new SimpleFloatProperty(0);
		rollDeg=new SimpleFloatProperty(0);
		headDeg=new SimpleFloatProperty(0);
		altitude=new SimpleFloatProperty(0);
		verticalSpeed=new SimpleFloatProperty(0);
		airspeed=new SimpleFloatProperty(0);
		
		//moni joystick
		moniAileron=new SimpleDoubleProperty(0);
		moniElevators=new SimpleDoubleProperty(0);
		moniRudder=new SimpleDoubleProperty(0);
		moniThrottle=new SimpleDoubleProperty(0);
		//run at another thread
	


		
		
		prop=initHashMap("streamprop.txt", prop);
		tabHash=initHashMap("streamprop.txt",tabHash);
		
		connectOp();
		connectStream();
		

	}
	
	
	

	
	private void initActiveTabHash() {
	// TODO Auto-generated method stub
	activeTabsHash.put("fleet", new AtomicBoolean(false));
	activeTabsHash.put("moni", new AtomicBoolean(false));
	activeTabsHash.put("tele", new AtomicBoolean(false));
	activeTabsHash.put("tc", new AtomicBoolean(false));
}


	public void connectOp() {

		int port= Integer.parseInt(tabHash.get("operationPort"));
		try {
		tabSock=new Socket(tabHash.get("ip"),port);
        opIn = new BufferedReader(new InputStreamReader(tabSock.getInputStream()));
        out2TabSock = new PrintWriter(tabSock.getOutputStream(),true);// auto flush
        ob = new ObjectInputStream(tabSock.getInputStream());
        
		} catch (IOException e) {e.printStackTrace();} 
	}
	
	
	public void connectStream() {
	
		
		int port= Integer.parseInt(tabHash.get("streamPort"));
		//The port is 5404 in order to connect to the backEnd stream socket
		try {
			streamSock=new Socket(tabHash.get("ip"),port);
			streamIn = new BufferedReader(new InputStreamReader(streamSock.getInputStream()));
		} catch (UnknownHostException e) {e.printStackTrace();} 
		catch (IOException e) {e.printStackTrace();} 
		 
	}
	
	
	//get stream from back-end
	public void streamSocket() {

		
				try {
					System.out.println("gague");
		             	
		             	String line = null;
		             
		             	
		            	while( switchTab) {
							line = streamIn.readLine();
							sp= line.split(",");
							liveStr.set(line);
				      
							
							Platform.runLater(()->{ 
								   //altitude-ft - high, vertical-speed-fps , airspeed-kt, heading-deg , roll-deg ,longitude-deg
						             altitude.set(Float.parseFloat(sp[Data.paramsIndex.get("altitude-ft")]));
						             airspeed.set(Float.parseFloat(sp[Data.paramsIndex.get("airspeed-kt")]));
						             headDeg.set(Float.parseFloat(sp[Data.paramsIndex.get("heading-deg")]));
						             longitude.set(Float.parseFloat(sp[Data.paramsIndex.get("longitude-deg")]));
						             rollDeg.set(Float.parseFloat(sp[Data.paramsIndex.get("roll-deg")]));
						             verticalSpeed.set(Float.parseFloat(sp[Data.paramsIndex.get("vertical-speed-fps")]));
							});

		            		}		           
			
		            		}
		        		      catch (IOException e) {
		        				e.printStackTrace();
		            		}     
		            		
		             		             
	}
	


	public void activeTab(String tab) {
		activeTabsHash.forEach((s,b) -> {
			activeTabsHash.put(s, new AtomicBoolean(false));
			if (s.equals(tab))
				activeTabsHash.put(s, new AtomicBoolean(true));
		});
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {e.printStackTrace();}
	}
	
	
	

	
	public HashMap<String, String> initHashMap(String propFileName,HashMap<String, String> prop)
	{
		
		try {
			prop=new HashMap<>();
			BufferedReader in=new BufferedReader(new FileReader(propFileName));
			String line;
				while((line=in.readLine())!=null)
				{
					String sp[]= line.split(",");
					prop.put(sp[0], sp[1]);
				}
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		return prop;	
	}
	
	
	//*******************************fleet overView*********************************
	
public void fleetActive() {
		
		this.activeTab("fleet");
		switchTab=false;
	try {
		Thread.sleep(1000);
	} catch (InterruptedException e) {e.printStackTrace();}

	while(activeTabsHash.get("fleet").get() == true)
        {
			System.out.println("fleet tab");
			try {
			Thread.sleep(2500);

    	 	out2TabSock.println("get planes");
			 planes=(ArrayList<Plane>) ob.readObject();
//			System.out.println(planes.id());
//			

				out2TabSock.println("get FleetSize");
		       fleetSize=(HashMap<Integer, Integer>)ob.readObject();
//
			  out2TabSock.println("get MilesPerMonth");
			  miles=(HashMap<String, Double>)ob.readObject();

			  out2TabSock.println("get MilesPerMonthYear");
		      milesYear= (HashMap<Integer,Double>)ob.readObject();

		     } catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
		     } catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
        
	}

	
	public void startFlight() {
		out2TabSock.println("start flight");
		System.out.println("start flight");
	}
	public void endFlight() {
		out2TabSock.println("end flight");
		System.out.println("end flight");
	}
	
	public void GetPlaneProp() {
		
		out2TabSock.println("get planes");
		try {
			planes=(ArrayList<Plane>) ob.readObject();
			System.out.println(planes.toString());
		 	Plane p=(Plane)ob.readObject();
		 	System.out.println(p.toString());
		 	
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	
	}
	
	public ArrayList<Plane> getPlanes()
	{
		return planes;
	}
	
	
	//******************************************************************************
	
	
	//*******************************monitoring*********************************
	
	public void moniActive() {
		try {	
		this.activeTab("moni");	
	//	out2TabSock.println("start flight");
		out2TabSock.println("set agent "+ currentId);
		switchTab=false;
		Thread.sleep(1000);
		switchTab=true;
		es.execute(()->streamSocket());
		
		while(activeTabsHash.get("moni").get() == true)
        {
			System.out.println("moni tab");
			Thread.sleep(500);

			//  	 out2TabSock.println("set aileron 0.3333");

			//aileron
	    	 //out2TabSock.println("get aileron");
	    	 Platform.runLater(()->{

   				//String value = opIn.readLine();
   				//moniAileron.set(Double.parseDouble(value));
				 if (sp.length==42)
					 moniAileron.set(Double.parseDouble(sp[data.paramsIndex.get("aileron")]));
   			 });
			System.out.println("aileron "+moniAileron.getValue());

    		//elevator
    		//Thread.sleep(500);
    		//out2TabSock.println("get elevators");
			 Platform.runLater(()->{
				 if (sp.length==42)
					 moniElevators.set(Double.parseDouble(sp[data.paramsIndex.get("elevator")]));
		  });
			System.out.println("elevator "+moniElevators.getValue());

        	//rudder
        	//out2TabSock.println("get rudder");
			//Thread.sleep(500);
			Platform.runLater(()->{
				 if (sp.length==42)
					 moniRudder.set(Double.parseDouble(sp[data.paramsIndex.get("rudder")]));
				//String val = opIn.readLine();
	         	//moniRudder.set(Double.parseDouble(val));
				  });	
			 System.out.println("rudder "+moniRudder.getValue());

        	//throttle
			// Thread.sleep(500);
			 //out2TabSock.println("get throttle");
			 Platform.runLater(()->{
				 if (sp.length==42)
				 	moniThrottle.set(Double.parseDouble(sp[data.paramsIndex.get("throttle1")]));
			 });
			 System.out.println("throttle "+moniThrottle.getValue());
        }
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//******************************************************************************
	
	
	//*******************************teleopration*********************************
	
	
	public void teleActive() {
		this.activeTab("tele");
		switchTab=false;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {e.printStackTrace();}
		switchTab=true;
		es.execute(()->streamSocket());
//		while(activeTabsHash.get("tele").get() == true)
//        {
//			try {
//				Thread.sleep(2500);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			System.out.println("tele");
//        }
	}
	
	public void sendTeleText() {
		
		out2TabSock.println("run script");

//		try {
//			BufferedReader in=new BufferedReader(new FileReader("teleoprationText.txt"));
//			String line;
//			while((line=in.readLine())!=null)
//			{
//				System.out.println(line);
//				out2agent.println(line);
//				out2agent.flush();
//			}
//			in.close();
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
	
	public void setAlieron(double nv) {
		String command=prop.get("aileron");
		out2TabSock.println(command+" "+nv);
		System.out.println(command+" "+nv);
		
	}

	public void setElevators(double nv) {
		String command=prop.get("elevators");
		out2TabSock.println(command+" "+nv);
		System.out.println(command+" "+nv);

	}

	public void setRudder(double nv) {
		String command=prop.get("rudder");
		out2TabSock.println(command+" "+nv);
		System.out.println(command+" "+nv);
	}

	public void setThrottle(double nv) {
		
		String command=prop.get("throttle");
		out2TabSock.println("get throttle");
		try {
			float throttle= Float.parseFloat(opIn.readLine());
			
//		if(throttle==0 && nv!=0.0) {
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			out2TabSock.println("start flight");
//		}
		out2TabSock.println(command+" "+nv);
		
		if(throttle!=0 && nv==0.0)
			out2TabSock.println("end flight");

	
		System.out.println(command+" "+nv);

		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	
	
	//*******************************time capsule*********************************
	
	
	//read the csv -> flight recovery
	
	public void flightRecovery(String ip,int port,String fileName,int rate) {
		
		try {
			Socket fg=new Socket(ip,port);
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			PrintWriter out=new PrintWriter(fg.getOutputStream());
			String line;
			while((line=in.readLine())!=null) {
				out.println(line);
				out.flush();
				Thread.sleep(rate);
			}
			out.close();
			in.close();
			fg.close();
		
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
		
		public void tcActive() {
			
			this.activeTab("tc");		
			while(activeTabsHash.get("tc").get() == true)
	        {
				try {
					Thread.sleep(2500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("tc");

	        }
		}

	
	//******************************************************************************
}
