package model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Model extends Observable {

String propFileName;
HashMap<String, String> prop;
HashMap<String, String> streamprop;
Socket agent,streamSock;
String sp[];
PrintWriter out2agent,out2streamSock;
boolean flightstart;
public FloatProperty altitude,headDeg,rollDeg,longitude,verticalSpeed,airspeed;
public DoubleProperty moniAileron;
public DoubleProperty moniRudder;
public DoubleProperty moniThrottle;
public DoubleProperty moniElevators;
Data data;
volatile boolean stopStream;
ExecutorService es;


//the constructor read the file and connect to the flightgear as a Socket
	public Model(String propFileName) {
	
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
		es = Executors.newFixedThreadPool(3);
		
	
		stopStream=false;
		data=new Data();
		flightstart=false;
		prop=initHashMap(propFileName,prop);
		int port= Integer.parseInt(prop.get("port"));
		/*
		 * try { //The port is 5403 in order to connect to the agent client socket
		 * agent=new Socket(prop.get("ip"),port); out2agent=new
		 * PrintWriter(agent.getOutputStream());
		 * System.out.println("Connected to the agent"); } catch (IOException e) {
		 * e.printStackTrace(); }
		 */
		streamSocket("streamprop.txt");
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
		             BufferedReader  in = new BufferedReader(new InputStreamReader(streamSock.getInputStream()));
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
				             
			             //altitude-ft - high, vertical-speed-fps , airspeed-kt, heading-deg , roll-deg ,longitude-deg
				             altitude.set(Float.parseFloat(sp[data.paramsIndex.get("altitude-ft")]));
				             airspeed.set(Float.parseFloat(sp[data.paramsIndex.get("airspeed-kt")]));
				             headDeg.set(Float.parseFloat(sp[data.paramsIndex.get("heading-deg")]));
				             longitude.set(Float.parseFloat(sp[data.paramsIndex.get("longitude-deg")]));
				             rollDeg.set(Float.parseFloat(sp[data.paramsIndex.get("roll-deg")]));
				             verticalSpeed.set(Float.parseFloat(sp[data.paramsIndex.get("vertical-speed-fps")]));
				             
		            		}
		            	
							}catch (InterruptedException e1) {
		            			e1.printStackTrace();
		        			}
		        		      catch (IOException e) {
		        				e.printStackTrace();
		        			}
		            	
		            			     
		            	});
			
			es.execute(()-> {

				try {
					//The port is 5404 in order to connect to the backEnd stream socket
					streamSock=new Socket(streamprop.get("ip"),port); 
		             BufferedReader  in = new BufferedReader(new InputStreamReader(streamSock.getInputStream()));
					 OutputStream out = streamSock.getOutputStream();
		             out2streamSock = new PrintWriter(out,true);// auto flush
		             
		             System.out.println("2:Connected to the stream sock->agent controller");	
		           	
		             Thread.sleep(2000);
		             out2streamSock.println("start flight");
		             
	            	Thread.sleep(2000);
	            	out2streamSock.println("get aileron");
            		String value = in.readLine();
            	//	System.out.println(value + "get aileron test string");

		             //System.out.println(v+"first str");
						
		            	while(!stopStream) {
		            		
		            		Thread.sleep(2000);
		            		out2streamSock.println("get pitch");
		            		 value = in.readLine();
		            		 int d=Integer.parseInt(value);
		            		 moniAileron.set(d);
		            		 System.out.println("aileron "+moniAileron.getValue());
		            		Thread.sleep(2000);

		            		Thread.sleep(500);
							out2streamSock.println("get elevators");
							 value = in.readLine();
							 moniElevators.set(Double.parseDouble(value));
							 System.out.println("elevator "+moniElevators.getValue());
			            		Thread.sleep(2000);

			            		Thread.sleep(500);
							out2streamSock.println("get rudder");
							 value = in.readLine();
							 moniRudder.set((Double.parseDouble(value)));
							 System.out.println("rudder "+moniRudder.getValue());
			            		Thread.sleep(2000);

							 Thread.sleep(500);
							out2streamSock.println("get throttle");
							 value = in.readLine();
							 moniThrottle.set(Double.parseDouble(value));
							 System.out.println("throttle "+moniThrottle.getValue());
			            		Thread.sleep(2000);
		            		
		            		
		            

		            		

		            	}
		            	
							}catch (InterruptedException e1) {
		            			e1.printStackTrace();
		        			}
		        		      catch (IOException e) {
		        				e.printStackTrace();
		        			}
		            	
		
		            	});
		
		
		
		
		
		             		             
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
		
		out2streamSock.println("get planes");
		
		
	
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
	
	//******************************************************************************
	
	
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
		out2agent.println(command+" "+nv);
		System.out.println(command+" "+nv);
		out2agent.flush();
		
	}

	public void setElevators(double nv) {
		String command=prop.get("elevators");
		out2agent.println(command+" "+nv);
		out2agent.flush();
		System.out.println(command+" "+nv);

	}

	public void setRudder(double nv) {
		String command=prop.get("rudder");
		out2agent.println(command+" "+nv);
		System.out.println(command+" "+nv);
		out2agent.flush();
	}

	public void setThrottle(double nv) {
		String command=prop.get("throttle");
		out2agent.println(command+" "+nv);
		out2agent.flush();
		if(!flightstart && nv!=0.0)
		{
			flightstart=true;
			out2agent.println("start flight");

		}
		if(flightstart && nv==0.0)
		{
			flightstart=false;
			out2agent.println("end flight");

		}
		System.out.println(command+" "+nv);
		out2agent.flush();
	}


	
	//******************************************************************************
}
