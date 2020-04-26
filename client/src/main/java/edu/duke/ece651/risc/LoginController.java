package edu.duke.ece651.risc;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class LoginController {

    @FXML private TextField username;
    @FXML private TextField password;
    @FXML private Label prompt;

    //All fields
    private Map<String, Territory> territories;
    private Player player;
    private Scanner sc;
    private ObjectOutputStream os;
    private ObjectInputStream is;
    private savedText savedText;
    //private Receiver receiver;
    private Sender sender;
    //current windows
    private Stage window;
    private ObjectOutputStream os1;
    private ObjectInputStream is1;

    public LoginController(Stage windows, Player player, Map<String, Territory> territories, Scanner sc,
                           ObjectOutputStream os, ObjectInputStream is, savedText savedText,
                           Sender sender) throws IOException {
        this.window = windows;
        this.player = player;
        this.territories = territories;
        this.sc = sc;
        this.is1 = is;
        this.os1 = os;
        //append or override the history file
        this.savedText=savedText;
        //this.receiver = receiver;
        this.sender = sender;
    }


    private boolean authenticate() throws IOException, ClassNotFoundException {
        //set username & password
        Credential credential = new Credential(username.getText(), password.getText());
        //send username & password
        os1.writeObject(credential);
        os1.flush();
        os1.reset();
        //if user exists
        return (boolean) is1.readObject();
    }

    public void goToMainPage() throws IOException, ClassNotFoundException {
        //authenticate

        if(!authenticate()) {
            prompt.setText("Wrong Username/Password! Type Again");
            cancel();
            return;
        }
        //receive info
        this.player = (Player) is1.readObject();
        System.out.println("recevied player");
        //receive map to be completed
        this.territories = (Map<String, Territory>) is1.readObject();
        System.out.println("recevied t");

        //if the users is true, jump tp next page
        FXMLLoader MainRoot =new FXMLLoader(getClass().getResource("Initialize.fxml"));
        MainRoot.setControllerFactory(c->{
            try {
                return new InitializeController(this.window,this.player,this.territories,this.sc,this.os1,this.is1,
                        this.savedText, this.sender);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
        Scene nextScene=new Scene(MainRoot.load());
        nextScene.getStylesheets().add(
                getClass().getResource("MainStyle.css")
                        .toExternalForm());
        this.window.setScene(nextScene);
        this.window.show();
    }


    public void cancel(){
        this.username.clear();
        this.password.clear();
    }
}
