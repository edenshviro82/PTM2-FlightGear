package model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Observable;

public class Model extends Observable {

String ip;
int port;
String propFileName;
int rate;
HashMap<String, String> prop;
Socket agent;
PrintWriter out2agent;

//the constructor read the file and connect to the flightgear as a Socket
	public Model(String propFileName) {
	
		prop=new HashMap<>();
		try {
			BufferedReader in=new BufferedReader(new FileReader(propFileName));
			String line;
			while((line=in.readLine())!=null)
			{
				String sp[]= line.split(",");
				prop.put(sp[0], sp[1]);
			}	
			in.close();
			} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("the prop file has been read at port "+ prop.get("port"));
		int port= Integer.parseInt(prop.get("port"));
		try {
			//The port is 5403 in order to connect to the agent client socket
			agent=new Socket(prop.get("ip"),port); 
			out2agent=new PrintWriter(agent.getOutputStream());
			System.out.println("Connected to the fg");
			 
			
		} catch (IOException e) {
			e.printStackTrace();
		}
			
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
		System.out.println(command+" "+nv);
		out2agent.flush();
	}
	
	//******************************************************************************
}
