package viewModel;

import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
	public ExecutorService es;

	
	
	public DoubleProperty aileron;
	public DoubleProperty elevators;
	public DoubleProperty rudder;
	public DoubleProperty throttle;
	
	public DoubleProperty verticalSpeed;
	public DoubleProperty headDeg,rollDeg,longitude,airspeed,altitude;

	public DoubleProperty moniAileron;
	public DoubleProperty moniElevators;
	public DoubleProperty moniRudder;
	public DoubleProperty moniThrottle;
	
	public IntegerProperty isRunPushed;

	public IntegerProperty isPlanepushTwice;
	public IntegerProperty isPlanepushOnce;
	
	public IntegerProperty isFleetPushed;
	public IntegerProperty isMoniPushed;
	public IntegerProperty isTelePushed;
	public IntegerProperty isTimeCapsulePushed;
	
	
	public ViewModel(Model m) {
		// TODO Auto-generated constructor stub
	
		this.m=m;
		m.addObserver(this);
		aileron=new SimpleDoubleProperty();
		elevators=new SimpleDoubleProperty();
		rudder=new SimpleDoubleProperty();
		throttle=new SimpleDoubleProperty();
		isRunPushed=new SimpleIntegerProperty();
		
		moniAileron = new SimpleDoubleProperty();
		moniElevators=new SimpleDoubleProperty();
		moniRudder=new SimpleDoubleProperty();
		moniThrottle=new SimpleDoubleProperty();
		
		altitude=new SimpleDoubleProperty();
		headDeg=new SimpleDoubleProperty();
		rollDeg=new SimpleDoubleProperty();
		longitude=new SimpleDoubleProperty();
		verticalSpeed=new SimpleDoubleProperty();
		airspeed=new SimpleDoubleProperty();
		
		isFleetPushed = new SimpleIntegerProperty();
		isMoniPushed = new SimpleIntegerProperty();
		isTelePushed = new SimpleIntegerProperty();
		isTimeCapsulePushed = new SimpleIntegerProperty();

		
		isPlanepushOnce=new SimpleIntegerProperty();
		isPlanepushTwice= new SimpleIntegerProperty();
		
		es = Executors.newFixedThreadPool(3);

		
		isFleetPushed.addListener((o,ov,nv)->es.execute(()-> m.fleetActive()));
		isMoniPushed.addListener((o,ov,nv)->es.execute(()-> m.moniActive()));
		isTelePushed.addListener((o,ov,nv)->es.execute(()-> m.teleActive()));
		isTimeCapsulePushed.addListener((o,ov,nv)->es.execute(()-> m.tcActive()));

		
		
		
		moniAileron.bind(m.moniAileron);
		moniElevators.bind(m.moniElevators);
		moniRudder.bind(m.moniRudder);
		moniThrottle.bind(m.moniThrottle);

		
		
		aileron.addListener((o,ov,nv)->m.setAlieron((double)nv));
		elevators.addListener((o,ov,nv)->m.setElevators((double)nv));
		rudder.addListener((o,ov,nv)->m.setRudder((double)nv));
		throttle.addListener((o,ov,nv)->m.setThrottle((double)nv));
		isRunPushed.addListener((o,ov,nv)->m.sendTeleText());
		
		verticalSpeed.bind(m.verticalSpeed);
		altitude.bind(m.altitude);
		headDeg.bind(m.headDeg);
		rollDeg.bind(m.rollDeg);
		longitude.bind(m.longitude);
		airspeed.bind(m.airspeed);
		
		
		//altitude.addListener((o,ov,nv)->m.altitude.setValue(altitude.getValue()));
		//headDeg.addListener((o,ov,nv)->m.headDeg.setValue(headDeg.getValue()));
		//rollDeg.addListener((o,ov,nv)->m.rollDeg.setValue(rollDeg.getValue()));
		//longitude.addListener((o,ov,nv)->m.longitude.setValue(longitude.getValue()));
		//verticalSpeed.addListener((o,ov,nv)->m.verticalSpeed.setValue(verticalSpeed.getValue()));
		//airspeed.addListener((o,ov,nv)->m.airspeed.setValue(airspeed.getValue()));

		/*
		 altitude.bind(m.altitude);
		altitude.addListener((o,ov,nv)->m.altitude.setValue(altitude.getValue()));
		headDeg.bind(m.headDeg);
		headDeg.addListener((o,ov,nv)->m.headDeg.setValue(headDeg.getValue()));
		rollDeg.bind(rollDeg);
		rollDeg.addListener((o,ov,nv)->m.rollDeg.setValue(rollDeg.getValue()));
		longitude.bind(longitude);
		longitude.addListener((o,ov,nv)->m.longitude.setValue(longitude.getValue()));
		verticalSpeed.bind(m.verticalSpeed);
		verticalSpeed.addListener((o,ov,nv)->m.verticalSpeed.setValue(verticalSpeed.getValue()));
		airspeed.bind(airspeed);
		airspeed.addListener((o,ov,nv)->m.airspeed.setValue(airspeed.getValue()));
		 */
	}

	@Override
	public void update(java.util.Observable o, Object arg) {
		
		
	}
}
