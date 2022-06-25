package test;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Test test = new Test();
        String line;
        Scanner in = new Scanner(System.in);
        line = in.nextLine();
        String[] split = line.split(" ");
        String command = split[0] + " " + split[1];
        if (test.getMap.containsKey(command))
            test.getMap.get(command).run();
        if (test.setMap.containsKey(command))
            test.setMap.get(command).accept(Float.parseFloat(split[2]));

        System.out.println("bye bye");
        }
    }