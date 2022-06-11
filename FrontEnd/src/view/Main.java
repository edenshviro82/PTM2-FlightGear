package view;
	
import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.TickLabelOrientation;
import eu.hansolo.medusa.skins.SpaceXSkin;
import javafx.application.Application;
import javafx.stage.Stage;
import model.Model;
import viewModel.ViewModel;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
		  Parent root = loader.load();
		  
		  Scene scene = new Scene(root,1100,450);
		  scene.getStylesheets().add("application.css");
		  
		  primaryStage.setTitle("Flight gear controller");
		  primaryStage.setScene(scene);
		
		  Model m=new Model("Properties.txt");
		ViewModel vm=new ViewModel(m);
			  
		  MainWindowController controller = loader.getController();
		  controller.setStage(primaryStage);
		  controller.init1(vm);
		  primaryStage.show();

	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
