package test;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Test test = new Test();
        String line;
        Scanner sc = new Scanner(System.in);
        line = sc.nextLine();
        System.out.println(line);
        while (!(line.equals("bye"))) {
            line = sc.nextLine();
            String[] split = line.split(" ");
            String command = split[0] + " " + split[1];
            if (test.getMap.containsKey(command))
                test.getMap.get(command).run();
            if (test.setMap.containsKey(command))
                test.setMap.get(command).accept(Float.parseFloat(split[2]));
        }
    }
}