package viewPlay;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class PlayController {

    @FXML public TextField playSpeed;
    @FXML public Label flightTime;
    @FXML public Slider slider;
    @FXML public Button play,stop,pause,fastForward, slowForward,toEnd,toStart,openButton;
    public StringProperty timeSeriesPath;
    public Runnable onPlay,onStop,onPause,onFastForward, onSlowForward,onToStart,onToEnd;
    public BooleanProperty isPlayed;

    public PlayController()
    {
        
        timeSeriesPath = new SimpleStringProperty();
        isPlayed = new SimpleBooleanProperty(false);
    }



    public void openCSV()
    {
        FileChooser fc = new FileChooser();
        fc.setTitle("Load Flight CSV File");
        fc.setInitialDirectory(new File("./Sources"));
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(
                "CSV Files (*.csv)", "*.csv");
        fc.getExtensionFilters().add(extensionFilter);
       // File chosenFile = fc.showOpenDialog(stage);
//
//        if(chosenFile!=null){
//            timeSeriesPath.setValue(chosenFile.getAbsolutePath());
//        }
    }


    public void play()
    {
        if(onPlay!=null)
        {
            onPlay.run();
            isPlayed.setValue(true);
            openButton.setDisable(true);
        }
    }

    public void stop()
    {
        if(onStop!=null)
        {
            onStop.run();
            isPlayed.setValue(false);
            openButton.setDisable(false);
        }
    }

    public void pause()
    {
        if(onPause!=null)
        {
            onPause.run();
            isPlayed.setValue(false);
            openButton.setDisable(false);
        }
    }

    public void fastForward()
    {
        if(onFastForward!=null)
            onFastForward.run();
    }

    public void slowForward()
    {

        if(onSlowForward !=null)
            onSlowForward.run();
    }

    public void toEnd(){
        if(onToEnd!=null) {
            onToEnd.run();
            isPlayed.setValue(false);
            openButton.setDisable(false);
        }
    }

    public void toStart()
    {
        if(onToStart!=null)
        {
            onToStart.run();
            isPlayed.setValue(true);
            openButton.setDisable(true);
        }
    }
}
