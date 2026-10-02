import java.util.Scanner;
import java.net.*;
import java.io.*;

public class Network {
    public int              role;
    private final Printer   printer;
    private ServerSocket    serverSocket;
    private Socket          clientSocket;
    private PrintWriter     out;
    private BufferedReader  in;
    private String          ip;

    //Source: https://www.baeldung.com/a-guide-to-java-sockets
    public          Network(Printer printer)                {
        Scanner scan = new Scanner(System.in);
        this.printer = printer;
        System.out.print("Host(H) or Client(any input)?");
        if (scan.next().equals("H") || scan.next().equals("h")) {
            role = 1;
            getIP();
            System.out.println("Waiting for Client...");
        } else {
            role = 0;
            System.out.print("Please enter the IP");
            ip = scan.next();}
    }
    public void     getIP()                                 {
        try {
            InetAddress address = InetAddress.getLocalHost();
            printer.printIP(address.getHostAddress());
        } catch (UnknownHostException e) {throw new RuntimeException(e);}
    }
    public void     buildConnection() throws IOException    {
        if (role == 1) {
            serverSocket = new ServerSocket(6666);
            clientSocket = serverSocket.accept();
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            if (Printer.cont.equals(in.readLine())) {
                out.println(Printer.cont);
                System.out.println("Connected!");}
        } else {
            clientSocket = new Socket(ip, 6666);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out.println(Printer.cont);
            System.out.println("Connecting...");
            if (Printer.cont.equals(in.readLine()))
                System.out.println("Connected!");}
    }
    public void     closeConnection() throws IOException    {
        in.close();
        out.close();
        clientSocket.close();
        if (role == 1)
            serverSocket.close();
    }
    public String   receiveSignal()                         {
        try {
            return in.readLine();
        } catch (IOException e) {
            return printer.errorMess;}}
    public void     sendSignal(String input)                {out.println(input);}
}