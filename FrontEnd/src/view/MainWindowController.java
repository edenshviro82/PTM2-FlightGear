package view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale.Category;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.TickLabelOrientation;
import eu.hansolo.medusa.skins.SpaceXSkin;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import viewJoystick.*;
import viewModel.ViewModel;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.Plane;
//import viewClocks.ClockController;


public class MainWindowController implements Initializable,Observer{

//********************************************fleet Overview***************************************	

	public ViewModel vm;
	
	private Stage stage;

	
	
	@FXML
	private PieChart pieChart;

	@FXML
	private BarChart<String,Double> barChart;
	@FXML
	private CategoryAxis x;
	@FXML
	private NumberAxis y;

	@FXML
	private BarChart<String,Double> barChartYearly;
	@FXML
	private CategoryAxis x1;
	@FXML
	private NumberAxis y1;
	
	@FXML
	private LineChart<String, Double> fleetLineChart;
	@FXML
	private CategoryAxis xl;
	@FXML
	private NumberAxis yl;
	
	
	@FXML
	Canvas mapCanvas;	
	
	//************************************************************************************************
	
	//***************************************teleopration***********************************************
	
	@FXML
	public JoystickController teleJoystickController;
	
	@FXML
	public Button buttonRun;
	
	@FXML
	public TextArea textAreaTele;
	
	private IntegerProperty isRunpush;
	
	//************************************************************************************************

	
	GraphicsContext canvasGc;
	Image background;
	Image airplane; 
	Map<String,Plane> planes;
	

	
	

		
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	
		
		
		//paint();
	//************************************************fleet overview************************************************
	//map
		
		
		planes= new HashMap<>();
		this.canvasGc = mapCanvas.getGraphicsContext2D();
		airplane=null;
		background=null;
	
		try {
			airplane = new Image(new FileInputStream("./imgs/plane.png"));
			background = new Image(new FileInputStream("./imgs/map.jpeg"));
			this.drawAirplane(0, 0);

			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
		
		//pieChart
		 ObservableList<PieChart.Data> pieChartData= FXCollections.observableArrayList(
				new PieChart.Data("available", 39),
				new PieChart.Data("not available",61));	
		pieChart.setData(pieChartData);
		
		
		//barChart
		XYChart.Series setL=new XYChart.Series<>();
		setL.getData().add(new XYChart.Data<>("1",13));
		setL.getData().add(new XYChart.Data<>("2",43));
		setL.getData().add(new XYChart.Data<>("3",63));	
		barChart.getData().addAll(setL);

		
		//yearly barChart
		XYChart.Series setL1=new XYChart.Series<>();
		setL1.getData().add(new XYChart.Data<>("1",13));
		setL1.getData().add(new XYChart.Data<>("2",93));
		barChartYearly.getData().addAll(setL1);
		
		
		//Fleet line chart
		XYChart.Series setLineChart=new XYChart.Series<>();
		setLineChart.getData().add(new XYChart.Data<>("1",133));
		setLineChart.getData().add(new XYChart.Data<>("2",98));		
		fleetLineChart.getData().add(setLineChart);
		
		//*********************************************************************************************
	
	//****************************teleopration****************************************************
			
		//textfieldTele = new TextField();
		System.out.println("end");
		isRunpush=new SimpleIntegerProperty(0);
		
	
	}
	//*********************************************************************************************
	
	
	public void drawMap() {
		System.out.println("draw map");
		this.canvasGc.drawImage(this.background, 0, 0, mapCanvas.getHeight(), mapCanvas.getWidth());
	}
	
	public void drawAirplane(int x, int y) {
		System.out.println("draw air");
		this.drawMap();
		this.canvasGc.drawImage(this.airplane, x, y, 50, 50);
	}
	

	public void init1(ViewModel vm)
	{	
		System.out.println("init1");
		this.vm= vm;
		
		//***************** teleopration binding***********************************
	
		vm.throttle.bind(teleJoystickController.throttleSlider.valueProperty());
		vm.rudder.bind(teleJoystickController.rudderSlider.valueProperty());
		vm.aileron.bind(teleJoystickController.ailerons);
		vm.elevators.bind(teleJoystickController.elevators);
		vm.isRunPushed.bind(isRunpush);
	
		//*************************************************************************
	
		
	}
	
	public void runTeleCode()
	{
		 try {
			
			PrintWriter out=new PrintWriter(new File("teleoprationText.txt"));
			out.println(textAreaTele.getText().toString());
			out.flush();
			out.close();
			
			//we increase the value in order to change the binding value		
			isRunpush.set(isRunpush.getValue()+1);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	

	

	
	
///***************************************************************************************
	@Override
	public void update(Observable o, Object arg) {
//		System.err.println("Blabla");
//		int newX = Integer.parseInt(x.getText());
//		int newY = Integer.parseInt(y.getText());
//		drawAirplane(newX, newY);
	}





	public void setStage(Stage primaryStage) {
		this.stage=primaryStage;
		
	}

	
	
}
