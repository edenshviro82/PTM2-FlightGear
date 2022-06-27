package viewPlay;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import eu.hansolo.medusa.tools.Data;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import necessary_classes.TimeSeries;
import view.Main;
import viewGraph.GraphController;
import model.*;

public class PlayController implements Initializable{

    @FXML public TextField playSpeed;
    @FXML public Label flightTime;
    @FXML public Slider slider;
    @FXML public Button play,stop,pause,fastForward, slowForward,openButton;
    public StringProperty timeSeriesPath;
    //public Runnable onPlay,onStop,onPause,onFastForward, onSlowForward,onToStart,onToEnd;
   // public BooleanProperty isPlayed;
    Stage stage;
    private GraphController gc;
    private ExecutorService es;
    public LongProperty rate;
    public Scanner sc;
    public PrintWriter out;
    public static volatile boolean isplayed;
    public DoubleProperty tcAileron;
    public DoubleProperty tcElevator;
    public DoubleProperty tcRudder;
    public DoubleProperty tcThrottle;
    public String str;
    public FloatProperty altitude,headDeg,rollDeg,longitude,verticalSpeed,airspeed;
    public static HashMap<String, Integer> paramsIndex=new HashMap<>();
    public PlayController()
    {
    	longitude=new SimpleFloatProperty(0);
		rollDeg=new SimpleFloatProperty(0);
		headDeg=new SimpleFloatProperty(0);
		altitude=new SimpleFloatProperty(0);
		verticalSpeed=new SimpleFloatProperty(0);
		airspeed=new SimpleFloatProperty(0);
    	rate= new SimpleLongProperty(100);
    	es=Executors.newFixedThreadPool(2);
    	stage = Main.getGuiStage();
        timeSeriesPath = new SimpleStringProperty();
        gc=new GraphController();
        isplayed=false;
        tcAileron = new SimpleDoubleProperty(0);
        tcElevator = new SimpleDoubleProperty(0);
        tcRudder = new SimpleDoubleProperty(0);
        tcThrottle = new SimpleDoubleProperty(0);

        
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		slider.setDisable(true);
        play.setDisable(true);
        stop.setDisable(true);
        pause.setDisable(true);
        fastForward.setDisable(true);
        slowForward.setDisable(true);
      
        
	}

    @FXML
    public void openCSV()
    {
    	
    	slider.setDisable(false);
        play.setDisable(false);
        stop.setDisable(false);
        pause.setDisable(false);
        fastForward.setDisable(false);
        slowForward.setDisable(false);
    	FileChooser fc = new FileChooser();
        fc.setTitle("Load Flight CSV File");
        fc.setInitialDirectory(new File("./sources"));
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(
                "CSV Files (*.csv)", "*.csv");
        fc.getExtensionFilters().add(extensionFilter);
        File chosenFile = fc.showOpenDialog(stage);
        
        

        if(chosenFile!=null){
            timeSeriesPath.setValue(chosenFile.getAbsolutePath());
            gc.path=chosenFile.getAbsolutePath();
            gc.ts2=new TimeSeries(gc.path);
            gc.sad.detect(gc.ts2);
            System.out.println("done detect");            
            
            
            
            es.execute(()->{
            	 Socket s;
				try {
//				s = new Socket("localhost",5400);
//                 System.out.println("connected to flightgear");
//                  out = new PrintWriter(s.getOutputStream(),true);
                 sc = new Scanner(new FileReader(gc.path));
                 str= sc.next();
                 String sp[]=str.split(",");
                 for(int i=0;i<sp.length;i++) 
                	 paramsIndex.put(sp[i], i);
                
                 

                 
//				} catch (UnknownHostException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
            });
            
            
        }
        
        
        
        
        
        
    	
    }

    @FXML
    public void play()
    {	System.out.println("play");
    	es.execute(()->{
         try {
    		isplayed=true;
    		while(sc.hasNext()&& isplayed) {
    			str=sc.next();
    			joyAndClocks();
    			//out.println(str);
				Thread.sleep(rate.longValue());	
    		}
           } catch (InterruptedException e) {e.printStackTrace();}
    	});
    }
    
    private void joyAndClocks() {
		String sp[]=str.split(",");
		
		Platform.runLater(()->{
		tcThrottle.set(Double.parseDouble(sp[6]));
		tcAileron.set(Double.parseDouble(sp[0]));
		tcElevator.set(Double.parseDouble(sp[1]));
		tcRudder.set(Double.parseDouble(sp[2]));
		   altitude.set(Float.parseFloat(sp[paramsIndex.get("altitude-ft")]));
           airspeed.set(Float.parseFloat(sp[paramsIndex.get("airspeed-kt")]));
           headDeg.set(Float.parseFloat(sp[paramsIndex.get("heading-deg")]));
           longitude.set(Float.parseFloat(sp[paramsIndex.get("longitude-deg")]));
           rollDeg.set(Float.parseFloat(sp[paramsIndex.get("roll-deg")]));
           verticalSpeed.set(Float.parseFloat(sp[paramsIndex.get("vertical-speed-fps")]));
		
		
		
		System.out.println(tcThrottle.doubleValue());
		});
		}

	@FXML
    public void stop()
    {
        isplayed=false;
        try {
			sc = new Scanner(new FileReader(gc.path));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    @FXML
    public void pause()
    {
        isplayed=false;

    }
    @FXML
    public void fastForward()
    {
        isplayed=false;
       	int i=0;
        if(sc.hasNext()&&i<300) {
        	//out.println(sc.next());
        	i++;
        }
        play();
        
    }
    @FXML
    public void slowForward()
    {

        isplayed=false;
       	int i=0;
        if(sc.hasNext()&&i<150) {
        	//out.println(sc.next());
        	i++;
        }
        play();
    }


    
    @FXML
    public void speed1()
    {
    	rate.set(100);
    }
    
    @FXML
    public void speed15()
    {
    	rate.set(67);

    }
    
    @FXML
    public void speed2()
    {
    	rate.set(33);

    }
    

}
