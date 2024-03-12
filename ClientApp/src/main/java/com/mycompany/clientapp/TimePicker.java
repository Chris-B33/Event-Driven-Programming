package com.mycompany.clientapp;

import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TimePicker extends VBox{
    int hour;
    int minute;
    
    HBox time;
    Spinner <Integer> hourSpinner;
    Spinner <Integer> minuteSpinner;
    SpinnerValueFactory<Integer> hourValueFactory;
    SpinnerValueFactory<Integer> minuteValueFactory;
    ClockPane clock;
    
    TimePicker() {
        this(9, 0);
    }
    
    TimePicker(int _hour, int _minute) {
        this.hour = _hour;
        this.minute = _minute;
        
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
            clock.setW(this.getWidth());
        });

        clock.heightProperty().addListener(ov -> {
            clock.setH(this.getHeight());
        });
        
        time = new HBox(2, hourSpinner, minuteSpinner);
        this.getChildren().addAll(clock, time);
    }
}
