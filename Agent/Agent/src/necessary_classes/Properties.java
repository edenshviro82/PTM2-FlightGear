package necessary_classes;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class Properties {
    public static HashMap<String,String> map;

    public Properties(String propertiesFileName) {
        try {
            map = new HashMap<>();
            LinkedList<String> output = new LinkedList<>();
            //read the properties text file and store in data structure with updated flightId
            Scanner sc = new Scanner(new FileReader("properties.txt"));
            while (sc.hasNext()) {
                String line = sc.next();
                output.add(line);
                String[] split = line.split(",");
                map.put(split[0],split[1]);
                //incrementing flight id parameter
                if (split[0].equals("flightId")) {
                    int inc = Integer.parseInt(split[1]) + 1;
                    String newLine = "flightId," + inc;
                    output.removeLast();
                    output.add(newLine);
                }
            }
            sc.close();

            //write the txt file back with a new incremented flightId
            PrintWriter printer = new PrintWriter(new FileOutputStream(propertiesFileName),true);
            output.forEach(s -> printer.println(s));
            printer.close();
        } catch (IOException e) { e.printStackTrace();}
    }
}
