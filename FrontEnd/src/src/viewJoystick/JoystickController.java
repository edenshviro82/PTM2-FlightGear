package viewJoystick;

	import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
	import javafx.scene.control.Label;
	import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Sphere;


	public class JoystickController extends Canvas implements Initializable{
		

		@FXML
		public Slider rudderSlider;
		
		@FXML
		public Slider throttleSlider;
		
		@FXML
		public Circle joystickCircle;
		
		@FXML
		public Label aileronLabel;
		
		@FXML
		public Label elevatorLabel;
		
		@FXML
		public Label throttleLabel;
		
		@FXML
		public Label rudderLabel;
	

		@FXML
		public Canvas canJoystick;
		
		
		Boolean mousePushed;
		double jx,jy,mx,my;
		public DoubleProperty ailerons,elevators;

		
		
		
		public JoystickController() {
		
			mousePushed=false;
			jx=0.0;
			jy=0.0;
			ailerons=new SimpleDoubleProperty(0);
			elevators=new SimpleDoubleProperty(0);
			
		}
		
		void paint() {
			GraphicsContext gc = canJoystick.getGraphicsContext2D();
			 mx=canJoystick.getWidth()/2;
			 my=canJoystick.getHeight()/2;
			gc.clearRect(0, 0, canJoystick.getWidth(), canJoystick.getHeight());
			gc.strokeOval(jx-10,jy-15, 20,20);
			ailerons.set((jx-mx)/mx);
			elevators.set((jy-my)/my);
			
			setLabels();

			
			System.out.println(ailerons+","+elevators);
		}
		
		public void setLabels() {
			aileronLabel.setText(String.format("%.2f",ailerons.doubleValue()));
			elevatorLabel.setText(String.format("%.2f",elevators.doubleValue()));
			throttleLabel.setText(String.format("%.2f",throttleSlider.getValue()));
			rudderLabel.setText(String.format("%.2f",rudderSlider.getValue()));

		}
		
		public void mouseDown(MouseEvent me) {
			if(!mousePushed){
				mousePushed=true;
				System.out.println("mouse is down");
			}
		
		}
		
		public void mouseUp(MouseEvent me) {
				if(mousePushed){
					mousePushed=false;
					System.out.println("mouse is up");
					jx=mx;
					jy=my;
					paint();
					
					
				}
		}
		
		public void mouseMove(MouseEvent me) {
			if(mousePushed){
				jx=me.getX();
				jy=me.getY();
				paint();
			}
			
	}

		@Override
		public void initialize(URL location, ResourceBundle resources) {
			throttleSlider.valueProperty().addListener(new ChangeListener<Number>() {

				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					throttleLabel.setText(String.format("%.4f",throttleSlider.getValue()));

				}
			});
			rudderSlider.valueProperty().addListener(new ChangeListener<Number>() {

				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					rudderLabel.setText(String.format("%.4f",rudderSlider.getValue()));

				}
			});
			
			ailerons.addListener(new ChangeListener<Number>() {

				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					aileronLabel.setText(String.format("%.4f",ailerons.getValue()));

				}
			});
			
			
			elevators.addListener(new ChangeListener<Number>() {

				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					elevatorLabel.setText(String.format("%.4f",elevators.getValue()));

				}
			});
			
			rudderSlider.valueProperty().addListener(new ChangeListener<Number>() {

				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					rudderLabel.setText(String.format("%.4f",rudderSlider.getValue()));

				}
			});
			
		}



	/*
	 * public void init(ViewModel vm) { 
	 * this.vm= vm;
	 * vm.throttle.bind(throttle.valueProperty());
	 * vm.rudder.bind(rudder.valueProperty()); vm.aileron.bind(ailerons);
	 * vm.elevators.bind(elevators);
	 * 
	 * 
	 * 
	 * }
	 */
		
		
		
		
	}

