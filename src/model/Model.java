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
import necessary_classes.Plane;

public class Model extends Observable {

static ConcurrentHashMap<String, AtomicBoolean> activeTabsHash;
String propFileName;
HashMap<String, String> prop;
HashMap<String, String> streamprop;
HashMap<String, String> tabHash;
Socket agent,streamSock,tabSock;
String sp[];
PrintWriter out2agent,out2streamSock,out2TabSock;
boolean flightstart;
public FloatProperty altitude,headDeg,rollDeg,longitude,verticalSpeed,airspeed;
public DoubleProperty moniAileron;
public DoubleProperty moniRudder;
public DoubleProperty moniThrottle;
public DoubleProperty moniElevators;
Data data;
volatile boolean stopStream;
ExecutorService es;
public IntegerProperty isFleetPushed;
public IntegerProperty isMoniPushed;
public IntegerProperty isTelePushed;
public IntegerProperty isTimeCapsulePushed;
public ObjectInputStream ob;
public BufferedReader in;
public ArrayList<Plane> planes;
public  HashMap<Integer,Integer> fleetSize;
public HashMap<Integer,Double> milesYear;
public Map<String,Double> miles;

public String currentId;


//the constructor read the file and connect to the flightgear as a Socket
	public Model(String propFileName) {
		activeTabsHash = new ConcurrentHashMap<>();
	
		fleetSize=new HashMap<>();
		milesYear=new HashMap<>();
		miles=new HashMap<>();
		
		this.initActiveTabHash();
		longitude=new SimpleFloatProperty(0);
		rollDeg=new SimpleFloatProperty(0);
		headDeg=new SimpleFloatProperty(0);
		altitude=new SimpleFloatProperty(0);
		verticalSpeed=new SimpleFloatProperty(0);
		airspeed=new SimpleFloatProperty(0);
		
		moniAileron=new SimpleDoubleProperty(0);
		moniElevators=new SimpleDoubleProperty(0);
		moniRudder=new SimpleDoubleProperty(0);
		moniThrottle=new SimpleDoubleProperty(0);
		//run at another thread
		es = Executors.newFixedThreadPool(2);
		isFleetPushed = new SimpleIntegerProperty();
		isMoniPushed = new SimpleIntegerProperty();
		isTelePushed = new SimpleIntegerProperty();
		isTimeCapsulePushed = new SimpleIntegerProperty();

		
		planes= new ArrayList<>();
		stopStream=false;
		data=new Data();
		flightstart=false;
		
//		streamSocket("streamprop.txt");
		System.out.println("tabHashMap");
		tabHash=initHashMap("streamprop.txt",tabHash);
		prop=initHashMap("streamprop.txt", prop);
		int port= Integer.parseInt(tabHash.get("streamPort"));
		
		try {
		tabSock=new Socket(tabHash.get("ip"),port);
          in = new BufferedReader(new InputStreamReader(tabSock.getInputStream()));
		 OutputStream out = tabSock.getOutputStream();
        out2TabSock = new PrintWriter(new OutputStreamWriter(out),true);// auto flush
        ob = new ObjectInputStream(tabSock.getInputStream());
       
        
        
		
        	
        Thread.sleep(2000);
        out2TabSock.println("start flight");
        Thread.sleep(2000);
        out2TabSock.println("get pitch");
        String val=in.readLine();
        //System.out.println(val);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        

	}

	
	private void initActiveTabHash() {
	// TODO Auto-generated method stub
	activeTabsHash.put("fleet", new AtomicBoolean(false));
	activeTabsHash.put("moni", new AtomicBoolean(false));
	activeTabsHash.put("tele", new AtomicBoolean(false));
	activeTabsHash.put("tc", new AtomicBoolean(false));
}


	//get stream from back-end
	public void streamSocket(String propFileName) {
	
		System.out.println("stream socket");
		streamprop=initHashMap(propFileName,streamprop);
		int port= Integer.parseInt(streamprop.get("streamPort"));
		
			es.execute(()-> {

				try {
					//The port is 5401 in order to connect to the backEnd stream socket
					streamSock=new Socket(streamprop.get("ip"),port); 
					BufferedReader in = new BufferedReader(new InputStreamReader(streamSock.getInputStream()));
					 OutputStream out = streamSock.getOutputStream();
		             out2streamSock = new PrintWriter(new OutputStreamWriter(out),true);// auto flush
		             
		             System.out.println("Connected to the stream sock->agent controller");	
		           	
		             Thread.sleep(2000);
		             out2streamSock.println("start flight");
		             
		            	while(!stopStream) {
			            	
							Thread.sleep(Long.parseLong(streamprop.get("rate")));
		            		out2streamSock.println("get stream");
							String line = in.readLine();
							//System.out.println(line);
							
				            sp= line.split(",");
				             
				            
				       	 Platform.runLater(()->{
							 
								   //altitude-ft - high, vertical-speed-fps , airspeed-kt, heading-deg , roll-deg ,longitude-deg
						             altitude.set(Float.parseFloat(sp[data.paramsIndex.get("altitude-ft")]));
						             airspeed.set(Float.parseFloat(sp[data.paramsIndex.get("airspeed-kt")]));
						             headDeg.set(Float.parseFloat(sp[data.paramsIndex.get("heading-deg")]));
						             longitude.set(Float.parseFloat(sp[data.paramsIndex.get("longitude-deg")]));
						             rollDeg.set(Float.parseFloat(sp[data.paramsIndex.get("roll-deg")]));
						             verticalSpeed.set(Float.parseFloat(sp[data.paramsIndex.get("vertical-speed-fps")]));
						             
							  
							  });
			          
		            		}
		            	
							}catch (InterruptedException e1) {
		            			e1.printStackTrace();
		            			System.out.println("socket ec");
		        			}
		        		      catch (IOException e) {
		        				e.printStackTrace();
		            			System.out.println("sleep ec");

		        			}
		            	
		            			     
		            	});		
		             		             
	}
	
	
	public void fleetActive() {
		
		this.activeTab("fleet");
		
		
		while(activeTabsHash.get("fleet").get() == true)
        {
			System.out.println("fleet tab");
			try {
			Thread.sleep(2500);
			
			//Plane p;
//    	 	out2TabSock.println("get planes");
//			planes=(ArrayList<Plane>) ob.readObject();
//			System.out.println(planes.toString());
			
//			p = (Plane)ob.readObject();
//			System.out.println(p.getFlightID());
//			} catch (ClassNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
			
//			  out2TabSock.println("get MilesPerMonth");
//			  miles = (Map<String, Double>)ob.readObject();
//		            
//			  out2TabSock.println("get MilesPerMonthYear");
//		      milesYear= (HashMap<Integer, Double>)ob.readObject();
//		      
//		      out2TabSock.println("get getFleetSize");
//		      HashMap<Integer,Integer> fleetSize=(HashMap<Integer, Integer>)ob.readObject();

		           
			
//		     } catch (ClassNotFoundException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//		     } catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
			}catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				}
			
			
			
			
        }
        
	}
	
