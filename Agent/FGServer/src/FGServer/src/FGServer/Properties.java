package FGServer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Scanner;

public class Properties {
    public static HashMap<String,String> map;

    public Properties(String propertiesFileName) {
        map = new HashMap<>();
        try {
            Scanner sc = new Scanner(new FileReader(propertiesFileName));
            while (sc.hasNext()) {
                String[] split = sc.next().split(",");
                map.put(split[0],split[1]);
            }
        } catch (FileNotFoundException e) { e.printStackTrace(); }
    }
}
