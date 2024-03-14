package com.mycompany.serverapp;

public class IncorrectActionException extends Exception {
    public IncorrectActionException() {
        super("Invalid Action.");
    }
    
    public IncorrectActionException(String message) {
        super(message);
    }
}
