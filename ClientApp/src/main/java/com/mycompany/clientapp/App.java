package com.mycompany.clientapp;

import java.io.BufferedReader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
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
  TextField actionField;
  TextField descriptionField;
  Button sendButton;

    @Override
    public void start(Stage stage) throws IOException {
        
        responseLabel = new Label("Last Response: ");
        actionField = new TextField("");
        descriptionField = new TextField("");
        sendButton = new Button("Send");
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
                
                action = actionField.getText();
                description = descriptionField.getText();
                
                // Message
                output.println(action);
                output.println(description);
                
                // Response
                String response;
                response = input.readLine();
                responseLabel.setText("Last Operation: " + response);
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        });
        VBox box = new VBox(responseLabel, actionField, descriptionField, sendButton);
        
        scene = new Scene(box, 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}