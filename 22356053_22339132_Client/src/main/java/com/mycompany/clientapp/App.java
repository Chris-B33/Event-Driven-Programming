package com.mycompany.clientapp;

import java.io.BufferedReader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.MenuButton;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

/**
 * JavaFX App
 */
public class App extends Application {
    private static final ArrayList dayNames = new ArrayList<>(
        Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    );

    static Socket link;
    static InetAddress host;
    static final int PORT = 1234;

    private static BufferedReader input;
    private static PrintWriter output;

    private static String action;
    private static String description;
    private static String response;

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
    int dayNum;
    TimePicker startTime;
    TimePicker endTime;
    VBox box;
    HBox times;
    

    @Override
    public void start(Stage stage) throws IOException {   
        try {
            host = InetAddress.getLocalHost();
        } catch(UnknownHostException e) 
        {
            System.out.println("Host ID not found!");
            System.exit(1);
        }
        
        link = new Socket(host,PORT);
        input = new BufferedReader(new InputStreamReader(link.getInputStream()));
        output = new PrintWriter(link.getOutputStream(), true);
        
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
        
        day = new MenuButton("Monday");
        days(day);
        
        startTime = new TimePicker(9, 0, "Start");
        endTime = new TimePicker(10, 0, "End");
        times = new HBox(100, startTime, endTime);
        
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
                        box.getChildren().addAll(courseLabel, course, modLabel, module, roomLabel, room, day, times, sendButton);
                        box.getChildren().removeAll(removeButton,displayButton, powerButton);
                        action = "ADD";
                    }else{
                        style(addButton);
                        box.getChildren().addAll(removeButton, displayButton, powerButton);
                        box.getChildren().removeAll(courseLabel, course, modLabel, module, roomLabel, room, day, times, sendButton);
                        action = "";
                    }
            });

        removeButton.setOnAction((ActionEvent r) -> {
                    if(removeButton.getStyle().contains("-fx-background-color: lightblue")){
                        style(removeButton);
                        box.getChildren().addAll(courseLabel, course, modLabel, module, roomLabel, room, day, times, sendButton);
                        box.getChildren().removeAll(addButton,displayButton, powerButton);
                        action = "REMOVE";
                    }else{
                        style(removeButton);
                        box.getChildren().addAll(addButton, displayButton, powerButton);
                        box.getChildren().removeAll(courseLabel, course, modLabel, module, roomLabel, room, day, times, sendButton);
                        action = "";
                    }
            });

        displayButton.setOnAction((ActionEvent d) -> {
                    if(displayButton.getStyle().contains("-fx-background-color: lightblue")){
                        style(displayButton);
                        box.getChildren().addAll(courseLabel, course, sendButton);
                        box.getChildren().removeAll(addButton, removeButton, powerButton);
                        action = "DISPLAY";
                    }else{
                        style(displayButton);
                        box.getChildren().addAll(addButton, removeButton, powerButton);
                        box.getChildren().removeAll(courseLabel, course, sendButton);
                        action = "";
                    }
            });

        powerButton.setOnAction((ActionEvent q) -> {
                    if(powerButton.getStyle().contains("-fx-background-color: red")){
                        powerButton.setStyle("-fx-background-color: darkred; -fx-background-radius: 30; -fx-background-insets: 0; -fx-text-fill: White");
                        box.getChildren().add(sendButton);
                        box.getChildren().removeAll(addButton, removeButton,displayButton);
                        action = "STOP";
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
                // Message
                description = String.format("%s %s %s %s %s %s", 
                        course.getText(), module.getText(), room.getText(), dayNum, startTime.getTime(), endTime.getTime()
                );

                output.println(action);
                output.println(description);

                // Response
                response = input.readLine();
                responseLabel.setText("Server Response: " + response);

                // Handling Responsesif (response.split(" ")[0].equals("DISPLAY")) { fancyTimetable(); }
                if (response.equals("TERMINATE")) { System.exit(0); }
                if (response.split(" ")[0].equals("DISPLAY")) { fancyTimetable(); }
            }
            catch(IOException e) {}
        });
           
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

    public void days(MenuButton d){
        MenuItem m1 = new MenuItem("Monday");
        MenuItem m2 = new MenuItem("Tuesday");
        MenuItem m3 = new MenuItem("Wednesday");
        MenuItem m4 = new MenuItem("Thursday");
        MenuItem m5 = new MenuItem("Friday");
        MenuItem m6 = new MenuItem("Saturday");
        MenuItem m7 = new MenuItem("Sunday");

        d.getItems().addAll(m1, m2, m3, m4, m5, m6, m7);

        EventHandler<ActionEvent> pickDay = (ActionEvent day1) -> {
            d.setText(((MenuItem) day1.getSource()).getText());
            dayNum = dayNames.indexOf(d.getText());
        };

        m1.setOnAction(pickDay);
        m2.setOnAction(pickDay);
        m3.setOnAction(pickDay);
        m4.setOnAction(pickDay);
        m5.setOnAction(pickDay);
        m6.setOnAction(pickDay);
        m7.setOnAction(pickDay);
    }
    
    private void fancyTimetable() {
        String[] conkedTimetable = response.split(" ");
        for (int i=1; i<conkedTimetable.length; i += 4) {
            
            String module = conkedTimetable[i];
            String room = conkedTimetable[i+1];
            String start = conkedTimetable[i+2];
            String end = conkedTimetable[i+3];
        }
    }

    public static void main(String[] args) {
        launch();
    }

}