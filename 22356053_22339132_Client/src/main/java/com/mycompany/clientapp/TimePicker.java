package com.mycompany.clientapp;

import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TimePicker extends VBox{
    int hour;
    int minute;
    String title;
    
    HBox time;
    VBox format;
    Spinner <Integer> hourSpinner;
    Spinner <Integer> minuteSpinner;
    SpinnerValueFactory<Integer> hourValueFactory;
    SpinnerValueFactory<Integer> minuteValueFactory;
    ClockPane clock;
    
    TimePicker() {
        this(9, 0, "Time");
    }
    
    TimePicker(int _hour, int _minute, String _title) {
        this.hour = _hour;
        this.minute = _minute;
        this.title = _title;
        
        clock = new ClockPane();
        hourSpinner = new Spinner<>();
        minuteSpinner = new Spinner<>();
        hourSpinner.setPrefSize(75, 25);
        minuteSpinner.setPrefSize(75, 25);
        hourSpinner.setPromptText("hour");
        minuteSpinner.setPromptText("minute");

        hourValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,23,hour);
        minuteValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59,minute,5);
        hourSpinner.setValueFactory(hourValueFactory);
        minuteSpinner.setValueFactory(minuteValueFactory);
        
        hourSpinner.valueProperty().addListener((observable, oldValue, newValue) ->{
            if(oldValue != newValue){
                hour = hourSpinner.getValue();
                clock.setHour(hour);
            }
        });

        minuteSpinner.valueProperty().addListener((observable, oldValue, newValue) ->{
            if(oldValue != newValue){
                minute = minuteSpinner.getValue();
                clock.setMinute(minute);
            }
        }); 
        
        clock.widthProperty().addListener(ov -> {
            clock.setW(this.getWidth() - 15);
        });

        clock.heightProperty().addListener(ov -> {
            clock.setH(this.getHeight() - 15);
        });
        
        time = new HBox(3, new Label(title + ": "), hourSpinner, minuteSpinner);
        format = new VBox(2, clock, time);
        this.getChildren().addAll(format);
    }
    
    public String getTime() {
        return hour + ":" + minute;
    }
}