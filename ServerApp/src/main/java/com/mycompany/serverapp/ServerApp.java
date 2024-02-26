package com.mycompany.serverapp;

import java.io.*;
import java.net.*;

public class ServerApp { 
  private static ServerSocket servSock;
  private static boolean running = true;
  private static final int PORT = 1234;
  
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
    Socket link = null;
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
          case "TERMINATE":
              closeConnection();
              break;
          default:
              break;
      }
      System.out.println("Action: " + action);
      System.out.println("Description: " + description);
      output.println(action + ", " + description);
     }
    catch(IOException e)
    {
        e.printStackTrace();
    }
    finally 
    {
       try {
	    System.out.println("\n* Closing connection... *");
            link.close();				    //Step 5.
	}
       catch(IOException e)
       {
            System.out.println("Unable to disconnect!");
	    System.exit(1);
       }
    }
  }
  
  /*
    addToSchedule Method
  */
  private static void addToSchedule() {
    System.out.println("Adding...");
    
  }
  
  /*
    removeFromSchedule Method
  */
  private static void removeFromSchedule() {
    System.out.println("Removing...");
    
  }
  
  /*
    displaySchedule Method
  */
  private static void displaySchedule() {
    System.out.println("Displaying...");
    
  }
  
  /*
    closeConnection Method
  */
  private static void closeConnection() {
    System.out.println("Closing...");
    
  }
}
