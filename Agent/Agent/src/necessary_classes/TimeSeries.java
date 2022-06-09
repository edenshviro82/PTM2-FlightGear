package necessary_classes;

import java.io.Serializable;
import java.util.ArrayList;

public class TimeSeries implements Serializable {
    ArrayList<String> dataStreams;

    public TimeSeries() {
        this.dataStreams = new ArrayList<>();
    }

    public ArrayList<String> getDataStreams() {
        return dataStreams;
    }

    public void setDataStreams(ArrayList<String> dataStreams) {
        this.dataStreams = dataStreams;
    }
}