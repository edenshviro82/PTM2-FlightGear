package necessary_classes;

import java.io.Serializable;
import java.util.ArrayList;

//time series object will store all the streams we get from the simulator
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

    public int getSize() { return dataStreams.size(); }

    public String getStream(int index) {
        if (index > 0 && index < dataStreams.size())
            return dataStreams.get(index);
        return null;
    }
}