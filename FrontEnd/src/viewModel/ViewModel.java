package viewModel;

import java.util.Observer;

import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import model.Model;

public class ViewModel implements Observer{
	
	public	Model m;
	
	public DoubleProperty aileron;
	public DoubleProperty elevators;
	public DoubleProperty rudder;
	public DoubleProperty throttle;
	
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
		
		aileron.addListener((o,ov,nv)->m.setAlieron((double)nv));
		elevators.addListener((o,ov,nv)->m.setElevators((double)nv));
		rudder.addListener((o,ov,nv)->m.setRudder((double)nv));
		throttle.addListener((o,ov,nv)->m.setThrottle((double)nv));
		isRunPushed.addListener((o,ov,nv)->m.sendTeleText());

	}

	@Override
	public void update(java.util.Observable o, Object arg) {
		
		
	}
}
