package com.mycompany.serverapp;

import java.io.*;
import java.net.*;

public class App {
    private static final int borderNum = 100;
    private static ServerSocket servSock;
    private static final int PORT = 1234;
    private static int clientConnections = 0;
    
    
    public static void main(String[] args) {
        System.out.println("-".repeat(borderNum));
        System.out.println("[INITIALIZE] Opening Port " + PORT);
        System.out.println("-".repeat(borderNum));
        try 
        {
            servSock = new ServerSocket(PORT);
        }
        catch(IOException e) 
        {
             System.out.println("[ERROR] Unable to attach to port!");
             System.out.println("-".repeat(borderNum));
             System.exit(1);
        }

        while (true) {
            run();
        }
    }

    private static void run()
    {
        try 
        {
            Thread t = new Thread(new ClientHandler(
                    servSock.accept(),
                    "Client #" + clientConnections++
            ));
            t.start();  
        }
        catch(IOException e) {}
    }
  
    
}
