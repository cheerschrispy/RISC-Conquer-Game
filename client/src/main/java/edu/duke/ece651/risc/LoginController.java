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

    //All fields
    private Map<String, Territory> territories;
    private Player player;
    private Scanner sc;
    private Boolean sameAround;
    private ObjectOutputStream os;
    private ObjectInputStream is;
    private savedText savedText;
    //current windows
    private Stage window;
    private ObjectOutputStream os1;
    private ObjectInputStream is1;

    public LoginController(Stage windows, Player player, Map<String, Territory> territories, Scanner sc,
                            ObjectOutputStream os, ObjectInputStream is,Boolean sameAround,savedText savedText) throws IOException {
        this.window = windows;
        this.player = player;
        this.territories = territories;
        this.sc = sc;
        this.is1 = is;
        this.os1 = os;
        //append or override the history file
        this.sameAround = sameAround;
        this.savedText=savedText;
    }

    public void goToMainPage() throws IOException {
        FXMLLoader MainRoot =new FXMLLoader(getClass().getResource("Main.fxml"));
        MainRoot.setControllerFactory(c->{
            return new mainController(this.window,this.player,this.territories,this.sc,this.os1,this.is1,
                    this.savedText);
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