	public void moniActive() {
		
		this.activeTab("moni");		
		out2TabSock.println("set agent"+ currentId);
		while(activeTabsHash.get("moni").get() == true)
        {
			System.out.println("moni tab");
        	try {
				Thread.sleep(2000);
	    	 	
	    	 	//aileron
	    	 	out2TabSock.println("get aileron");
	    	 	Platform.runLater(()->{
   			  try {
   				String value = in.readLine();
   				moniAileron.set(Double.parseDouble(value));
   			  } catch (IOException e) {
   					e.printStackTrace();
   				}
   			  });	
    		 System.out.println("aileron "+moniAileron.getValue());
    		Thread.sleep(2000);

    		//elevator
    		Thread.sleep(500);
    		out2TabSock.println("get elevators");
			 Platform.runLater(()->{
			  try {
			String val = in.readLine();
         	moniElevators.set(Double.parseDouble(val));
			  } catch (IOException e) {
					e.printStackTrace();
				}
			  });	
			 System.out.println("elevator "+moniElevators.getValue());
        	Thread.sleep(2000);

        	//rudder
        	Thread.sleep(500);
        	out2TabSock.println("get rudder");
			 Platform.runLater(()->{
				  try {
				String val = in.readLine();
	         	moniRudder.set(Double.parseDouble(val));
				  } catch (IOException e) {
						e.printStackTrace();
					}
				  });	
			 System.out.println("rudder "+moniRudder.getValue());
        		Thread.sleep(2000);

        		//throttle
			 Thread.sleep(500);
			 out2TabSock.println("get throttle");
			 Platform.runLater(()->{
				  try {
					 String value = in.readLine();
					moniThrottle.set(Double.parseDouble(value));
				  } catch (IOException e) {
						e.printStackTrace();
					}
				  });	
			 System.out.println("throttle "+moniThrottle.getValue());
        	Thread.sleep(2000);
        	
        	
        	
        	} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        }
	}
	
