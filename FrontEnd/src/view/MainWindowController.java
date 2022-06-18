package view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale.Category;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.TickLabelOrientation;
import eu.hansolo.medusa.skins.SpaceXSkin;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import viewJoystick.*;
import viewModel.ViewModel;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.Window;
import necessary_classes.*;
import viewClocks.ClockController;
//import viewClocks.ClockController;


public class MainWindowController implements Initializable,Observer{

	ExecutorService es;
	
	@FXML
	private TabPane tabPane;
	
	String currentId;
	
	public IntegerProperty isFleetPushed;
	public IntegerProperty isMoniPushed;
	public IntegerProperty isTelePushed;
	public IntegerProperty isTimeCapsulePushed;
	
//********************************************fleet Overview***************************************	

	
	static int i=0;
	public ViewModel vm;
	
	private Stage stage;
	public  HashMap<Integer,Number> fleetSize;
	public HashMap<Integer,Number> milesYear;
	public HashMap<String,Number> miles;
	public ArrayList<Plane> planesArray;

	
	@FXML
	private PieChart pieChart;

	@FXML
	private BarChart<String,Number> barChart;
	@FXML
	private CategoryAxis x;
	@FXML
	private NumberAxis y;
	
	@FXML
	public Button refreshButton;

	@FXML
	private BarChart<String,Number> barChartYearly;
	@FXML
	private CategoryAxis x1;
	@FXML
	private NumberAxis y1;
	
	@FXML
	private LineChart<String,Number> fleetLineChart;
	@FXML
	private CategoryAxis xl;
	@FXML
	private NumberAxis yl;
	
	
	@FXML
	Canvas mapCanvas;	
	
	@FXML
	Button airplane1Button;
	@FXML
	Button airplane2Button;

	
	@FXML
	public TextArea textAreaPlane;
	
	@FXML
	public TextArea textAreaPlane2;
	
	float posX,posY;
	
	private IntegerProperty isPlanepushOnce;
	private IntegerProperty isPlanepushTwice;
	
	



	

	//************************************************************************************************
	
	
	//***************************************monitoring***********************************************

	@FXML
	public JoystickController moniJoystickController;
	
	@FXML
	public ClockController moniClockController;
	
	
	//************************************************************************************************
	
	//***************************************teleopration***********************************************
	
	@FXML
	public JoystickController teleJoystickController;
	
	@FXML
	public ClockController teleClockController;
	
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
		
		es= Executors.newFixedThreadPool(4);
		
		isFleetPushed = new SimpleIntegerProperty(0);
		isMoniPushed = new SimpleIntegerProperty(0);
		isTelePushed = new SimpleIntegerProperty(0);
		isTimeCapsulePushed = new SimpleIntegerProperty(0);
		
		
		
		
		
		
		//paint();
	//************************************************fleet overview************************************************
	//map
		planes= new HashMap<>();
		planesArray=new ArrayList<>();
		this.canvasGc = mapCanvas.getGraphicsContext2D();
		airplane=null;
		background=null;
		isPlanepushOnce=new SimpleIntegerProperty();
		isPlanepushTwice= new SimpleIntegerProperty();
		
		fleetSize=new HashMap<>();
		milesYear=new HashMap<>();
		miles=new HashMap<>();

		//refresh();
	
		try {
			airplane = new Image(new FileInputStream(("./imgs/plane.png")));
			background = new Image(new FileInputStream("./imgs/map.jpeg"));
			this.textAreaPlane.setLayoutX(2000);
			this.textAreaPlane.setLayoutY(2000);
			this.textAreaPlane.setBackground(null);
			this.textAreaPlane2.setLayoutX(2000);
			this.textAreaPlane2.setLayoutY(2000);
			this.textAreaPlane2.setBackground(null);
//			this.textAreaPlane.setStyle("-fx-background-color: blue");
//			this.textAreaPlane2.setStyle("-fx-background-color: blue");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	
		
//		vm.isPlanepushOnce.bind(isPlanepushOnce);
//		vm.isPlanepushTwice.bind(isPlanepushTwice);
		
		
		//pieChart
		 ObservableList<PieChart.Data> pieChartData= FXCollections.observableArrayList(
				new PieChart.Data("available", 0),
				new PieChart.Data("not available",0));	
		pieChart.setData(pieChartData);
		
		
		//barChart
		XYChart.Series setL=new XYChart.Series<>();
		barChart.getData().addAll(setL);

		
		//yearly barChart
		XYChart.Series setL1=new XYChart.Series<>();
		barChartYearly.getData().addAll(setL1);
		
		
		//Fleet line chart
		XYChart.Series setLineChart=new XYChart.Series<>();	
		fleetLineChart.getData().add(setLineChart);
		
		//*********************************************************************************************
	
	//****************************teleopration****************************************************
			
		//textfieldTele = new TextField();
		System.out.println("end");
		isRunpush=new SimpleIntegerProperty(0);
		
		
		
	
	}
	//*********************************************************************************************
	
	
	
