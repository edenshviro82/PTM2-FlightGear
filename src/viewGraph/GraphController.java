package viewGraph;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import model.Data;

public class GraphController implements Initializable {
	
	
	
    public @FXML LineChart leftGraph,rightGraph;
    public @FXML StackPane stackPane;
    public XYChart.Series leftSeries,rightSeries;
    
    public @FXML  ListView<String> listView;
    public Data data;
    
    
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
        leftSeries = new XYChart.Series();
        rightSeries = new XYChart.Series();
        leftGraph.setAnimated(false);
        rightGraph.setAnimated(false);
        leftGraph.setCreateSymbols(false);
        rightGraph.setCreateSymbols(false);
        data = new Data();
        listView.getItems().addAll(data.fps);
        
    }

//    public void display(ObservableList<Float> leftListItem,ObservableList<Float> rightListItem)
//    {
//       leftGraph.getData().clear();
//       leftSeries.getData().clear();
//       rightGraph.getData().clear();
//       rightSeries.getData().clear();
//
//        for(int i=0;i<leftListItem.size();i++)
//        {
//            leftSeries.getData().add(new XYChart.Data<>(getTimeByIndex(i),leftListItem.get(i)));
//            rightSeries.getData().add(new XYChart.Data<>(getTimeByIndex(i),rightListItem.get(i)));
//        }
//
//        leftGraph.getData().add(leftSeries);
//        rightGraph.getData().add(rightSeries);
//    }

//    private String getTimeByIndex(int index)
//    {
//        int timeInSeconds = index/10;
//        return Calculate.getTimeString(timeInSeconds);
//    }

//    public void updateDisplay(ObservableList<Float> leftListItem, ObservableList<Float> rightListItem, int ov)
//    {
//        if(list.getSelectionModel().getSelectedItem()!=null)
//        {
//            int j = ov;
//            for(int i=0;i<leftListItem.size();i++,j++)
//            {
//                leftSeries.getData().add(new XYChart.Data<>(getTimeByIndex(j),leftListItem.get(i)));
//                rightSeries.getData().add(new XYChart.Data<>(getTimeByIndex(j),rightListItem.get(i)));
//            }
//        }
//    }

}