	public void teleActive() {
		this.activeTab("tele");

		
		while(activeTabsHash.get("tele").get() == true)
        {
			try {
				Thread.sleep(2500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("tele");
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
	
	public void activeTab(String tab) {
		activeTabsHash.forEach((s,b) -> {
			activeTabsHash.put(s, new AtomicBoolean(false));
			if (s.equals(tab))
				activeTabsHash.put(s, new AtomicBoolean(true));
		});
	}
	
	
//	public void CurrentTab()
//	{
//		
//
//				try {
					//The port is 5401 in order to connect to the backEnd stream socket
				
		             
//		             es.execute(()-> fleetActive());
//		             es.execute(()-> moniActive());
//		             es.execute(()-> teleActive());
//		             es.execute(()-> tcActive());
		             
		             
		             
//		             while(isFleetPushed.get()==1)
//		             {
//		            	 System.out.println("fleet tab");
//							Thread.sleep(250); 
//		            	 	out2TabSock.println("get planes");
////							planes=(ArrayList<Plane>) ob.readObject();
////							System.out.println(planes.toString());
//		            	 	Plane p=(Plane)ob.readObject();
//		            	 	System.out.println(p.toString());
//		            	 
//		             }
//		             
//		             while(isMoniPushed.get()==1)
//		             {
//		            	 System.out.println("moni tab");
//			            	Thread.sleep(2000);
//		            	 	out2TabSock.println("get aileron");
//		            		String value = in.readLine();
//		            		 int d=Integer.parseInt(value);
//		            		 moniAileron.set(d);
//		            		 System.out.println("aileron "+moniAileron.getValue());
//		            		Thread.sleep(2000);
//
//		            		Thread.sleep(500);
//		            		out2TabSock.println("get elevators");
//							 value = in.readLine();
//							 moniElevators.set(Double.parseDouble(value));
//							 System.out.println("elevator "+moniElevators.getValue());
//			            		Thread.sleep(2000);
//
//			            		Thread.sleep(500);
//			            		out2TabSock.println("get rudder");
//							 value = in.readLine();
//							 moniRudder.set((Double.parseDouble(value)));
//							 System.out.println("rudder "+moniRudder.getValue());
//			            		Thread.sleep(2000);
//
//							 Thread.sleep(500);
//							 out2TabSock.println("get throttle");
//							 value = in.readLine();
//							 moniThrottle.set(Double.parseDouble(value));
//							 System.out.println("throttle "+moniThrottle.getValue());
//			            		Thread.sleep(2000);
//		            		
//		             }
//		            
//		            
//		             while(isTimeCapsulePushed.get()==1)
//		             {
//		            	 System.out.println("TC tab");
//
//		             }
//		             
//		             
//            	
//					}
//				catch (IOException e) {
//				e.printStackTrace();
//				}
//				catch (ClassNotFoundException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
    	

    	
//	}
	
	
	public ArrayList<Plane> getPlanes()
	{
		return planes;
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
	
	
	//******************************************************************************
	
	
	//*******************************monitoring*********************************
	
	
	//******************************************************************************
	
	
	//*******************************teleopration*********************************
	
	public void sendTeleText() {
		
		try {
			BufferedReader in=new BufferedReader(new FileReader("teleoprationText.txt"));
			String line;
			while((line=in.readLine())!=null)
			{
				System.out.println(line);
				out2agent.println(line);
				out2agent.flush();
			}
			in.close();
			
		} catch (IOException e) {
			e.printStackTrace();
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
		out2TabSock.println(command+" "+nv);
		if(!flightstart && nv!=0.0)
		{
			flightstart=true;
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			out2TabSock.println("start flight");

		}
		if(flightstart && nv==0.0)
		{
			flightstart=false;
			out2TabSock.println("end flight");

		}
		System.out.println(command+" "+nv);
	}


	
	//******************************************************************************
}
