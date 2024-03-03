package com.mycompany.clientapp;

import java.io.BufferedReader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.control.MenuButton;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
    Label modLabel;
    TextField module;
    MenuButton day;
    MenuButton startTime;
    MenuButton endTime;
    VBox box;

    @Override
    public void start(Stage stage) throws IOException {       
        responseLabel = new Label("Server Response: ");
        sendButton = new Button("Confirm");

        addButton = new Button("Add");
        removeButton = new Button("Remove");
        displayButton = new Button("Display Schedule");
        powerButton = new Button("Quit");
        
        module = new TextField("");
        modLabel = new Label("Module:");
        day = new MenuButton("Select Day");
        day.getItems().addAll(new MenuItem("Monday"), new MenuItem("Tuesday"), new MenuItem("Wednesday"), new MenuItem("Thursday"), new MenuItem("Friday"), new MenuItem("Saturday"));
        startTime = new MenuButton("Select Start Time");
        times(startTime);
        endTime = new MenuButton("Select End Time");
        times(endTime);
        
        addButton.setPrefWidth(200);
        addButton.setPrefHeight(100);
        removeButton.setPrefWidth(200);
        removeButton.setPrefHeight(100);
        displayButton.setPrefWidth(200);
        displayButton.setPrefHeight(100);
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
                        box.getChildren().addAll(modLabel, module, day, startTime, endTime, sendButton);
                        box.getChildren().removeAll(removeButton,displayButton, powerButton);
                        action = "ADD";
                    }else{
                        style(addButton);
                        box.getChildren().addAll(removeButton, displayButton, powerButton);
                        box.getChildren().removeAll(modLabel, module, day, startTime, endTime, sendButton);
                        action = "";
                    }
            });

        removeButton.setOnAction((ActionEvent r) -> {
                    if(removeButton.getStyle().contains("-fx-background-color: lightblue")){
                        style(removeButton);
                        box.getChildren().addAll(modLabel, module, day, startTime, endTime, sendButton);
                        box.getChildren().removeAll(addButton,displayButton, powerButton);
                        action = "REMOVE";
                    }else{
                        style(removeButton);
                        box.getChildren().addAll(addButton, displayButton, powerButton);
                        box.getChildren().removeAll(modLabel, module, day, startTime, endTime, sendButton);
                        action = "";
                    }
            });

        displayButton.setOnAction((ActionEvent d) -> {
                    if(displayButton.getStyle().contains("-fx-background-color: lightblue")){
                        style(displayButton);
                        box.getChildren().addAll(modLabel, module,sendButton);
                        box.getChildren().removeAll(addButton, removeButton, powerButton);
                        action = "DISPLAY";
                    }else{
                        style(displayButton);
                        box.getChildren().addAll(addButton, removeButton, powerButton);
                        box.getChildren().removeAll(modLabel, module, sendButton);
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

        box = new VBox(5, responseLabel, addButton, removeButton, displayButton, powerButton);
        scene = new Scene(box, 640, 480);
        stage.setScene(scene);
        stage.show();
    }
    
    public void style(Button b){
        if(b.getStyle().contains("-fx-background-color: lightblue")){
            b.setStyle("-fx-background-color: slateblue; -fx-background-radius: 8,7,6; -fx-background-insets: 0,1,2; -fx-text-fill: black; -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1)");
        }else{
            b.setStyle("-fx-background-color: lightblue; -fx-background-radius: 8,7,6; -fx-background-insets: 0,1,2; -fx-text-fill: black; -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1)");
        }
    }
    
    public void times(MenuButton m){
        m.getItems().addAll(new MenuItem("09:00"), new MenuItem("10:00"), new MenuItem("11:00"), new MenuItem("12:00"), new MenuItem("13:00"), new MenuItem("14:00"), new MenuItem("15:00"), new MenuItem("16:00"));
    }

    public static void main(String[] args) {
        launch();
    }

}
