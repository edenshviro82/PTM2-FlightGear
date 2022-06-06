package FGServer;

import necessary_classes.FlightData;
import necessary_classes.Location;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class DataCollector {
    //params index hash map will give us the ability to get a desired parameter from a stream by O(1)
    private final HashMap<String, Integer> paramsIndex;
    private ConcurrentHashMap<String, String> symbolTable;
    //string with all the parameters the process collects from flight gear
    private String flightParams;
    private FlightData flightData;

    public DataCollector() {
        symbolTable = new ConcurrentHashMap<>();
        paramsIndex = new HashMap<>();
        flightData = new FlightData();
        this.initSymTable("symbol_table_features.txt");
        this.initFlightParams("playback_small.xml");
        this.initParamsIndex();
    }

    //get methods//////////////////////////////////////////////////////////////
    public String getAileron() {
        return symbolTable.get("aileron");
    }
    public String getRudder() {
        return symbolTable.get("rudder");
    }
    public String getThrottle() { return symbolTable.get("throttle"); }
    public String getBreaks() { return symbolTable.get("speedbrake"); }
    public String getElevators() { return symbolTable.get("elevator"); }
    public String getAlt() {return symbolTable.get("altitude-ft");}
    public String getHeading() { return symbolTable.get("heading-deg"); }
    public String getAirSpeed() { return symbolTable.get("airspeed-kt"); }
    public String getRoll() { return symbolTable.get("roll-deg"); }
    public String getPitch() {
        return symbolTable.get("pitch-deg");
    }
    public FlightData getFlight() { return this.flightData; }
    public String getStream() { return flightData.getTs().getDataStreams().get(flightData.getTs().getDataStreams().size()-1); }

    public Location getLocation() {
        float longitude = Float.parseFloat(symbolTable.get("longitude-deg"));
        float latitude = Float.parseFloat(symbolTable.get("latitude-deg"));
        return new Location(longitude, latitude);
    }


    //update methods////////////////////////////////////////////////////////////////////////
    public void initFlightData(String stream) {
        flightData.setStartTime(Instant.now().toString());
        String[] params = stream.split(",");
        float longitude = Float.parseFloat(params[paramsIndex.get("longitude-deg")]);
        float latitude = Float.parseFloat(params[paramsIndex.get("latitude-deg")]);
        flightData.setFlyFrom(new Location(longitude, latitude));
        //init the first line with of the time series with the flight params to get headlines
        flightData.getTs().getDataStreams().add(stream);
        //after flight data initiated, we also need to update the first stream values
        this.updateFlightData(stream);
    }

    public void updateFlightData(String stream) {
        this.updateTimeSeries(stream);
        this.updateSymTable(stream);
        this.updateMaxValues(stream);
        this.updateMiles();
    }

    public void completeFlightData() {
        //get the parameters from the last time stamp of the flight
        String[] params = flightData.getTs().getDataStreams().get(flightData.getTs().getDataStreams().size() - 1).split(",");
        flightData.setEndTime(Instant.now().toString());
        float longitude = Float.parseFloat(params[paramsIndex.get("longitude-deg")]);
        float latitude = Float.parseFloat(params[paramsIndex.get("latitude-deg")]);
        float altitude = Float.parseFloat(params[paramsIndex.get("altitude-ft")]);
        flightData.setFlyTo(new Location(longitude, latitude));
    }

    private void updateMaxValues(String stream) {
        String[] params = stream.split(",");

        float currSpeed = Float.parseFloat(params[paramsIndex.get("airspeed-kt")]);
        if (currSpeed > flightData.getMaxSpeed())
            flightData.setMaxSpeed(currSpeed);

        float currAltitude = Float.parseFloat(params[paramsIndex.get("altitude-ft")]);
        if (currAltitude > flightData.getMaxAltitude())
            flightData.setMaxAltitude(currAltitude);
    }

    private void updateSymTable(String stream) {
        //given a new stream of data, split the data by , to get array of values and update the symbol table
        //the desired value index stored in the pramsIndex map
        String[] params = stream.split(",");
        symbolTable.forEach((s1,s2) -> symbolTable.put(s1, params[paramsIndex.get(s1)]));
    }

    private void updateTimeSeries(String stream) {
        flightData.getTs().getDataStreams().add(stream);
    }

    private void updateMiles() { flightData.setMiles(flightData.getMiles() + this.calcMiles());}

    //initialize methods///////////////////////////////////////////////////////
    private void initFlightParams(String xmlFileName) {
        try {
            File fXmlFile = new File(xmlFileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("chunk");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < nList.getLength() / 2; i++) {
                Node nNode = nList.item(i);
                Element element = (Element) nNode;
                sb.append(element.getElementsByTagName("name").item(0).getTextContent()).append(",");
            }
            flightParams = sb.toString();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    private void initSymTable(String SymTblFileName) {
        try {
            Scanner sc = new Scanner(new FileReader(SymTblFileName));
            while (sc.hasNext()) {
                symbolTable.put(sc.next(), "0");
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void initParamsIndex() {
        String[] params = flightParams.split(",");
        for (int i = 0; i < params.length; i++)
            paramsIndex.put(params[i], i);
    }

    //calc distance method///////////////////////////////////////////////////////
    private double distance(Location loc1, Location loc2) {
        // The math module contains a function
        // named toRadians which converts from
        // degrees to radians.
        double long1 = Math.toRadians(loc1.getLongitude());
        double long2 = Math.toRadians(loc2.getLongitude());
        double lat1 = Math.toRadians(loc1.getLatitude());
        double lat2 = Math.toRadians(loc2.getLatitude());

        // Haversine formula
        double dlon = long2 - long1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in miles. Use 6371
        // for kilometers
        double r = 3956;

        // calculate the result
        return(c * r);
    }

    private double calcMiles() {
        int size = flightData.getTs().getSize();
        //we want to calculate the miles every 10 seconds and the time series get updated 10 times in a second
        if (size%100 != 0)
            return 0;

        //get the stream from 10 seconds ago to get the location of the plane
        String lastStream = flightData.getTs().getDataStreams().get(size-101);
        String[] values = lastStream.split(", ");
        Float long1 = Float.parseFloat(values[paramsIndex.get("longitude-deg")]);
        Float lat1 = Float.parseFloat(values[paramsIndex.get("latitude-deg")]);
        Location loc1 = new Location(long1,lat1);
        //we already have a method to get the current location
        Location loc2 = this.getLocation();
        return this.distance(loc1,loc2);
    }
}
