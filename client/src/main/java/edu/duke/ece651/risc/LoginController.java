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
import java.net.Socket;
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
    private ObjectOutputStream cos;
    private ObjectInputStream cis;
    private int gameId;
    private Receiver receiver;



    public LoginController(Stage windows, Player player, Map<String, Territory> territories, Scanner sc,
                           ObjectOutputStream os, ObjectInputStream is, savedText savedText,
                           Sender sender,int gameId,ObjectInputStream cis,ObjectOutputStream cos,Receiver receiver) throws IOException {
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
        this.gameId= gameId;
        this.cis= cis;
        this.cos= cos;
        this.receiver=receiver;
    }


    private boolean authenticate() throws IOException, ClassNotFoundException {
        //set username & password
        System.out.println(username.getText()+ password.getText());
        Credential credential = new Credential(username.getText(), password.getText());
        //send username & password
        os1.writeObject(credential);
        os1.flush();
        os1.reset();
        //valid or not
        if (!(boolean) is1.readObject()){
            endPop("Wrong password");
            //todo:disconnect
            this.window.close();

        }
        return (boolean) is1.readObject();
    }

    public void goToMainPage() throws IOException, ClassNotFoundException {
        //old player
        if(authenticate()) {
            InitializeGame();
            System.out.println( "ready to go mainController");

            FXMLLoader MainRoot =new FXMLLoader(getClass().getResource("Main.fxml"));
            MainRoot.setControllerFactory(c->{
                return new mainController(this.window,this.player,this.territories,this.sc,this.os1,this.is1,
                        this.savedText, this.sender);
            });
            Scene nextScene=new Scene(MainRoot.load());
            nextScene.getStylesheets().add(
                    getClass().getResource("MainStyle.css")
                            .toExternalForm());
            this.window.setScene(nextScene);
            this.window.show();
        }
        else {
            //new player
            InitializeGame();
            //if the users is true, jump tp next page
            FXMLLoader MainRoot = new FXMLLoader(getClass().getResource("Initialize.fxml"));
            MainRoot.setControllerFactory(c -> {
                try {
                    return new InitializeController(this.window, this.player, this.territories, this.sc, this.os1, this.is1,
                            this.savedText, this.sender);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            });
            Scene nextScene = new Scene(MainRoot.load());
            nextScene.getStylesheets().add(
                    getClass().getResource("MainStyle.css")
                            .toExternalForm());
            this.window.setScene(nextScene);
            this.window.show();
        }
    }


    public void cancel(){
        this.username.clear();
        this.password.clear();
    }

    private void InitializeGame() throws IOException, ClassNotFoundException {

        //receive info
        this.player = (Player) is1.readObject();
        //receive map to be completed
        this.territories = (Map<String, Territory>) is1.readObject();


        //connect to Chat Server
        Socket chattingSocket = new Socket("localhost", 7999 - gameId);
        System.out.println("Connected to Chat Server");
        this.cos = new ObjectOutputStream(chattingSocket.getOutputStream());
        this.cis = new ObjectInputStream(chattingSocket.getInputStream());
        this.receiver = new Receiver(cis, player.getId(), savedText);
        receiver.start();
        this.sender = new Sender(cos, player.getId());

    }
    private void endPop(String information){
        Stage window = new Stage();
        //int status=0;

        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        //window.setTitle("RISC");
        window.setMinWidth(250);

        Label label = new Label();
        label.setText(information);

        Button quitButton =new Button(("Quit"));
        quitButton.setOnAction(e -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, quitButton);
        layout.setAlignment(Pos.TOP_CENTER);


        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

}
