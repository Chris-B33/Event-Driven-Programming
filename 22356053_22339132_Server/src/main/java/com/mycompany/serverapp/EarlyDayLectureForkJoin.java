package com.mycompany.serverapp;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class EarlyDayLectureForkJoin extends RecursiveTask<String> {
    private static final String[] dayNames = {
        "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
    };
    
    private final String courseName;
    private final HashMap<String, Schedule> schedules;
    private final int start;
    private final int end;
    
    EarlyDayLectureForkJoin(String _courseName, HashMap<String, Schedule> _schedules, int _start, int _end) {
        this.courseName = _courseName;
        this.schedules = _schedules;
        this.start = _start;
        this.end = _end;
    }
    
    @Override
    protected synchronized String compute() {
        Schedule courseSchedule = schedules.get(this.courseName);
        if(end != courseSchedule.getNumberOfTimesScheduled()) {
            ArrayList<Booking> bookings = courseSchedule.getBookings();
            int day = bookings.get(this.start).getDay();
            LocalTime earliestSlot = this.getEarliestSlot(day);
            
            long pushBack = ChronoUnit.MINUTES.between(earliestSlot, bookings.get(this.start).getStart());
            
            if (pushBack > 0) {
                for (Booking bk : bookings) {
                    if (bk.day == day) {
                        bk.start = bk.start.minus(pushBack, ChronoUnit.MINUTES);
                        bk.end = bk.end.minus(pushBack, ChronoUnit.MINUTES);
                    }
                }
                return dayNames[day];
            } else {
                return "";
            }
        } 
        else if (this.start < this.end) {
            int i;
            ArrayList<Booking> bookings = courseSchedule.getBookings();
            for (i=this.start + 1; i<this.end; i++) {
                if (bookings.get(i - 1).getDay() != bookings.get(i).getDay()) {
                    break;
                }
            }
            
            EarlyDayLectureForkJoin left  = new EarlyDayLectureForkJoin(this.courseName, this.schedules, this.start, i - 1);
            EarlyDayLectureForkJoin right = new EarlyDayLectureForkJoin(this.courseName, this.schedules, i, this.end);
            
            left.fork();
            String rightAns = right.compute();
            String leftAns  = left.join();
            if (leftAns.equals("")) {
                return rightAns;
            }
            return leftAns + " " + rightAns;
         }
        return "";
    }
    
    private LocalTime getEarliestSlot(int day) {
        LocalTime earliestSlot = LocalTime.parse("09:00");
        
        for (String scheduleName : schedules.keySet()) {
                Schedule schedule = schedules.get(scheduleName);
                if (scheduleName.equals(this.courseName)) {
                    continue;
                }
                
                for (Booking b : schedule.getBookings()) {
                    if (day == b.getDay() && b.getEnd().compareTo(earliestSlot) > 0) {
                        earliestSlot = b.getEnd();
                    }
                }
                
        }
        return earliestSlot;
    }
    
    static String earlyize(String name, HashMap<String, Schedule> schedules, int start, int end) {
        return ForkJoinPool.commonPool().invoke(new EarlyDayLectureForkJoin(name, schedules, start, end));
    }
}