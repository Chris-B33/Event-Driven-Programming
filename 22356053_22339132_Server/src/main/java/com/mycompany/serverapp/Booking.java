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
    
    public String getRoomCode() {
        return this.roomCode;
    }
    
    public String getModuleName() {
        return this.moduleName;
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
    
    public void setRoomCode(String _roomCode) {
        this.roomCode = _roomCode;
    }
    
    public void setModuleName(String _moduleName) {
        this.moduleName = _moduleName;
    }
    
    public void setDay(int _day) {
        this.day = _day;
    }
    
    public void setStart(LocalTime _start) {
        this.start = _start;
    }
    
    public void setEnd(LocalTime _end) {
        this.end = _end;
    }
}
