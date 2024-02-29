package com.mycompany.serverapp;

import java.time.LocalTime;
import java.util.ArrayList;

/*
    Schedule for each course
*/
public class Schedule {
    private ArrayList<String> roomCodes; 
    private ArrayList<String> moduleNames;
    private ArrayList<Integer> days;
    private ArrayList<LocalTime> startTimes;
    private ArrayList<LocalTime> endTimes;
    
    public Schedule() {
        roomCodes = new ArrayList<>();
        moduleNames = new ArrayList<>();
        days = new ArrayList<>();
        startTimes = new ArrayList<>();
        endTimes = new ArrayList<>();
    }
    
    public ArrayList<LocalTime> getStartTimes() {
        return this.startTimes;
    }
    
    public ArrayList<LocalTime> getEndTimes() {
        return this.endTimes;
    }
    
    public ArrayList<Integer> getDays() {
        return this.days;
    }
    
    public ArrayList<String> getRoomCodes() {
        return this.roomCodes;
    }
    
    public ArrayList<String> getModuleNames() {
        return this.moduleNames;
    }
    
    public int getNumberOfTimesScheduled() {
        return this.startTimes.size();
    }
    
    public boolean addClassToSchedule(String roomCode, String moduleName, int day, LocalTime start, LocalTime end) {
        for(int i=0; i<this.getNumberOfTimesScheduled(); i++) {
            if (day != days.get(i)) {
                continue;
            }
            
            if (
                    ((startTimes.get(i).compareTo(start) <= 0 && endTimes.get(i).compareTo(start) >= 0) ||
                    (startTimes.get(i).compareTo(end) <= 0 && endTimes.get(i).compareTo(end) >= 0)) &&
                    roomCode.equals(roomCodes.get(i))
            ) {
                return false;
            }
        }
        
        roomCodes.add(roomCode);
        moduleNames.add(moduleName);
        days.add(day);
        startTimes.add(start);
        endTimes.add(end);
        return true;
    }
    
    public boolean removeClassFromSchedule(String roomCode, String moduleName, int day, LocalTime start, LocalTime end) {
        int i;
        for(i=0; i<this.getNumberOfTimesScheduled() - 1; i++) {
            if (
                    roomCode.equals(roomCodes.get(i)) &&
                    moduleName.equals(moduleNames.get(i)) &&
                    day == days.get(i) &&
                    start == startTimes.get(i) &&
                    end == startTimes.get(i)
            ) {
                return false;
            }
        }
        
        roomCodes.remove(i);
        moduleNames.remove(i);
        days.remove(i);
        startTimes.remove(i);
        endTimes.remove(i);
        return true;
    }
}
