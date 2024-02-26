package com.mycompany.serverapp;

import java.io.*;
import java.net.*;

public class ServerApp { 
    private static int borderNum = 40;
    
    private static ServerSocket servSock;
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
                    output.println("Invalid");
                    break;
            }
            
            System.out.println("[ACTION]:      " + action);
            System.out.println("[DESCRIPTION]: " + description);
            System.out.println("-".repeat(borderNum));
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
        output.println("ADD");
    }

    /*
      removeFromSchedule Method
    */
    private static void removeFromSchedule() {
        output.println("REMOVE");
    }

    /*
      displaySchedule Method
    */
    private static void displaySchedule() {
        output.println("DISPLAY");
    }

    /*
      closeConnection Method
    */
    private static void closeConnection() {
        output.println("TERMINATE");
        try {
            link.close();
        } catch (IOException e) {
            System.out.println("Unable to close connection");
            System.exit(0);
        }
    }
}
