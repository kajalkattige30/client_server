package server;

import java.io.*;
import java.net.*;
import javax.net.*;
import javax.net.ssl.*;
import java.security.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.net.InetAddress;

public class Server {
    // hold the name of the keystore containing public and private keys
    static String keyStore = "src/server/dskeystore";
    // password of the keystore (same as the alias)
    static char keyStorePass[] = "ds2021f".toCharArray();

    public static void main(String args[]) {
        // assigning a port where server will be running
        int port = 2022;
        SSLServerSocket server;
        try {
            // get the keystore into memory
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream(keyStore), keyStorePass);
            // initialize the key manager factory with the keystore data
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, keyStorePass);

            // initialize the SSLContext engine
            // may throw NoSuchProvider or NoSuchAlgorithm exception
            // TLS - Transport Layer Security most generic
            SSLContext sslContext = SSLContext.getInstance("TLS");
            // Initialize context with given KeyManagers, TrustManagers,
            // SecureRandom defaults taken if null
            sslContext.init(kmf.getKeyManagers(), null, null);
            // Get ServerSocketFactory from the context object
            ServerSocketFactory ssf = sslContext.getServerSocketFactory();

            // Now like programming with normal server sockets
            ServerSocket serverSocket = ssf.createServerSocket(port);
            System.out.println("Accepting secure connections");

            // a blocking call to wait for incoming client connections
            Socket client = serverSocket.accept();
            System.out.println("Got connection");
            // Creating an output stream object to send data to client
            PrintWriter pw = new PrintWriter(client.getOutputStream(), true) ;
            // Creating an input stream object to read data sent from client
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            boolean connected = true;
            while(connected){
//            System.out.println("To get message ...");
                // readLine will get the client response and store it in the String 'msg'
                String msg = in.readLine();

                // To send time information
                if(msg.equals("1")){
                    // creating an object to format and parse date in the following pattern
                    SimpleDateFormat sdt = new SimpleDateFormat("yyyy/MM/dd:HH:mm:ss");
                    long timeInMillis = System.currentTimeMillis();
                    // creating a date object
                    Date date= new Date(timeInMillis);
                    // converting into the specific format
                    String dateTime = sdt.format(date);
                    // sending date and time
                    pw.println(dateTime);
                    System.out.println("Got message: " + msg );

                }
                // To send ip address
                else if(msg.equals("2")){
                    InetAddress ip = null;
                    // Storing instance of InetAddress containing address and hostname
                    try {
                        ip = InetAddress.getLocalHost();
                    }catch (UnknownHostException e){
                        e.printStackTrace();
                    }
                    // Storing local address in the variable ip_address
                    String ip_address = ip.getHostAddress();
                    // Sending ip address information from server to client
                    pw.println(ip_address);
                    System.out.println("Got message: " + msg );

                }
                // To disconnect
                else if(msg.equals("3")){
                    // sending greetings from server to client
                    pw.println("Happy new semester! \n");
                    System.out.println("Got message: " + msg + "\nHappy new semester!");
                    connected = false;
                    // Closing connection
                    in.close();
                    pw.close();
                    serverSocket.close();
                } else {
                    // Sending msg to client to enter correct input
                    System.out.println("Got message: " + msg );
                    pw.println("Please enter either 1,2 or 3 to exit!!!");
                }
            }
        }
        catch (Exception e) {
            System.out.println("Exception thrown " + e);
        }
    }
}