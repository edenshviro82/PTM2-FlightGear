package FGServer;

import necessary_classes.FlightData;
import necessary_classes.Location;
import necessary_classes.Plane;
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
import java.sql.Date;
import java.time.Instant;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

//date collector class is responsible for collecting the data from flight gear simulator, store it
//and make analytics
public class DataCollector {
    //params index hash map will give us the ability to get a desired parameter from a stream by O(1)
    private final HashMap<String, Integer> paramsIndex;
    //symbol table, contains the values of desired flight parameters to be accessible by O(1), configurable from txt file
    private ConcurrentHashMap<String, String> symbolTable;
    //string with all the parameters the process collects from flight gear, give us the ability to know exactly
    //on which parameter we are looking (flight gear send a long string with all the parameters separated by ",")
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

    //update methods - the current methods is going to react to the given data stream from flight gear/////////////////

    //at the beginning of the flight, we want to init the flight data object with the start time of the flight
    //and with the source location of the flight
    public void initFlightData(String stream) {
        flightData.setStartTime(new Date(Calendar.getInstance().getTimeInMillis()));
        String[] params = stream.split(",");
        float longitude = Float.parseFloat(params[paramsIndex.get("longitude-deg")]);
        float latitude = Float.parseFloat(params[paramsIndex.get("latitude-deg")]);
        flightData.setFlyFrom(new Location(longitude, latitude));
        //after flight data initiated, we also need to update the first stream values
        this.updateFlightData(stream);
    }

    //this method is called every time a new stream is coming from flight gear and contains all the necessary update methods
    public void updateFlightData(String stream) {
        this.updateTimeSeries(stream);
        this.updateSymTable(stream);
        this.updateMaxValues(stream);
        this.updateMiles();
    }

    //at the end of the flight, we want to add the last details of our flight data (end time, dest location)
    //so he will be ready to sent to backend
    public void completeFlightData() {
        //get the parameters from the last time stamp of the flight
        flightData.setEndTime(new Date(Calendar.getInstance().getTimeInMillis()));
        String[] params = flightData.getTs().getDataStreams().get(flightData.getTs().getDataStreams().size() - 1).split(",");
        float longitude = Float.parseFloat(params[paramsIndex.get("longitude-deg")]);
        float latitude = Float.parseFloat(params[paramsIndex.get("latitude-deg")]);
        flightData.setFlyTo(new Location(longitude, latitude));
        flightData.setMiles(this.calcMiles());
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
        //the desired value index in that stream array stored in the pramsIndex map
        String[] params = stream.split(",");
        symbolTable.forEach((s1, s2) -> symbolTable.put(s1, params[paramsIndex.get(s1)]));
    }

    //add the stream to the time series
    private void updateTimeSeries(String stream) {
        flightData.getTs().getDataStreams().add(stream);
    }

    private void updateMiles() {
        flightData.setMiles(flightData.getMiles() + this.calcMiles());
    }

    //initialize methods///////////////////////////////////////////////////////

    /*this method will read the names of the parameters we collect from flight gear from the xml configuration
    file and concat the parameters names with "," using string builder. at the end our flightParams string
    is going to be updated with the names of all the parameters that we collect*/
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

    //open symbol table features file to get all the parameters we want to store in our symbol table and init the
    //map with the name as key and 0 as value
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

    /*this map will split flightParams string that we collected before, separate with "," to get value names array
    and store in the hashmap the value name and his index in the stream array, give us the ability to get desired
    value from live stream by O(1)*/
    public void initParamsIndex() {
        String[] params = flightParams.split(",");
        for (int i = 0; i < params.length; i++)
            paramsIndex.put(params[i], i);
    }

    //get methods//////////////////////////////////////////////////////////////
    public String getAileron() { return symbolTable.get("aileron"); }

    public String getRudder() {
        return symbolTable.get("rudder");
    }

    public String getThrottle() {
        return symbolTable.get("throttle");
    }

    public String getBrakes() {
        return symbolTable.get("speedbrake");
    }

    public String getElevators() {
        return symbolTable.get("elevator");
    }

    public String getAlt() {
        return symbolTable.get("encoder_indicated-altitude-ft");
    }

    public String getHeading() { return symbolTable.get("indicated-heading-deg");}

    public String getAirSpeed() {
        return symbolTable.get("airspeed-indicator_indicated-speed-kt");
    }

    public String getRoll() {
        return symbolTable.get("attitude-indicator_indicated-roll-deg");
    }

    public String getPitch() {
        return symbolTable.get("attitude-indicator_internal-pitch-deg");
    }

    public FlightData getFlight() {
        return this.flightData;
    }

    public String getStream() {
        //get the last stream from the time series
        return flightData.getTs().getDataStreams().get(flightData.getTs().getDataStreams().size() - 1);
    }

    public Location getLocation() {
        float longitude = Float.parseFloat(symbolTable.get("longitude-deg"));
        float latitude = Float.parseFloat(symbolTable.get("latitude-deg"));
        return new Location(longitude, latitude);
    }

    public Plane getPlane() {
        Plane plane = new Plane();
        plane.plainId = Properties.map.get("planeID");
        plane.flightID = Properties.map.get("flightID");
        plane.alt = Double.parseDouble(this.getAlt());
        plane.heading = Double.parseDouble(this.getHeading());
        plane.location = this.getLocation();
        plane.speed = Double.parseDouble(this.getAirSpeed());
        return plane;
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
                * Math.pow(Math.sin(dlon / 2), 2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in miles. Use 6371
        // for kilometers
        double r = 3956;

        // calculate the result
        return (c * r);
    }

    private double calcMiles() {
        int size = flightData.getTs().getSize();
        //we want to calculate the miles every rate specified seconds (configurable from properties txt file)
        //and the time series get updated 10 times in a second
        int rate = Integer.parseInt(Properties.map.get("calc_miles_rate(secs)"))*10;

        if (size%rate != 0)
            return 0;

        //get the stream from 10 seconds ago to get the previous location of the plane
        String lastStream = flightData.getTs().getDataStreams().get(size-100);
        String[] values = lastStream.split(",");
        float long1 = Float.parseFloat(values[paramsIndex.get("longitude-deg")]);
        float lat1 = Float.parseFloat(values[paramsIndex.get("latitude-deg")]);
        Location loc1 = new Location(long1,lat1);
        //we already have a method to get the current location
        Location loc2 = this.getLocation();
        return this.distance(loc1,loc2);
    }
}
