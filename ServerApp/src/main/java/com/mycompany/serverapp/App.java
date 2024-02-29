package com.mycompany.serverapp;

import java.io.*;
import java.net.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

public class App {
    private static final String[] dayNames = {
        "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
    };
    private static final int borderNum = 75;
    
    private static ServerSocket servSock;
    private static final int PORT = 1234;

    private static Socket link;
    private static BufferedReader input;
    private static PrintWriter output;

    private static String action;
    private static String description;
    
    private static HashMap<String, Schedule> courseSchedules;

    /*
      Main Method
    */
    public static void main(String[] args) {
        courseSchedules = new HashMap<>();
        
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
            String roomCode = arguments[1];
            String moduleName = arguments[2];
            int day = Integer.parseInt(arguments[3]);
            LocalTime startTime = LocalTime.parse(arguments[4].split("-")[0]);
            LocalTime endTime = LocalTime.parse(arguments[4].split("-")[1]);
            
            if (!courseSchedules.containsKey(name)) {
                courseSchedules.put(name, new Schedule());
            }
            
            Schedule courseSchedule = courseSchedules.get(name);
            if (!courseSchedule.addClassToSchedule(roomCode, moduleName, day, startTime, endTime)) {
                throw new IncorrectActionException("Class clashes with other booking.");
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
            if (description.split(" ").length < 5) {
                throw new IncorrectActionException("Invalid description.");
            }
            String name = arguments[0];
            String roomCode = arguments[1];
            String moduleName = arguments[2];
            int day = Integer.parseInt(arguments[3]);
            LocalTime startTime = LocalTime.parse(arguments[4].split("-")[0]);
            LocalTime endTime = LocalTime.parse(arguments[4].split("-")[1]);
            
            if (!courseSchedules.containsKey(name)) {
                throw new IncorrectActionException("Course doesn't have any classes yet.");
            }
            
            Schedule courseSchedule = courseSchedules.get(name);
            if (!courseSchedule.removeClassFromSchedule(roomCode, moduleName, day, startTime, endTime)) {
                throw new IncorrectActionException("No class to remove from specified course.");
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
            if (!courseSchedules.containsKey(arguments[0])) {
                throw new IncorrectActionException("Course schedule doesn't exist yet.");
            }

            Schedule cur = courseSchedules.get(arguments[0]); 
            ArrayList<Integer> days = cur.getDays();
            ArrayList<LocalTime> startTimes = cur.getStartTimes();
            ArrayList<LocalTime> endTimes = cur.getEndTimes();

            for (int i=0; i<cur.getNumberOfTimesScheduled(); i++) {
                System.out.printf("%s: %s -> %s\n", dayNames[days.get(i)], startTimes.get(i), endTimes.get(i));
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
