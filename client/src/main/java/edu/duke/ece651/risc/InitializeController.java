package edu.duke.ece651.risc;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class InitializeController {
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

    private int playerNum;

    //components
    @FXML private ComboBox<Integer> t1;
    @FXML private ComboBox<Integer> t2;
    @FXML private ComboBox<Integer> t3;
    @FXML private ComboBox<Integer> t4;
    @FXML private ComboBox<Integer> t5;
    @FXML private ComboBox<Integer> t6;

    @FXML private Label initialInfo;
    @FXML private Label prompt;
    @FXML private ImageView mapInfo;


    public InitializeController(Stage windows, Player player, Map<String, Territory> territories, Scanner sc,
                           ObjectOutputStream os, ObjectInputStream is, savedText savedText, Sender sender) throws IOException {
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

    @FXML
    public void initialize(){
        String TerritoryOwned="";
        for(Territory t :this.territories.values()){
            if(this.player.getId()==t.getOwner()){
                TerritoryOwned+=t.getName();
                TerritoryOwned+=" ";
            }
        }
        TerritoryOwned+="\n";
        TerritoryOwned+="You have 10 soldiers to assign";
        initialInfo.setText("The Territories You Owned Are："+TerritoryOwned);

        //initialize the combox box


        ObservableList<Integer> options= FXCollections.observableArrayList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        t1.setItems(options);
        t1.getSelectionModel().select(0);
        t2.setItems(options);
        t2.getSelectionModel().select(0);
        t3.setItems(options);
        t3.getSelectionModel().select(0);
        t4.setItems(options);
        t4.getSelectionModel().select(0);
        t5.setItems(options);
        t5.getSelectionModel().select(0);
        t6.setItems(options);
        t6.getSelectionModel().select(0);


        if(this.playerNum==3){
            t5.setDisable(true);
            t6.setDisable(true);
        }
        if(this.playerNum==4){
            t4.setDisable(true);
            t5.setDisable(true);
            t6.setDisable(true);
        }

        Image image2 = new Image("file:./map.jpg");
        mapInfo.setImage(image2);
    }


    public void done() throws IOException, ClassNotFoundException {
        //Validate total num
        int[] numList=new int[6];
        int num1=t1.getValue();
        numList[0]=num1;
        int num2=t2.getValue();
        numList[1]=num2;
        int num3=t3.getValue();
        numList[2]=num3;
        int num4=t4.getValue();
        numList[3]=num4;
        int num5=t5.getValue();
        numList[4]=num5;
        int num6=t6.getValue();
        numList[5]=num6;

        if(num1 + num2 + num3 + num4 + num5 + num6!=10){
            prompt.setText("Invalid Input. Choose Again");
            return;
        }
        //socket
        int index=0;
        HashMap<String, Integer> init_info = new HashMap<>();
        for(Territory t :this.territories.values()){
            if(this.player.getId()==t.getOwner()){
                init_info.put(t.getName(),numList[index]);
                index++;
            }
        }
        //send it to server

        os1.writeObject(init_info);
        os1.flush();
        os1.reset();

        this.territories = (Map<String, Territory>) is1.readObject();

        //jump to next
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


}
