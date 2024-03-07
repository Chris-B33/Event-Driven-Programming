package com.mycompany.clientapp;

import java.io.BufferedReader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.MenuButton;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import java.time.LocalTime;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * JavaFX App
 */
public class App extends Application {
    static InetAddress host;
    static final int PORT = 1234;

    private static BufferedReader input;
    private static PrintWriter output;

    private static String action;
    private static String description;

    static Scene scene;
    Label responseLabel;
    Button sendButton;

    Button addButton;
    Button removeButton;
    Button displayButton;
    Button powerButton;
    Label courseLabel;
    TextField course;
    Label modLabel;
    TextField module;
    Label roomLabel;
    TextField room;
    MenuButton day;
    Label dayLabel;
    int dayNum;
    MenuButton startTime;
    Label startLabel;
    MenuButton endTime;
    Label endLabel;
    LocalTime start;
    LocalTime end;
    VBox box;

    @Override
    public void start(Stage stage) throws IOException {       
        responseLabel = new Label("Server Response: ");
        sendButton = new Button("Confirm");

        addButton = new Button("Add");
        removeButton = new Button("Remove");
        displayButton = new Button("Display Schedule");
        powerButton = new Button("Quit");

        course = new TextField("");
        courseLabel = new Label("Course code:");
        module = new TextField("");
        modLabel = new Label("Module:");
        room = new TextField("");
        roomLabel = new Label("Room code:");
        day = new MenuButton("Select Day");
        
        dayLabel = new Label("");
        Days(day);
        
        startTime = new MenuButton("Select Start Time");
        startLabel = new Label("");
        times(startTime, startLabel, start);
        
        endTime = new MenuButton("Select End Time");
        endLabel = new Label("");
        times(endTime, endLabel, end);
        
        powerButton.setPrefWidth(150);
        powerButton.setPrefHeight(50);
        sendButton.setPrefWidth(500);

        style(addButton);
        style(removeButton);
        style(displayButton);
        powerButton.setStyle("-fx-background-color: red; -fx-background-radius: 30; -fx-background-insets: 0; -fx-text-fill: White");

        addButton.setOnAction((ActionEvent a) -> {
                    if(addButton.getStyle().contains("-fx-background-color: lightblue")){
                        style(addButton);
                        box.getChildren().addAll(courseLabel, course, modLabel, module, roomLabel, room, day, dayLabel, startTime, endTime, sendButton);
                        box.getChildren().removeAll(removeButton,displayButton, powerButton);
                        action = "ADD";
                    }else{
                        style(addButton);
                        box.getChildren().addAll(removeButton, displayButton, powerButton);
                        box.getChildren().removeAll(courseLabel, course, modLabel, module, roomLabel, room, day, dayLabel, startTime, endTime, sendButton);
                        action = "";
                    }
            });

        removeButton.setOnAction((ActionEvent r) -> {
                    if(removeButton.getStyle().contains("-fx-background-color: lightblue")){
                        style(removeButton);
                        box.getChildren().addAll(courseLabel, course, modLabel, module, roomLabel, room, day, dayLabel, startTime, endTime, sendButton);
                        box.getChildren().removeAll(addButton,displayButton, powerButton);
                        action = "REMOVE";
                    }else{
                        style(removeButton);
                        box.getChildren().addAll(addButton, displayButton, powerButton);
                        box.getChildren().removeAll(courseLabel, course, modLabel, module, roomLabel, room, day, dayLabel, startTime, endTime, sendButton);
                        action = "";
                    }
            });

        displayButton.setOnAction((ActionEvent d) -> {
                    if(displayButton.getStyle().contains("-fx-background-color: lightblue")){
                        style(displayButton);
                        box.getChildren().addAll(courseLabel, course, modLabel, module,sendButton);
                        box.getChildren().removeAll(addButton, removeButton, powerButton);
                        action = "DISPLAY";
                    }else{
                        style(displayButton);
                        box.getChildren().addAll(addButton, removeButton, powerButton);
                        box.getChildren().removeAll(courseLabel, course, modLabel, module, sendButton);
                        action = "";
                    }
            });

        powerButton.setOnAction((ActionEvent q) -> {
                    if(powerButton.getStyle().contains("-fx-background-color: red")){
                        powerButton.setStyle("-fx-background-color: darkred; -fx-background-radius: 30; -fx-background-insets: 0; -fx-text-fill: White");
                        box.getChildren().add(sendButton);
                        box.getChildren().removeAll(addButton, removeButton,displayButton);
                        action = "STOP";

                        // COURSE (str) MODULE (str) ROOM (str) DAY (int) TIME1 (LocalTime) TIME2 (LocalTime)  
                        description = "";
                    }else{
                        powerButton.setStyle("-fx-background-color: red; -fx-background-radius: 30; -fx-background-insets: 0; -fx-text-fill: White");
                        box.getChildren().addAll(addButton, removeButton, displayButton);
                        box.getChildren().remove(sendButton);
                        action = "";
                    }
            });

        sendButton.setOnAction((ActionEvent t) -> {
                    try
                    {
                        host = InetAddress.getLocalHost();
                    }
                    catch(UnknownHostException e) 
                    {
                        System.out.println("Host ID not found!");
                        System.exit(1);
                    }

                    Socket link;
                    try
                    {
                        link = new Socket(host,PORT);
                        input = new BufferedReader(new InputStreamReader(link.getInputStream()));
                        output = new PrintWriter(link.getOutputStream(), true);

                        // Message
                        output.println(action);
                        output.println(description);

                        // Response
                        String response;
                        response = input.readLine();
                        responseLabel.setText("Server Response: " + response);

                        // Handling Responses

                        if (response.equals("TERMINATE")) {
                            System.exit(0);
                        }
                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();
                    }
            });
           
        description = course.getText() + " " + room.getText() + " " + module.getText() + " " + dayNum + " " + start + " " + end; 
        box = new VBox(5, responseLabel, addButton, removeButton, displayButton, powerButton);
        box.setAlignment(Pos.CENTER);
        scene = new Scene(box, 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public void style(Button b){
        if(b.getStyle().contains("-fx-background-color: lightblue")){
            b.setStyle("-fx-background-color: slateblue;-fx-background-radius: 8,7,6; -fx-background-insets: 0,1,2; -fx-text-fill: black; -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1)");
        }else{
            b.setStyle("-fx-background-color: lightblue; -fx-background-radius: 8,7,6; -fx-background-insets: 0,1,2; -fx-text-fill: black; -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1)");
        }

        b.setPrefWidth(200);
        b.setPrefHeight(100);
    }

    public void Days(MenuButton d){
        MenuItem m1 = new MenuItem("Monday");
        MenuItem m2 = new MenuItem("Tuesday");
        MenuItem m3 = new MenuItem("Wednesday");
        MenuItem m4 = new MenuItem("Thursday");
        MenuItem m5 = new MenuItem("Friday");
        MenuItem m6 = new MenuItem("Saturday");
        MenuItem m7 = new MenuItem("Sunday");

        d.getItems().addAll(m1, m2, m3, m4, m5, m6, m7);

        EventHandler<ActionEvent> pickDay = new EventHandler<ActionEvent>(){
                public void handle(ActionEvent d){
                    dayLabel.setText(((MenuItem)d.getSource()).getText());
                }
            };

        m1.setOnAction(pickDay);
        m2.setOnAction(pickDay);
        m3.setOnAction(pickDay);
        m4.setOnAction(pickDay);
        m5.setOnAction(pickDay);
        m6.setOnAction(pickDay);
        m7.setOnAction(pickDay);

        if(dayLabel.getText().equals(m1.getText())){
            dayNum = 1;
        }else if(dayLabel.getText().equals( m2.getText())){
            dayNum = 2;
        }else if(dayLabel.getText().equals(m3.getText())){
            dayNum = 3;
        }else if(dayLabel.getText().equals(m4.getText())){
            dayNum = 4;
        }else if(dayLabel.getText().equals(m5.getText())){
            dayNum = 5;
        }else if(dayLabel.getText().equals(m6.getText())){
            dayNum = 6;
        }else if(dayLabel.getText().equals(m7.getText())){
            dayNum = 7;
        }
    }

    public void times(MenuButton t, Label l, LocalTime e){
        MenuItem t1 = new MenuItem("09:00");
        MenuItem t2 = new MenuItem("10:00");
        MenuItem t3 = new MenuItem("11:00");
        MenuItem t4 = new MenuItem("12:00");
        MenuItem t5 = new MenuItem("13:00");
        MenuItem t6 = new MenuItem("14:00");
        MenuItem t7 = new MenuItem("15:00");
        MenuItem t8 = new MenuItem("16:00");
        MenuItem t9 = new MenuItem("17:00");
        MenuItem t10 = new MenuItem("18:00");

        t.getItems().addAll(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);

        EventHandler<ActionEvent> pickTime = new EventHandler<ActionEvent>(){
                public void handle(ActionEvent s){
                    l.setText(((MenuItem)s.getSource()).getText());
                }
            };
            
        t1.setOnAction(pickTime);
        t2.setOnAction(pickTime);
        t3.setOnAction(pickTime);
        t4.setOnAction(pickTime);
        t5.setOnAction(pickTime);
        t6.setOnAction(pickTime);
        t7.setOnAction(pickTime);
        t8.setOnAction(pickTime);
        t9.setOnAction(pickTime);
        t10.setOnAction(pickTime);
        
        if(l.getText().equals(t1.getText())){
            e = LocalTime.parse("09:00");
        }else if(l.getText().equals(t2.getText())){
            e = LocalTime.parse("10:00");
        }else if(l.getText().equals(t3.getText())){
            e = LocalTime.parse("11:00");
        }else if(l.getText().equals(t4.getText())){
            e = LocalTime.parse("12:00");
        }else if(l.getText().equals(t5.getText())){
            e = LocalTime.parse("13:00");
        }else if(l.getText().equals(t6.getText())){
            e = LocalTime.parse("14:00");
        }else if(l.getText().equals(t7.getText())){
            e = LocalTime.parse("15:00");
        }else if(l.getText().equals(t8.getText())){
            e = LocalTime.parse("16:00");
        }else if(l.getText().equals(t9.getText())){
            e = LocalTime.parse("17:00");
        }else if(l.getText().equals(t10.getText())){
            e = LocalTime.parse("18:00");
        }
    }

    public static void main(String[] args) {
        launch();
    }

}