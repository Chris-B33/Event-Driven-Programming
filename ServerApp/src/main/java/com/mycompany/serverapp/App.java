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
    private static final int borderNum = 100;
    
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
                    throw new IncorrectActionException();
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        catch (IncorrectActionException e) {
            output.println(e.getMessage());
        }
    }
  
    /*
      addToSchedule Method
    */
    private static void addToSchedule() {
        try {
            String[] arguments = description.split(" ");
            String name = arguments[0];
            String moduleName = arguments[1];
            String roomCode = arguments[2];
            int day = Integer.parseInt(arguments[3]);
            LocalTime startTime = LocalTime.parse(arguments[4]);
            LocalTime endTime = LocalTime.parse(arguments[5]);
            
            if (!courseSchedules.containsKey(name)) {
                courseSchedules.put(name, new Schedule());
            }
            
            Schedule courseSchedule = courseSchedules.get(name);
            
            ArrayList<String> uniqueModules = courseSchedule.getUniqueModuleNames();
            if (uniqueModules.size() == 5 && !uniqueModules.contains(moduleName)) {
               throw new IncorrectActionException("Course already has 5 modules.");
            }
           
            
            // Check for clashes in other schedules
            for (String scheduleName : courseSchedules.keySet()) {
                Schedule curSchedule = courseSchedules.get(scheduleName);
                ArrayList<Booking> curBookings = curSchedule.getBookings();
                        
                if (name.equals(scheduleName)) {
                    continue;
                }
                
                for (int i=0; i<curSchedule.getNumberOfTimesScheduled(); i++) {
                    if (
                            curSchedule.checkForClashWithIndividualClass(
                                    startTime, curBookings.get(i).start, endTime, curBookings.get(i).end
                            ) &&
                            roomCode.equals(curBookings.get(i).roomCode)
                    ) {
                        throw new IncorrectActionException("Class clashes with booking in other course.");
                    }
                }
            }
            
            // Try add course to schedule
            if (!courseSchedule.addClassToSchedule(roomCode, moduleName, day, startTime, endTime)) {
                throw new IncorrectActionException("Class clashes with booking in same course.");
            }
            
            output.println("Added successfully.");
        }
        catch (IncorrectActionException e) {
            System.out.println("[ERROR]: " + e);
            System.out.println("-".repeat((int)(borderNum)));
            output.println(e.getMessage());
        }
    }

    /*
      removeFromSchedule Method
    */
    private static void removeFromSchedule() {
        try {           
            String[] arguments = description.split(" ");
            String name = arguments[0];
            String moduleName = arguments[1];
            String roomCode = arguments[2];
            int day = Integer.parseInt(arguments[3]);
            LocalTime startTime = LocalTime.parse(arguments[4]);
            LocalTime endTime = LocalTime.parse(arguments[5]);
            
            if (!courseSchedules.containsKey(name)) {
                throw new IncorrectActionException("Course doesn't have any classes.");
            }
            
            Schedule courseSchedule = courseSchedules.get(name);
            if (!courseSchedule.removeClassFromSchedule(roomCode, moduleName, day, startTime, endTime)) {
                throw new IncorrectActionException("Specificed class doesn't exist in course schedule.");
            }
            
            output.printf("Room %s is now free at %s until %s.\n", roomCode, startTime, endTime);
        }
        catch (IncorrectActionException e) {
            System.out.println("[ERROR]: " + e);
            System.out.println("-".repeat((int)(borderNum)));
            output.println(e.getMessage());
        }
    }

    /*
      displaySchedule Method
    */
    private static void displaySchedule() {
        try {            
            String[] arguments = description.split(" ");           
            if (!courseSchedules.containsKey(arguments[0]) || courseSchedules.get(arguments[0]).getNumberOfTimesScheduled() == 0) {
                throw new IncorrectActionException("Course schedule is null or doesn't exist.");
            }

            Schedule cur = courseSchedules.get(arguments[0]); 
            ArrayList<Booking> bookings = cur.getBookings();

            for (int i=0; i<cur.getNumberOfTimesScheduled(); i++) {
                System.out.printf("%s-%s-%s: %s -> %s\n", 
                        bookings.get(i).roomCode, 
                        bookings.get(i).moduleName, 
                        dayNames[bookings.get(i).day], 
                        bookings.get(i).start, 
                        bookings.get(i).end
                );
            }
            System.out.println("-".repeat((int)(borderNum)));
            output.println("Displaying in server console.");
        } 
        catch (IncorrectActionException e) {
            System.out.println("[ERROR]: " + e);
            System.out.println("-".repeat((int)(borderNum)));
            output.println(e.getMessage());
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
