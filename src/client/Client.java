package client;

import java.net.*;
import java.io.*;
import javax.net.SocketFactory;
import javax.net.ssl.*;
import java.security.KeyStore;
import java.util.Scanner;

public class Client {
    public static void main(String args[]) {
        try {
            // Assigning a port where the client will be running
            int port = 2022;
            // To read input from the keyboard
            Scanner sc= new Scanner(System.in);
            // tell the system who we trust
            System.setProperty("javax.net.ssl.trustStore","src/client/ds.truststore");
            System.setProperty("javax.net.ssl.trustStorePassword","ds2021f");
            // get an SSLSocketFactory
            SocketFactory sf = SSLSocketFactory.getDefault();
            // an SSLSocket "is a" Socket
            // Creating a client socket
            Socket s = sf.createSocket("localhost",port);
            // Creating an output stream object to send data
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            // Creating an input stream object to receive data
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
//            System.out.println("Type 1 to ask Server for current time \n"+"Type 2 to ask Sever for IP of local host\n" + "Type 3 to disconnect");
            boolean active = true;
            while(active){
                System.out.println("\nType 1 to ask Server for current time \n"+"Type 2 to ask Sever for IP of local host\n" + "Type 3 to disconnect");
                // Taking client input
                String userResponse = sc.nextLine();
                if(userResponse.equals("3")){
                    active = false;

                }
                // Sending client input to server
                out.println(userResponse);
                System.out.println("Server has sent message!");
                // Reading server response
                String answer = in.readLine();
                System.out.println(answer);
            }
            // Closing connection
            out.close();
            in.close();
            s.close();


        } catch(Exception e) {
            System.out.println("Exception thrown " + e);
        }
    }
}
