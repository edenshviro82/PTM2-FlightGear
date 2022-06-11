package model;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Data {

	  public final HashMap<String, Integer> paramsIndex;
	  public String flightParams;
	  public String fps[];

	 
	 
	 public Data() {
		this.paramsIndex = new HashMap<>();
		this.initFlightParams("playback_small.xml");
		this.initFpsArry();
	 }
	
	 
	 //get xml file and put all the params in string -> flightParams
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
	            this.flightParams = sb.toString();
	        } catch (ParserConfigurationException | SAXException | IOException e) {
	            e.printStackTrace();
	        }
	    }
	   
	   //get over the params string and init the fps array and HashMap<String, Integer> paramsIndex.
	   private void initFpsArry()
	   {
		   	fps= flightParams.split(",");
		   	for (int i=0; i<fps.length; i++) 
		   	    paramsIndex.put(fps[i], i);
		   	
	   }
	
	
}
