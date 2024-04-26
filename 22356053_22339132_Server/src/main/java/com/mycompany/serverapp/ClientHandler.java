package com.mycompany.serverapp;

import java.io.*;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.*;

public class ClientHandler implements Runnable {
    private static final String[] dayNames = {
        "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
    };
    private static HashMap<String, Schedule> courseSchedules = new HashMap<>();
    private static final int borderNum = 100;
    
    private String name;
    private Socket link;
    private BufferedReader input;
    private PrintWriter output;
    
    private ExecutorService executor;
    
    private String action;
    private String description;
    
    public ClientHandler(Socket _link, String _name) {
        this.name = _name;
        this.link = _link;
        this.executor = Executors.newCachedThreadPool();
    }
    
    @Override
    public synchronized void run() {
        try {
            input = new BufferedReader(new InputStreamReader(link.getInputStream()));
            output = new PrintWriter(link.getOutputStream(), true);
            
            while(true) {
                action = input.readLine().toUpperCase();
                description = input.readLine();
                
                System.out.println("[NAME]:        " + this.name);
                System.out.println("[ACTION]:      " + action);
                System.out.println("[DESCRIPTION]: " + description);
                System.out.println("-".repeat(borderNum));

                switch (action) {
                    case "ADD":
                        executor.execute(this::addToSchedule);
                        break;
                    case "REMOVE":
                        executor.execute(this::removeFromSchedule);
                        break;
                    case "DISPLAY":
                        executor.execute(this::displaySchedule);
                        break;
                    case "STOP":
                        executor.execute(this::closeConnection);
                        break;
                    case "EARLY":
                        executor.execute(this::tryEarlyLectures);
                        break;
                    default:
                        throw new IncorrectActionException();
                }
            }
        } catch (IncorrectActionException | IOException e) {
            System.out.println("[ERROR]: " + e);
            System.out.println("-".repeat(borderNum));
            output.println(e.getMessage());
        }   
    }
    
    /*
      addToSchedule Method
    */
    private void addToSchedule() {
        try {
            String[] arguments = description.split(" ");
            String name = arguments[0];
            String moduleName = arguments[1];
            String roomCode = arguments[2];
            int day = Integer.parseInt(arguments[3]);
            LocalTime startTime = LocalTime.parse(arguments[4], DateTimeFormatter.ofPattern("H:m"));
            LocalTime endTime = LocalTime.parse(arguments[5], DateTimeFormatter.ofPattern("H:m"));
            
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
            System.out.println("-".repeat(borderNum));
            output.println(e.getMessage());
        }
    }

    /*
      removeFromSchedule Method
    */
    private void removeFromSchedule() {
        try {           
            String[] arguments = description.split(" ");
            String name = arguments[0];
            String moduleName = arguments[1];
            String roomCode = arguments[2];
            int day = Integer.parseInt(arguments[3]);
            LocalTime startTime = LocalTime.parse(arguments[4], DateTimeFormatter.ofPattern("H:m"));
            LocalTime endTime = LocalTime.parse(arguments[5], DateTimeFormatter.ofPattern("H:m"));
            
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
            System.out.println("-".repeat(borderNum));
            output.println(e.getMessage());
        }
    }

    /*
      displaySchedule Method
    */
    private void displaySchedule() {
        try {            
            String[] arguments = description.split(" ");           
            if (!courseSchedules.containsKey(arguments[0]) || courseSchedules.get(arguments[0]).getNumberOfTimesScheduled() == 0) {
                throw new IncorrectActionException("Course schedule is null or doesn't exist.");
            }

            Schedule cur = courseSchedules.get(arguments[0]); 
            ArrayList<Booking> bookings = cur.getBookings();
            
            String outputStr = "DISPLAY ";
            
            int j = -1;
            for (int i=0; i<cur.getNumberOfTimesScheduled(); i++) {  
                String dy = dayNames[bookings.get(i).day];
                String mn = bookings.get(i).moduleName;
                String rc = bookings.get(i).roomCode;
                String st = bookings.get(i).start.toString();
                String et = bookings.get(i).end.toString();
                outputStr += String.format("%s %s %s %s %s ", dy, mn, rc, st, et);
            }
            output.println(outputStr);
        } 
        catch (IncorrectActionException e) {
            System.out.println("[ERROR]: " + e);
            System.out.println("-".repeat(borderNum));
            output.println(e.getMessage());
        }
    }

    /*
      closeConnection Method
    */
    private void closeConnection() {
        output.println("TERMINATE");
        try {
            link.close();
        } catch (IOException e) {
            System.out.println("[ERROR]: Unable to close connection");
            System.out.println("-".repeat(borderNum));
            System.exit(0);
        }
    }
    
    /*
        tryEarlyLectures Method
    */
    private void tryEarlyLectures() {
        try {           
            String[] arguments = description.split(" ");
            String name = arguments[0];
            
            if (!courseSchedules.containsKey(name) || courseSchedules.get(name).getNumberOfTimesScheduled() == 0) {
                throw new IncorrectActionException("Course schedule is null or doesn't exist.");
            }    

            String earlyDays = EarlyDayLectureForkJoin.earlyize(
                    name, 
                    courseSchedules, 
                    0, 
                    courseSchedules.get(name).getNumberOfTimesScheduled()
            );
            
            if (earlyDays.equals("")) {
                throw new IncorrectActionException("No days could be pushed back for early lectures.");
            }
            
            output.println("( " + earlyDays + ") were pushed back.");
        }
        catch (IncorrectActionException e) {
            System.out.println("[ERROR]: " + e);
            System.out.println("-".repeat(borderNum));
            output.println(e.getMessage());
        }
    }
}
