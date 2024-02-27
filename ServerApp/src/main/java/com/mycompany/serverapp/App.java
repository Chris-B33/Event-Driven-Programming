package com.mycompany.serverapp;

import java.io.*;
import java.net.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

public class App { 
    private static final int borderNum = 75;
    
    private static ServerSocket servSock;
    private static final int PORT = 1234;

    private static Socket link;
    private static BufferedReader input;
    private static PrintWriter output;

    private static String action;
    private static String description;
    
    // Storage
    private static HashMap<String, Schedule> courseSchedules;

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
            
            System.out.println("[ACTION]:      " + action);
            System.out.println("[DESCRIPTION]: " + description);
            System.out.println("-".repeat(borderNum));
            
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
        try {
            String[] arguments = description.split(" ");
            String name = arguments[0];
            LocalDate date = LocalDate.parse(arguments[1]);
            LocalTime startTime = LocalTime.parse(arguments[2].split("-")[0]);
            LocalTime endTime = LocalTime.parse(arguments[2].split("-")[1]);
            
            // If course exists, continue.
            // Otherwise, create course schedule.
            // Check if times clash with any others in course schedule using binary search (Combine them all into LocalDateTimes).
            // If none are found, add to schedule.
            // Otherwise, throw exception.
            if (true) {
                
            } else {
                throw new IncorrectActionException("Class clashes with other classes in course.");
            }
            
            output.println("Added successfully.");
        }
        catch (IncorrectActionException e) {
            System.out.println("[ERROR]: " + e);
            System.out.println("-".repeat((int)(borderNum)));
            output.println("Incorrect action.");
        }
    }

    /*
      removeFromSchedule Method
    */
    private static void removeFromSchedule() {
        try {
            if (courseSchedules == null) {
                throw new IncorrectActionException("No schedules exist.");
            }
            
            String[] arguments = description.split(" ");
            String name = arguments[0];
            LocalDate date = LocalDate.parse(arguments[1]);
            LocalTime startTime = LocalTime.parse(arguments[2].split("-")[0]);
            LocalTime endTime = LocalTime.parse(arguments[2].split("-")[1]);
            
            // If course exists, continue.
            // Otherwise, throw exception.
            // Go to course in hashmap.
            // Use binary search to search for class with matching date, start time and end time (Combine them all into LocalDateTimes).
            // If found, remove it.
            // Otherwise, throw exception.
            Schedule course = courseSchedules.get(name);
            if (true) {

            } else {
                throw new IncorrectActionException("No class to remove.");
            }
            
            output.println("Removed successfully.");
        }
        catch (IncorrectActionException e) {
            System.out.println("[ERROR]: " + e);
            System.out.println("-".repeat((int)(borderNum)));
            output.println("Incorrect action.");
        }
    }

    /*
      displaySchedule Method
    */
    private static void displaySchedule() {
        try {
            if (courseSchedules == null) {
                throw new IncorrectActionException("No schedules exist.");
            }

            String[] arguments = description.split(" ");

            Schedule cur = courseSchedules.get(arguments[0]);
            ArrayList<LocalDateTime> startTimes = cur.getStartTimes();
            ArrayList<LocalDateTime> endTimes = cur.getEndTimes();

            for (int i=0; i<cur.getNumberOfTimesScheduled(); i++) {
                System.out.printf("%s -> %s\n", startTimes.get(i), endTimes.get(i));
            }
            System.out.println("-".repeat((int)(borderNum)));
            output.println("Displaying in server console.");
        } 
        catch (IncorrectActionException e) {
            System.out.println("[ERROR]: " + e);
            System.out.println("-".repeat((int)(borderNum)));
            output.println("Incorrect action.");
        }
    }

    /*
      closeConnection Method
    */
    private static void closeConnection() {
        output.println("TERMINATE");
        try {
            link.close();
        } catch (IOException e) {
            System.out.println("[ERROR]: Unable to close connection");
            System.out.println("-".repeat(borderNum));
            System.exit(0);
        }
    }
}
