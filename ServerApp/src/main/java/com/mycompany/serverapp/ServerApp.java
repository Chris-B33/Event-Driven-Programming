package com.mycompany.serverapp;

import java.io.*;
import java.net.*;

public class ServerApp { 
    private static ServerSocket servSock;
    private static boolean running = true;
    private static final int PORT = 1234;

    private static Socket link;
    private static BufferedReader input;
    private static PrintWriter output;

    private static String action;
    private static String description;

    /*
      Main Method
    */
    public static void main(String[] args) {
      System.out.println("Opening port...\n");
      try 
      {
          servSock = new ServerSocket(PORT);
      }
      catch(IOException e) 
      {
           System.out.println("Unable to attach to port!");
           System.exit(1);
      }

      while (running) {
          run();
      }

    }
  
    /*
      Run Method
    */
    private static void run()
    {
        try 
        {
            link = servSock.accept();
            input = new BufferedReader(new InputStreamReader(link.getInputStream()));
            output = new PrintWriter(link.getOutputStream(), true);

            action = input.readLine().toUpperCase();
            description = input.readLine();
            
            switch (action) {
                case "ADD":
                    addToSchedule();
                    break;
                case "REMOVE":
                    removeFromSchedule();
                    break;
                case "DISPLAY":
                    displaySchedule();
                    break;
                case "STOP":
                    closeConnection();
                    break;
                default:
                    break;
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
  
    /*
      addToSchedule Method
    */
    private static void addToSchedule() {
        System.out.println("Adding...");
        output.println("");
    }

    /*
      removeFromSchedule Method
    */
    private static void removeFromSchedule() {
        System.out.println("Removing...");
        output.println("");
    }

    /*
      displaySchedule Method
    */
    private static void displaySchedule() {
        System.out.println("Displaying...");
        output.println("");
    }

    /*
      closeConnection Method
    */
    private static void closeConnection() {
        System.out.println("Closing...");
        output.println("TERMINATE");
        try {
            link.close();
        } catch (IOException e) {
            System.out.println("Unable to close connection");
            System.exit(0);
        }
    }
}
