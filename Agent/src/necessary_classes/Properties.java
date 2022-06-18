package necessary_classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Scanner;

public class Properties {
    public static HashMap<String,String> map;

    public Properties(String propertiesFileName) {
        try {
            map = new HashMap<>();
            Scanner sc = new Scanner(new FileReader("properties.txt"));
            while (sc.hasNext()) {
                String[] split = sc.next().split(",");
                map.put(split[0],split[1]);
            }
        } catch (FileNotFoundException e) { e.printStackTrace();}
    }
}
