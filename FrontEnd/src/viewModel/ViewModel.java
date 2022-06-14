package viewModel;

import java.util.Observer;

import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import model.Model;

public class ViewModel implements Observer{
	
	public	Model m;
	
	public DoubleProperty aileron;
	public DoubleProperty elevators;
	public DoubleProperty rudder;
	public DoubleProperty throttle;
	
	public DoubleProperty verticalSpeed;
	public FloatProperty headDeg,rollDeg,longitude,airspeed,altitude;
	
	public IntegerProperty isRunPushed;
	
	
	
	
	public ViewModel(Model m) {
		// TODO Auto-generated constructor stub
	
		this.m=m;
		m.addObserver(this);
		aileron=new SimpleDoubleProperty();
		elevators=new SimpleDoubleProperty();
		rudder=new SimpleDoubleProperty();
		throttle=new SimpleDoubleProperty();
		isRunPushed=new SimpleIntegerProperty();
		
		altitude=new SimpleFloatProperty();
		headDeg=new SimpleFloatProperty();
		rollDeg=new SimpleFloatProperty();
		longitude=new SimpleFloatProperty();
		verticalSpeed=new SimpleDoubleProperty();
		airspeed=new SimpleFloatProperty();
		
		
		aileron.addListener((o,ov,nv)->m.setAlieron((double)nv));
		elevators.addListener((o,ov,nv)->m.setElevators((double)nv));
		rudder.addListener((o,ov,nv)->m.setRudder((double)nv));
		throttle.addListener((o,ov,nv)->m.setThrottle((double)nv));
		isRunPushed.addListener((o,ov,nv)->m.sendTeleText());
		
		verticalSpeed.bind(m.verticalSpeed);
		altitude.bind(m.altitude);
		altitude.addListener((o,ov,nv)->m.altitude.setValue(altitude.getValue()));
		headDeg.addListener((o,ov,nv)->m.headDeg.setValue(headDeg.getValue()));
		rollDeg.addListener((o,ov,nv)->m.rollDeg.setValue(rollDeg.getValue()));
		longitude.addListener((o,ov,nv)->m.longitude.setValue(longitude.getValue()));
		//verticalSpeed.addListener((o,ov,nv)->m.verticalSpeed.setValue(verticalSpeed.getValue()));
		airspeed.addListener((o,ov,nv)->m.airspeed.setValue(airspeed.getValue()));
//		System.out.println("vs= "+verticalSpeed.floatValue());
	}

	@Override
	public void update(java.util.Observable o, Object arg) {
		
		
	}
}
