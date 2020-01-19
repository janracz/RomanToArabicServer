/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.polsl.raczynski.romantoarabicserver.network;

import java.net.*;
import java.io.*;
import java.util.Properties;
import pl.polsl.raczynski.romantoarabicserver.controler.*;

/**
 * Class implementing a TCP Server
 * 
 * @author Jasiek
 * @version 1.0
 */
public class TCPServer implements Closeable {

    /**
     * Controller of model object.
     */
    //rivate final RomanToArabicServerControler serverController;
    /**
     * Socket waiting for client connections.
     */
    private final ServerSocket serverSocket;
    /**
     * Port number.
     */
    private Integer port;

    /**
     * Creates single service connection and listens for connections with
     * clients.
     */
    public void run() {
        try {
            System.out.println("Server RomanToArabic started!");
            while (true) {
                Socket socket = serverSocket.accept();
                //SingleService singleService = new SingleService(serverController, socket);
                SingleService singleService = new SingleService(socket);
                singleService.start();
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    /**
     * Closes server's socket (if it is open).
     *
     * @throws IOException when there is a problem with closing socket.
     */
    @Override
    public void close() throws IOException {
        if (serverSocket != null) {
            serverSocket.close();
        }
    }

    /**
     * Constructor of server. Loads properties from file or if there are not any
     * available properties, it uses default values. It also creates controller
     * and server's socket.
     *
     * @throws IOException when there is a problem with creating a socket.
     */
    public TCPServer() throws IOException {
        Properties propertiesToRead = new Properties();
        //Reading properties from file
        try (FileInputStream input = new FileInputStream("RomanToArabic.properties")) {
            propertiesToRead.load(input);
            port = Integer.parseInt(propertiesToRead.getProperty("port"));
        } catch (IOException e) {

            System.out.println("Can not read properties from file. Default properties"
                    + " will be used.");
            port = 4728;
        }
        Properties propertiesToWrite = new Properties();
        //Writing properties to file
        propertiesToWrite.setProperty("port", port.toString());
        try (FileOutputStream out = new FileOutputStream("RomanToArabic.properties")) {
            propertiesToWrite.store(out, "--Configuration--");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        serverSocket = new ServerSocket(port);
        //serverController = new RomanToArabicServerControler();
    }
}
