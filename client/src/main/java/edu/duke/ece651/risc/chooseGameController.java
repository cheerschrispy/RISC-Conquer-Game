package edu.duke.ece651.risc;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;

public class chooseGameController {//All fields
    private Map<String, Territory> territories;
    private Player player;
    private Scanner sc;

    private ObjectOutputStream os;
    private ObjectInputStream is;
    private savedText savedText;
    private Receiver receiver;
    private Sender sender;
    //current windows
    private Stage window;
    private ObjectOutputStream os1;//null
    private ObjectInputStream is1;//null

    @FXML
    private ComboBox<Integer> gameID;

    @FXML
    public void initialize(){
        ObservableList<Integer> options= FXCollections.observableArrayList(0, 1);
        gameID.setItems(options);
        gameID.getSelectionModel().select(0);
    }

    public chooseGameController(Stage windows, Player player, Map<String, Territory> territories, Scanner sc,
                           ObjectOutputStream os, ObjectInputStream is, savedText savedText, Receiver receiver, Sender sender) throws IOException {
        this.window = windows;
        this.player = player;
        this.territories = territories;
        this.sc = sc;
        this.is1 = is;
        this.os1 = os;
        //append or override the history file

        this.savedText=savedText;
        this.receiver = receiver;
        this.sender = sender;
    }

    public void done() throws IOException {
        int gameId=gameID.getValue();
        //socket

        /*
        Socket socket = new Socket("localhost", 8000 + gameId);
        this.os1 = new ObjectOutputStream(socket.getOutputStream());
        this.is1 = new ObjectInputStream(socket.getInputStream());
        */

        //go to next
        FXMLLoader MainRoot =new FXMLLoader(getClass().getResource("Login.fxml"));
        MainRoot.setControllerFactory(c->{
            try {
                return new LoginController(this.window,this.player,this.territories,this.sc,this.os1,this.is1,
                        this.savedText, this.sender);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
        Scene nextScene=new Scene(MainRoot.load());
        nextScene.getStylesheets().add(
                getClass().getResource("LoginStyle.css")
                        .toExternalForm());
        this.window.setScene(nextScene);
        this.window.show();
    }
}
