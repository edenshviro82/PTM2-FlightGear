package viewClocks;
import java.net.URL;
import java.net.URL;
import java.util.ResourceBundle;
import eu.hansolo.medusa.FGauge;
import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import eu.hansolo.medusa.TickLabelOrientation;
import eu.hansolo.medusa.skins.ModernSkin;
import eu.hansolo.medusa.skins.SpaceXSkin;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ClockController implements Initializable{

	@FXML
	Canvas canvasClocks;
	@FXML
	public Gauge clock1;
	@FXML
	public Gauge clock2;
	@FXML
	public Gauge clock3;
	@FXML
	public Gauge clock4;
	@FXML
	public Gauge clock5;
	@FXML
	public Gauge clock6;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		//gauge=new Gauge();
		clock1.setSkin(new ModernSkin(clock1));  //ModernSkin : you guys can change the skin
		clock1.setTitle("altitude");  //title
		clock1.setUnit("Km / h");  //unit
		clock1.setUnitColor(Color.WHITE);
        clock1.setDecimals(0); 
        clock1.setValue(50.00); //deafult position of needle on gauage
        clock1.setAnimated(true);
        //gauge.setAnimationDuration(500); 

        clock1.setValueColor(Color.WHITE); 
        clock1.setTitleColor(Color.WHITE); 
        clock1.setSubTitleColor(Color.WHITE); 
        clock1.setBarColor(Color.rgb(0, 214, 215)); 
        clock1.setNeedleColor(Color.RED); 
        clock1.setThresholdColor(Color.RED);  //color will become red if it crosses threshold value
        clock1.setThreshold(85);
        clock1.setThresholdVisible(true);
        clock1.setTickLabelColor(Color.rgb(151, 151, 151)); 
        clock1.setTickMarkColor(Color.WHITE); 
        clock1.setTickLabelOrientation(TickLabelOrientation.ORTHOGONAL);
		
		
		//**************************************************
		clock2.setSkin(new ModernSkin(clock2));  //ModernSkin : you guys can change the skin
		clock2.setTitle("headDeg");  //title
		clock2.setUnit("Degrees");  //unit
		clock2.setUnitColor(Color.WHITE);
        clock2.setDecimals(0); 
        clock2.setValue(56.00); //deafult position of needle on gauage
        
        clock2.setAnimated(true);
        //gauge.setAnimationDuration(500); 

        clock2.setValueColor(Color.WHITE); 
        clock2.setTitleColor(Color.WHITE); 
        clock2.setSubTitleColor(Color.WHITE); 
        clock2.setBarColor(Color.rgb(0, 214, 215)); 
        clock2.setNeedleColor(Color.RED); 
        clock2.setThresholdColor(Color.RED);  //color will become red if it crosses threshold value
        clock2.setThreshold(85);
        clock2.setThresholdVisible(true);
        clock2.setTickLabelColor(Color.rgb(151, 151, 151)); 
        clock2.setTickMarkColor(Color.WHITE); 
        clock2.setTickLabelOrientation(TickLabelOrientation.ORTHOGONAL);
		
		//***************************************************
        
        clock3.setSkin(new ModernSkin(clock3));  //ModernSkin : you guys can change the skin
		clock3.setTitle("rollDeg");  //title
		clock3.setUnit("Degrees");  //unit
		clock3.setUnitColor(Color.WHITE);
        clock3.setDecimals(0); 
        clock3.setValue(90.00); //deafult position of needle on gauage
        
        clock3.setAnimated(true);
        //gauge.setAnimationDuration(500); 

        clock3.setValueColor(Color.WHITE); 
        clock3.setTitleColor(Color.WHITE); 
        clock3.setSubTitleColor(Color.WHITE); 
        clock3.setBarColor(Color.rgb(0, 214, 215)); 
        clock3.setNeedleColor(Color.RED); 
        clock3.setThresholdColor(Color.RED);  //color will become red if it crosses threshold value
        clock3.setThreshold(85);
        clock3.setThresholdVisible(true);
        clock3.setTickLabelColor(Color.rgb(151, 151, 151)); 
        clock3.setTickMarkColor(Color.WHITE); 
        clock3.setTickLabelOrientation(TickLabelOrientation.ORTHOGONAL);
		
        //***************************************************
        clock4.setSkin(new ModernSkin(clock4));  //ModernSkin : you guys can change the skin
		clock4.setTitle("longitude");  //title
		clock4.setUnit("KM");  //unit
		clock4.setUnitColor(Color.WHITE);
        clock4.setDecimals(0); 
        clock4.setValue(58.00); //deafult position of needle on gauage
        
        clock4.setAnimated(true);
        //gauge.setAnimationDuration(500); 

        clock4.setValueColor(Color.WHITE); 
        clock4.setTitleColor(Color.WHITE); 
        clock4.setSubTitleColor(Color.WHITE); 
        clock4.setBarColor(Color.rgb(0, 214, 215)); 
        clock4.setNeedleColor(Color.RED); 
        clock4.setThresholdColor(Color.RED);  //color will become red if it crosses threshold value
        clock4.setThreshold(85);
        clock4.setThresholdVisible(true);
        clock4.setTickLabelColor(Color.rgb(151, 151, 151)); 
        clock4.setTickMarkColor(Color.WHITE); 
        clock4.setTickLabelOrientation(TickLabelOrientation.ORTHOGONAL);
		
		
      //***************************************************
        clock5.setSkin(new ModernSkin(clock5));  //ModernSkin : you guys can change the skin
		clock5.setTitle("verticalSpeed");  //title
		clock5.setUnit("KM");  //unit
		clock5.setUnitColor(Color.WHITE);
        clock5.setDecimals(0); 
        clock5.setValue(50.00); //deafult position of needle on gauage
        
        clock5.setAnimated(true);
        //gauge.setAnimationDuration(500); 

        clock5.setValueColor(Color.WHITE); 
        clock5.setTitleColor(Color.WHITE); 
        clock5.setSubTitleColor(Color.WHITE); 
        clock5.setBarColor(Color.rgb(0, 214, 215)); 
        clock5.setNeedleColor(Color.RED); 
        clock5.setThresholdColor(Color.RED);  //color will become red if it crosses threshold value
        clock5.setThreshold(85);
        clock5.setThresholdVisible(true);
        clock5.setTickLabelColor(Color.rgb(151, 151, 151)); 
        clock5.setTickMarkColor(Color.WHITE); 
        clock5.setTickLabelOrientation(TickLabelOrientation.ORTHOGONAL);
		
        
      //***************************************************
        clock6.setSkin(new ModernSkin(clock6));  //ModernSkin : you guys can change the skin
		clock6.setTitle("Air Speed");  //title
		clock6.setUnit("KM");  //unit
		clock6.setUnitColor(Color.WHITE);
        clock6.setDecimals(0); 
        clock6.setValue(50.00); //deafult position of needle on gauage
        
        clock6.setAnimated(true);
        //gauge.setAnimationDuration(500); 

        clock6.setValueColor(Color.WHITE); 
        clock6.setTitleColor(Color.WHITE); 
        clock6.setSubTitleColor(Color.WHITE); 
        clock6.setBarColor(Color.rgb(0, 214, 215)); 
        clock6.setNeedleColor(Color.RED); 
        clock6.setThresholdColor(Color.RED);  //color will become red if it crosses threshold value
        clock6.setThreshold(85);
        clock6.setThresholdVisible(true);
        clock6.setTickLabelColor(Color.rgb(151, 151, 151)); 
        clock6.setTickMarkColor(Color.WHITE); 
        clock6.setTickLabelOrientation(TickLabelOrientation.ORTHOGONAL);
		
	}
	
	

}
			
