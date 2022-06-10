package viewClocks;
import java.net.URL;
import java.net.URL;
import java.util.ResourceBundle;
import eu.hansolo.medusa.FGauge;
import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import eu.hansolo.medusa.TickLabelOrientation;
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
Gauge clock2;
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
//	       GaugeBuilder builder = GaugeBuilder.create();
//	       clock1 = builder.decimals(0).maxValue(10000).unit("Questions").build();
//	       clock1.setValue(45);
//		clock1.setDecimals(0);
//		clock1.setMaxValue(10000);
//		clock1.setUnit("Questions");
//		clock1.setValue(45);
		
	}

}
			
//		Button btn=new Button();
//		btn.setText("dedn");
//		btn.setTranslateX(10);
//		btn.setTranslateY(200);
//		btn.setOnAction(new EventHandler<ActionEvent>() {
//			
//			@Override
//			public void handle(ActionEvent event) {
//					System.out.println("hello ");
//					gauge.setAnimated(true);
//					gauge.setValue(90);		
//			}
//		});
//		
//		gauge.setSkin(new SpaceXSkin(gauge));
//		gauge.setTitle("clocks");
//		gauge.setUnit("km/hour");
//		gauge.setUnitColor(Color.WHITE);
//		gauge.setDecimals(0);
//		gauge.setValue(50.00);
//		gauge.setAnimated(true);
//		gauge.setValueColor(Color.WHITE);
//		gauge.setTitleColor(Color.WHITE);
//		gauge.setSubTitleColor(Color.WHITE);
//		gauge.setBarColor(Color.rgb(0,214,215));
//		gauge.setNeedleColor(Color.RED);
//		gauge.setThresholdColor(Color.RED);
//		gauge.setThreshold(85);
//		gauge.setThresholdVisible(true);
//		gauge.setTickLabelColor(Color.rgb(151,151,151));
//		gauge.setTickMarkColor(Color.WHITE);
//		gauge.setTickLabelOrientation(TickLabelOrientation.ORTHOGONAL);
//		
//		
//		StackPane root=new StackPane();
//		root.getChildren().addAll(gauge);
//		root.getChildren().addAll(btn);
//		Scene scene=new Scene(root,300,250);
//		Stage stage = new Stage();
//		stage.setTitle("gaugeEx");
//		stage.setScene(scene);
//		stage.show();
//
//		

