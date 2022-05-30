package server;
import java.net.*;
import java.io.*;
import java.util.Scanner;

public class testclient {
    public static void main(String[] args ) throws IOException {

        Socket s = new Socket("localhost",4999);
        PrintWriter out = new PrintWriter(s.getOutputStream());
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        while (true) {
            String str = in.readLine();
            System.out.println("server:" + str);
            Scanner usrinput = new Scanner(System.in);
            String num = usrinput.next();
            out.println(num);
            out.flush();
            if (num.equals("bye"))
                break;
        }
    }
}


