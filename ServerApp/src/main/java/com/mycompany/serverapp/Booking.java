package com.mycompany.serverapp;

import java.time.LocalTime;

public class Booking {
    protected String roomCode; 
    protected String moduleName;
    protected int day;
    protected LocalTime start;
    protected LocalTime end;
    
    public Booking(String _roomCode, String _moduleName, int _day, LocalTime _start, LocalTime _end) {
        this.roomCode = _roomCode;
        this.moduleName = _moduleName;
        this.day = _day;
        this.start = _start;
        this.end = _end;
    }
    
    public int getDay() {
        return this.day;
    }
    
    public LocalTime getStart() {
        return this.start;
    }
    
    public LocalTime getEnd() {
        return this.end;
    }
}
