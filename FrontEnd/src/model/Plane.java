package model;

public class Plane {

	public String id;
	public double hd;
	public double alt;
	public double lan;
	public double speed;
	
	
	
	public Plane(String props) {
		//["2,275,15,80,1000"]
		
		String[] arr=props.split(",");
		this.id=arr[0];
		this.hd=Double.parseDouble(arr[1]);
		this.alt=Double.parseDouble(arr[2]);
		this.lan=Double.parseDouble(arr[3]);
		this.speed=Double.parseDouble(arr[4]);
		
		
	}
	
	public Plane(String id, double hd, double alt, double lan, double speed) {
		super();
		this.id = id;
		this.hd = hd;
		this.alt = alt;
		this.lan = lan;
		this.speed = speed;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public double getHd() {
		return hd;
	}
	public void setHd(double hd) {
		this.hd = hd;
	}
	public double getAlt() {
		return alt;
	}
	public void setAlt(double alt) {
		this.alt = alt;
	}
	public double getLan() {
		return lan;
	}
	public void setLan(double lan) {
		this.lan = lan;
	}
	public double getSpeed() {
		return speed;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	@Override
	public String toString() {
		
		return "id: "+id +", hd: "+hd+", alt: "+alt+", lan: "+lan+", speed: "+speed;
	}
	
}
