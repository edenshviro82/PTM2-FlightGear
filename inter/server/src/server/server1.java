package server;
import java.net.*;
import java.io.*;
public class server1 {
    public static void main(String[] args ) throws IOException {
        ServerSocket ss = new ServerSocket(4999);
        Socket s = ss.accept();
        System.out.println("client has connected");
        PrintWriter out = new PrintWriter(s.getOutputStream());
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            while (true) {
                out.println("choose a number:");
                out.flush();
                String str = in.readLine();
                System.out.println("client:" + str);
                if (str.equals("bye"))
                 break;
            }
        }
    }

