package com.mycompany.serverapp;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/*
    Schedule for each course
*/
public class Schedule {
    private ArrayList<Booking> bookings;
    
    public Schedule() {
        bookings = new ArrayList<>();
    }
    
    public ArrayList<Booking> getBookings() {
        return this.bookings;
    }
    
    public int getNumberOfTimesScheduled() {
        return this.bookings.size();
    }
    
    public ArrayList<String> getUniqueModuleNames() {
        ArrayList<String> uniqueModules = new ArrayList<>();
        for (Booking cur : bookings) {
            if (!uniqueModules.contains(cur.moduleName)) {
                uniqueModules.add(cur.moduleName);
            }
        }
        return uniqueModules;
    } 
    
    public boolean checkForClashWithIndividualClass(LocalTime start1, LocalTime start2, LocalTime end1, LocalTime end2) {
            return ((start1.compareTo(start2) >= 0 && start1.compareTo(end2) < 0) ||
                    (end1.compareTo(start2) > 0 && end1.compareTo(end2) <= 0));
    }
   
    public boolean checkForClashInSchedule(String roomCode, int day, LocalTime start, LocalTime end) {
        for(int i=0; i<this.getNumberOfTimesScheduled(); i++) {
            if (day != bookings.get(i).day) {
                continue;
            }
            
            if (this.checkForClashWithIndividualClass(start, bookings.get(i).start, end, bookings.get(i).end)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean addClassToSchedule(String roomCode, String moduleName, int day, LocalTime start, LocalTime end) {
        if (this.checkForClashInSchedule(roomCode, day, start, end)) {
            return false;
        }
        
        bookings.add(new Booking(roomCode, moduleName, day, start, end));
        this.sortBookings();
        return true;
    }
    
    public boolean removeClassFromSchedule(String roomCode, String moduleName, int day, LocalTime start, LocalTime end) {
        int i;
        for(i=0; i<this.getNumberOfTimesScheduled(); i++) {
            if (
                    roomCode.equals(bookings.get(i).roomCode) &&
                    moduleName.equals(bookings.get(i).moduleName) &&
                    day == bookings.get(i).day &&
                    start.equals(bookings.get(i).start) &&
                    end.equals(bookings.get(i).end)
            ) {
                bookings.remove(i);
                return true;
            }
        }        
        return false;
    }
    
    public void sortBookings() {
        Collections.sort(this.bookings, Comparator.comparing(Booking::getDay)
            .thenComparing(Booking::getStart)
            .thenComparing(Booking::getEnd)
        );
    }
}
