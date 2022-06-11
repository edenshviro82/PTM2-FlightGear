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

public class Model extends Observable {

String propFileName;
HashMap<String, String> prop;
HashMap<String, String> streamprop;
Socket agent,streamSock;
String sp[];
PrintWriter out2agent,out2streamSock;
boolean flightstart;
float altitude,headDeg,rollDeg,longitude,verticalSpeed,airspeed;
Data data;


//the constructor read the file and connect to the flightgear as a Socket
	public Model(String propFileName) {
		
		data=new Data();
		flightstart=false;
		prop=initHashMap(propFileName,prop);
		int port= Integer.parseInt(prop.get("port"));
		try {
			//The port is 5403 in order to connect to the agent client socket
			agent=new Socket(prop.get("ip"),port); 
			out2agent=new PrintWriter(agent.getOutputStream());
			System.out.println("Connected to the agent");			
		} catch (IOException e) {
			e.printStackTrace();
		}
		streamSocket("streamprop.txt");
	}

	
	//get stream from back-end
	public void streamSocket(String propFileName) {
		 
		System.out.println("stream socket");
		streamprop=initHashMap(propFileName,streamprop);
		int port= Integer.parseInt(streamprop.get("streamPort"));
		try {
			String line=null;
			//The port is 5401 in order to connect to the backEnd stream socket
			 streamSock=new Socket(streamprop.get("ip"),port); 
             BufferedReader  in = new BufferedReader(new InputStreamReader(streamSock.getInputStream()));
			 OutputStream out = streamSock.getOutputStream();
             out2streamSock = new PrintWriter(new OutputStreamWriter(out),true);
             System.out.println("Connected to the stream sock");	
             out2streamSock.println("get stream");
             //out2streamSock.flush();
             line = in.readLine();
             line = in.readLine();
             sp= line.split(",");
             
             //altitude-ft - high, vertical-speed-fps , airspeed-kt, heading-deg , roll-deg ,longitude-deg
             altitude=Float.parseFloat(sp[data.paramsIndex.get("altitude-ft")]);
             airspeed=Float.parseFloat(sp[data.paramsIndex.get("airspeed-kt")]);
             headDeg=Float.parseFloat(sp[data.paramsIndex.get("heading-deg")]);
             longitude=Float.parseFloat(sp[data.paramsIndex.get("longitude-deg")]);
             rollDeg=Float.parseFloat(sp[data.paramsIndex.get("roll-deg")]);
             verticalSpeed=Float.parseFloat(sp[data.paramsIndex.get("vertical-speed-fps")]);
             
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
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
	
	
	//******************************************************************************
	
	
	//*******************************monitoring*********************************
	
	
	//******************************************************************************
	
	
	//*******************************teleopration*********************************
	
	
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
}
