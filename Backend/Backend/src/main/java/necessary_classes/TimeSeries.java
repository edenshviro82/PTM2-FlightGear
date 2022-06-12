package necessary_classes;

import java.io.*;
import java.util.ArrayList;

public class TimeSeries implements Serializable {
    ArrayList<String> dataStreams;

    public TimeSeries() {this.dataStreams = new ArrayList<>();}

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