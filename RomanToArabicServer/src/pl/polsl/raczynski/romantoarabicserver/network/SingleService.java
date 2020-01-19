/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.polsl.raczynski.romantoarabicserver.network;

import java.net.*;
import java.io.*;
//import pl.polsl.raczynski.romantoarabicserver.controler.RomanToArabicServerControler;
import pl.polsl.raczynski.romantoarabicserver.model.*;

/**
 *Class that operates a Single Service
 * 
 * @author Jasiek
 * @version 1.0
 */
public class SingleService extends Thread implements Closeable {

    /**
     * socket representing connection to the client.
     */
    private final Socket socket;
    /**
     * buffered input character stream.
     */
    private final BufferedReader inputFromClient;
    /**
     * Formatted output character stream.
     */
    private final PrintWriter outputToClient;
    /**
     * Object representing server controller.
     */
    //private final RomanToArabicServerControler controller;

    /**
     * The constructor of instance of the SingleService class. Uses the socket
     * and server controller object as a parameters.
     *
     * @param socket socket representing connection to the client.
     * @throws IOException when has problem with creating a stream.
     */
    //public SingleService(RomanToArabicServerControler controller, Socket socket) throws IOException {
    public SingleService(Socket socket) throws IOException {
        this.socket = socket;
        //this.controller = controller;

        outputToClient = new PrintWriter(
                new BufferedWriter(
                        new OutputStreamWriter(
                                socket.getOutputStream())), true);
        inputFromClient = new BufferedReader(
                new InputStreamReader(
                        socket.getInputStream()));

    }

    /**
     * Sends server's messages to the client.
     *
     * @param messages strings with messages which should be sent to the client.
     */
    private void sendMessagesToClient(String... messages) {
        outputToClient.println("==START==");
        for (String m : messages) {
            outputToClient.println(m);
        }
        outputToClient.println("==END==");
    }

    /**
     * Checks which command was choosen and takes proper steps (for example
     * invokes special method).
     */
    @Override
    public void run() {
        try {
            sendMessagesToClient("Welcome! Type HELP to get information about available commands.");
            RomanNumber numberToConvert = new RomanNumber();
            ArabicNumber convertedNumber = new ArabicNumber();
            Converter conversionAlgorithm = new Converter();
            boolean stop = false;
            int order = 0;
            while (!stop) {
                String str = inputFromClient.readLine().toUpperCase();
                System.out.println("Client sent command: " + str);
                if (str == null) {
                    stop = true;
                } else {
                    String[] args = str.split(" ");
                    String command = args[0];
                    command = command.trim();
                    switch (command) {
                        case "HELLO":
                            if (args.length == 1 ) {
                                if (order == 0) {
                                sendMessagesToClient("Server connection made.");
                                order = order + 1;
                                }
                                else {
                                    sendMessagesToClient("WRONG ORDER OF COMMANDS!");
                                }
                            } else {
                                sendMessagesToClient("WRONG AMOUNT OF PARAMETERS!");
                            }
                            break;
                        case "SET":
                            if (args.length == 2) {
                                if (order == 1) {
                                    sendMessagesToClient("Setting roman number.");
                                    numberToConvert.setRomanNumber(args[1]);
                                    order = order + 1;
                                }
                                else {
                                    sendMessagesToClient("WRONG ORDER OF COMMANDS!");
                                }
                            } else {
                                sendMessagesToClient("WRONG AMOUNT OF PARAMETERS!");
                            }
                            break;
                        case "RUN":
                            if (args.length == 1) {
                                if (order == 2) {
                                    sendMessagesToClient("Converting roman number.");
                                    convertedNumber.setArabicNumber(conversionAlgorithm.conversionFromRomanToArabic(numberToConvert.getRomanNumber()));
                                    order = order + 1;
                                }
                                else {
                                    sendMessagesToClient("WRONG ORDER OF COMMANDS!");
                                }
                            } else {
                                sendMessagesToClient("WRONG AMOUNT OF PARAMETERS!");
                            }
                            break;
                        case "GET":
                            if (args.length == 1) {
                                if (order == 3) {
                                    sendMessagesToClient("Arabic number is: " + convertedNumber.getArabicNumber());
                                    convertedNumber.setArabicNumber(conversionAlgorithm.conversionFromRomanToArabic(numberToConvert.getRomanNumber()));
                                    order = 1;
                                }
                                else {
                                    sendMessagesToClient("WRONG ORDER OF COMMANDS!");
                                }
                            } else {
                                sendMessagesToClient("WRONG AMOUNT OF PARAMETERS!");
                            }
                            break;
                        case "HELP":
                            if (args.length == 1) {
                                displayHelpCommand();
                            } else {
                                sendMessagesToClient("WRONG AMOUNT OF PARAMETERS!");
                            }
                            break;
                        case "QUIT":
                            if (args.length == 1) {
                                stop = true;
                            } else {
                                sendMessagesToClient("WRONG AMOUNT OF PARAMETERS!");
                            }
                            break;
                        default:
                            sendMessagesToClient("UNKNOWN COMMAND: " + "\"" + str + "\"");
                            break;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                close();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }



    /**
     * Sends message with available commands and information about them to
     * client.
     */
    private void displayHelpCommand() {
        sendMessagesToClient("Available commands: HELLO - check connection with server, "
                + "HELP - displays list of available commands, "
                + "QUIT - disconnects client from server, "
                + "SET[parameter] - allows client to choose number to convert, "
                + "RUN - runs the conversion algorithm"
                + "GET - gets converted value");
    }

    /**
     * Closes socket (if it is open).
     *
     * @throws IOException when there is a problem with closing socket.
     */
    @Override
    public void close() throws IOException {
        System.out.println("Single service closing.");
        sendMessagesToClient(" Single service is closed!");
        if (socket != null) {
            socket.close();
        }
    }
}