	public void refresh()
	{
//		miles = vm.m.miles;
//		fleetSize = vm.m.fleetSize;
//		milesYear = vm.m.milesYear;
		
		miles.put("e", 6.2);
		miles.put("dfjc", 2.3);

		
		fleetSize.put(2, 29);	
		fleetSize.put(3, 4);
		fleetSize.put(4, 2);
		fleetSize.put(6, 28);

		
		milesYear.put(3, 44.4);
		milesYear.put(4, 71.4);
		milesYear.put(5, 71.4);
		milesYear.put(6, 71.4);
		milesYear.put(8, 71.4);
		milesYear.put(7, 71.4);
		
		
		
		//int availablePlanes = vm.m.planes.size();
		int month = Integer.parseInt(Instant.now().toString().split("-")[1]);
		int availablePlanes=2;
		int thisM=fleetSize.get(month).intValue();
		int notAvailablePlanes = fleetSize.get(month).intValue() -availablePlanes;
	
		
	es.execute(()->{
		
		try {
			ObservableList<PieChart.Data> pieChartData= FXCollections.observableArrayList(
			new PieChart.Data("available", availablePlanes),
			new PieChart.Data("not available",notAvailablePlanes));		
		Thread.sleep(250);
		Platform.runLater(()->
		{
			pieChart.setData(pieChartData);
		 });	 
		

		Platform.runLater(()->
		{
			  XYChart.Series<String, Number> setL2 = new XYChart.Series<>();
			  XYChart.Data<String, Number> d1 = new XYChart.Data<>(" ",0.0001);
			  setL2.getData().add(d1);
			  barChart.setData(FXCollections.observableArrayList(setL2));
			  
	    XYChart.Series<String, Number> setL = new XYChart.Series<>();
	    for (Map.Entry<String, Number> entry : miles.entrySet()) {
	        String tmpString = entry.getKey();
	        Number tmpValue = entry.getValue();
	        XYChart.Data<String, Number> d = new XYChart.Data<>(tmpString, tmpValue);
	        setL.getData().add(d);
	    }
	  
	    barChart.setData(FXCollections.observableArrayList(setL));
		 });

		Platform.runLater(()->
		{
	    XYChart.Series<String, Number> setL1 = new XYChart.Series<>();
	    for (Map.Entry<Integer, Number> entry : milesYear.entrySet()) {
	        String tmpInt = entry.getKey().toString();
	        Number tmpValue = entry.getValue();
	        XYChart.Data<String, Number> d = new XYChart.Data<>(tmpInt, tmpValue);
	        setL1.getData().add(d);
	    }
	    
	    barChartYearly.setData(FXCollections.observableArrayList(setL1));
		 });

		
		Platform.runLater(()->
		{
	    XYChart.Series<String, Number> setL3 = new XYChart.Series<>();
	    for (Map.Entry<Integer, Number> entry : fleetSize.entrySet()) {
	        String tmpInt = entry.getKey().toString();
	        Number tmpValue = entry.getValue();
	        XYChart.Data<String, Number> d = new XYChart.Data<>(tmpInt, tmpValue);
	        setL3.getData().add(d);
	    }
	    
	    fleetLineChart.setData(FXCollections.observableArrayList(setL3));
		 });
		
		System.out.println("Dddddddd");
//		
//		Thread.sleep(2000);	
//		XYChart.Series setLineChart=new XYChart.Series<Integer,Integer>();
//		fleetSize.forEach((inti,inti1)-> setLineChart.getData().add(new XYChart.Data<>(inti,inti1)));
//		fleetLineChart.getData().clear();
//		Platform.runLater(()->
//		{		
//			fleetLineChart.getData().addAll(setLineChart);	
//		});	 
//
	
		
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	
	});
	
	
	
		
		
	}
	
	
	
	public void drawMap() {
		//System.out.println("draw map");
		this.canvasGc.drawImage(this.background, 0, 0, mapCanvas.getHeight(), mapCanvas.getWidth());
	}
	@FXML
	public void drawAirplane() {
		//System.out.println("draw air");

		this.drawMap();
		try {
		
		for(int i=0; i<planesArray.size(); i++)
		{
			
			this.posX= planesArray.get(i).getLocation().getLongitude()%366;
			this.posY=  planesArray.get(i).getLocation().getLatitude()%366;
			changeAirplaneButton(i,posX,posY);
			
			
				if(planesArray.get(i).getHeading()>= 0 && planesArray.get(i).getHeading() <= 90)
					airplane = new Image(new FileInputStream(("./imgs/plane.png")));
				if(planesArray.get(i).getHeading()>= 91  && planesArray.get(i).getHeading() <= 180)
					airplane = new Image(new FileInputStream(("./imgs/plane2.png")));
				if(planesArray.get(i).getHeading()>= 181 && planesArray.get(i).getHeading() <= 270)
					airplane = new Image(new FileInputStream(("./imgs/plane3.png")));
				if(planesArray.get(i).getHeading()>= 271 && planesArray.get(i).getHeading() < 360)
					airplane = new Image(new FileInputStream(("./imgs/plane4.png")));
					
				
				
				this.canvasGc.drawImage(airplane,posX,posY,50,50);
			}
				
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//this.canvasGc.drawImage(airplane, f, g,50,50);
	}
	

	public void changeAirplaneButton(int index,float posX,float posY)
	{
		if (index==0)
		{
			airplane1Button.setBackground(null);
			airplane1Button.setLayoutX(posX);
			airplane1Button.setLayoutY(posY);
			
		
		}
		if (index==1)
		{
			airplane2Button.setBackground(null);
			airplane2Button.setLayoutX(posX);
			airplane2Button.setLayoutY(posY);
		
		}	

			
	}
	public void moveButton() {
		
		isPlanepushOnce.set(isPlanepushOnce.getValue()+1);
		//System.out.println("clicked!!");
		this.textAreaPlane.setLayoutX(airplane1Button.getLayoutX()+50);
		this.textAreaPlane.setLayoutY(airplane1Button.getLayoutY()+50);
		this.textAreaPlane.setText("id: "+ planesArray.get(0).getPlainId()+ "\n"+ "Heading: " + planesArray.get(0).getHeading() + "\n" 
				+ "High: "+ planesArray.get(0).getAlt()+ "\n"+ "Speed: " + planesArray.get(0).getSpeed());
		
		
		this.textAreaPlane2.setLayoutX(2000);
		this.textAreaPlane2.setLayoutY(2000);
		this.textAreaPlane2.setBackground(null);
	


	}
	
	public void moveButton2() {
		
		isPlanepushOnce.set(isPlanepushOnce.getValue()+1);
		//System.out.println("clicked!!");
		this.textAreaPlane2.setLayoutX(airplane2Button.getLayoutX()+50);
		this.textAreaPlane2.setLayoutY(airplane2Button.getLayoutY()+50);
		this.textAreaPlane2.setText("id: "+ planesArray.get(1).getPlainId()+ "\n"+ "Heading: " + planesArray.get(1).getHeading() + "\n" 
				+ "High: "+ planesArray.get(1).getAlt()+ "\n"+ "Speed: " + planesArray.get(1).getSpeed());
		
		this.textAreaPlane.setLayoutX(2000);
		this.textAreaPlane.setLayoutY(2000);
		this.textAreaPlane.setBackground(null);


	}
	
	public void isMouseDoubleClicked(MouseEvent me) {
		
		if(me.getClickCount() == 2){
			isPlanepushTwice.set(isPlanepushTwice.get()+1);
           // System.out.println("Double clicked");
            tabPane.getSelectionModel().selectNext();;
				this.textAreaPlane.setLayoutX(2000);
				this.textAreaPlane.setLayoutY(2000);
				this.textAreaPlane2.setLayoutX(2000);
				this.textAreaPlane2.setLayoutY(2000);
				

		}
			vm.m.currentId=planesArray.get(0).plainId;
			System.out.println(vm.m.currentId);
	}
		public void isMouseDoubleClicked2(MouseEvent me) {
			
			if(me.getClickCount() == 2){
				isPlanepushTwice.set(isPlanepushTwice.get()+1);
	           // System.out.println("Double clicked");
	            tabPane.getSelectionModel().selectNext();;
	    		this.textAreaPlane.setLayoutX(2000);
				this.textAreaPlane.setLayoutY(2000);
				this.textAreaPlane2.setLayoutX(2000);
				this.textAreaPlane2.setLayoutY(2000);
				
			}
			vm.m.currentId=planesArray.get(1).plainId;
			System.out.println(vm.m.currentId);
	}
	
	
	public void fleetTabSelection() {
		if (isFleetPushed != null) {
			isFleetPushed.set(isFleetPushed.getValue()+1);
			es.execute(()->planesLocation());
		}
	}
	
	public void planesLocation() {
		
			while(tabPane.getSelectionModel().getSelectedIndex()==0) {
		
			try {
			System.out.println("Ssssss");
			Thread.sleep(1000);
			//planesArray=vm.m.planes;
			Plane p = new Plane();
			p.alt = 500;
			p.heading = 20;
			p.plainId = "12";
			p.location = new Location();
			p.location.setLatitude((float) 50.0000);
			System.out.println("felisdjeljsfdisfe");
			System.out.println(p.location.getLatitude());
			p.location.setLongitude((float) 30.0000);
			p.speed = 2000;
			
			Plane p1 = new Plane();
			p1.alt = 23500;
			p1.heading = 278;
			p1.plainId= "13";
			p1.location = new Location();
			p1.location.setLatitude((float) 570.0000);
			p1.location.setLongitude((float) 2340.0000);
			p1.speed = 2000;
		
			planesArray.add(p);
			planesArray.add(p1);

			Thread.sleep(1000);
			drawAirplane();

			} 
			
			catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		}
		
		
	}
	
	
	public void teleTabSelection() {
		isTelePushed.set(isTelePushed.getValue()+1);

	}
	
	public void moniTabSelection() {
		isMoniPushed.set(isMoniPushed.getValue()+1);
	}
	
	public void timeCapsuleTabSelection() {
		isTimeCapsulePushed.set(isTimeCapsulePushed.getValue()+1);
	}	
	
	
	public void init1(ViewModel vm)
	{	
		System.out.println("init1");
		this.vm= vm;
		
		
		//*******************************monitoring binding******************
		
		moniClockController.clock1.valueProperty().bind(vm.altitude);
		moniClockController.clock2.valueProperty().bind(vm.headDeg);
		moniClockController.clock3.valueProperty().bind(vm.rollDeg);
		moniClockController.clock4.valueProperty().bind(vm.longitude);
		moniClockController.clock5.valueProperty().bind(vm.verticalSpeed);
		moniClockController.clock6.valueProperty().bind(vm.airspeed);
		
		moniJoystickController.ailerons.bind(vm.moniAileron);
		moniJoystickController.elevators.bind(vm.moniElevators);
		moniJoystickController.rudderSlider.valueProperty().bind(vm.moniRudder);
		moniJoystickController.throttleSlider.valueProperty().bind(vm.moniThrottle);
		
		vm.moniThrottle.addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
	
				double d = moniJoystickController.throttleSlider.getValue();
				moniJoystickController.throttleLabel.setText(""+d);
				System.out.println(moniJoystickController.throttleLabel.getText());
			}
		});
		
		
		moniJoystickController.rudderSlider.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				
				double d = moniJoystickController.rudderSlider.getValue();
				moniJoystickController.rudderLabel.setText(""+d);
				System.out.println(moniJoystickController.rudderLabel.toString());

			}
		});
		
		moniJoystickController.ailerons.addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
			
				double d = moniJoystickController.ailerons.getValue();
				moniJoystickController.aileronLabel.setText(""+d);
				System.out.println(moniJoystickController.aileronLabel.toString());

			}
		});
		
		moniJoystickController.elevators.addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				
				double d = moniJoystickController.elevators.getValue();
				moniJoystickController.elevatorLabel.setText(""+d);
				System.out.println(moniJoystickController.elevatorLabel.toString());

			}
		});
		
		//***************** teleopration binding***********************************
	
		vm.throttle.bind(teleJoystickController.throttleSlider.valueProperty());
		vm.rudder.bind(teleJoystickController.rudderSlider.valueProperty());
		vm.aileron.bind(teleJoystickController.ailerons);
		vm.elevators.bind(teleJoystickController.elevators);
		vm.isRunPushed.bind(isRunpush);
		vm.isFleetPushed.bind(isFleetPushed);
		vm.isMoniPushed.bind(isMoniPushed);
		vm.isTelePushed.bind(isTelePushed);
		vm.isTimeCapsulePushed.bind(isTimeCapsulePushed);
	
		//
		fleetTabSelection();
		

		teleClockController.clock1.valueProperty().bind(vm.altitude);
		teleClockController.clock2.valueProperty().bind(vm.headDeg);
		teleClockController.clock3.valueProperty().bind(vm.rollDeg);
		teleClockController.clock4.valueProperty().bind(vm.longitude);
		teleClockController.clock5.valueProperty().bind(vm.verticalSpeed);
		teleClockController.clock6.valueProperty().bind(vm.airspeed);
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
