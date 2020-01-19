/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.polsl.raczynski.romantoarabicserver.controler;

import pl.polsl.raczynski.romantoarabicserver.network.TCPServer;
import java.io.*;

/**
 * Class to initialize program
 *
 * @author Jasiek
 * @version 1.0
 */
public class RomanToArabicServerEntrypoint {

    /**
     * Main function of program
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
                try {
            new TCPServer().run();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    
}
