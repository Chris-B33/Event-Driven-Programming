package com.mycompany.serverapp;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Schedule {
    private ArrayList<LocalDateTime> startTimes;
    private ArrayList<LocalDateTime> endTimes;
    
    public ArrayList<LocalDateTime> getStartTimes() {
        return this.startTimes;
    }
    
    public ArrayList<LocalDateTime> getEndTimes() {
        return this.endTimes;
    }
    
    public int getNumberOfTimesScheduled() {
        return this.startTimes.size();
    }
}
