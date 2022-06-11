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
FGauge clock1;
@FXML
Gauge gauge;
@FXML
Gauge clock3;
@FXML
FGauge clock4;
@FXML
Gauge clock5;
@FXML
FGauge clock6;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		//gauge=new Gauge();
		
		gauge.setSkin(new ModernSkin(gauge));  //ModernSkin : you guys can change the skin
        gauge.setTitle("COOL IT HELP");  //title
        gauge.setUnit("Km / h");  //unit
        gauge.setUnitColor(Color.WHITE);
        gauge.setDecimals(0); 
        gauge.setValue(50.00); //deafult position of needle on gauage
        gauge.setAnimated(true);
        //gauge.setAnimationDuration(500); 

        gauge.setValueColor(Color.WHITE); 
        gauge.setTitleColor(Color.WHITE); 
        gauge.setSubTitleColor(Color.WHITE); 
        gauge.setBarColor(Color.rgb(0, 214, 215)); 
        gauge.setNeedleColor(Color.RED); 
        gauge.setThresholdColor(Color.RED);  //color will become red if it crosses threshold value
        gauge.setThreshold(85);
        gauge.setThresholdVisible(true);
        gauge.setTickLabelColor(Color.rgb(151, 151, 151)); 
        gauge.setTickMarkColor(Color.WHITE); 
        gauge.setTickLabelOrientation(TickLabelOrientation.ORTHOGONAL);
		
		
		

		
	}

}
			
