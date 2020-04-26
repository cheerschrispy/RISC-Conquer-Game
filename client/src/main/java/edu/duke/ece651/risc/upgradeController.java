package edu.duke.ece651.risc;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

public class upgradeController {
    //All fields
    private Map<String, Territory> territories;
    private Player player;
    private Scanner sc;
    private ObjectOutputStream os;
    private ObjectInputStream is;
    private Boolean sameAround;
    private savedText savedText;
    private int language;
    private Sender sender;

    private Stage windows;
    @FXML private ImageView map;
    @FXML private TextArea mapInfo;
    @FXML private TextArea playerInfo;


    //all the button
    @FXML private Button done;//return to main page
    @FXML private ComboBox<String> source;
    @FXML private ComboBox<Integer> srcLevel;
    @FXML private ComboBox<Integer> destLevel;
    @FXML private ComboBox<Integer> num;

    @FXML private Button Map1;
    @FXML private Button Map2;
    @FXML private Button Map3;
    @FXML private Button Map4;
    @FXML private Button Map5;
    @FXML private Button Map6;
    @FXML private Button Map7;
    @FXML private Button Map8;
    @FXML private Button Map9;
    @FXML private Button Map10;
    @FXML private Button Map11;
    @FXML private Button Map12;


    public upgradeController(Stage windows,Player player, Map<String, Territory> territories,Scanner sc,
                             ObjectOutputStream os, ObjectInputStream is,Boolean sameAround,savedText savedText,int language, Sender sender){
        this.windows=windows;
        this.player=player;
        this.territories=territories;
        this.sc=sc;
        this.os=os;
        this.is=is;
        this.sameAround=sameAround;
        this.savedText= savedText;
        this.language=language;
        this.sender=sender;
    }

    @FXML
    public void initialize(){
        Image mapImage=new Image("file:./map.jpg");
        map.setImage(mapImage);
        if (this.language == 1){
            TextPrinter t1 = new EngTextPrinter();
            String s = t1.appendPlayerInfo(player.getId(), player.getTechLevel(), player.getFoodResources(), player.getTechResources());
            playerInfo.setText(s);
            mapInfo.clear();
        }
        else{
            TextPrinter t2 = new ChiTextPrinter();
            String s = t2.appendPlayerInfo(player.getId(), player.getTechLevel(), player.getFoodResources(), player.getTechResources());
            playerInfo.setText(s);
            mapInfo.clear();
        }



        ObservableList<Integer> options= FXCollections.observableArrayList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25);
        ObservableList<Integer> levels = FXCollections.observableArrayList(0, 1, 2, 3, 4, 5, 6);
        srcLevel.setItems(levels);
        srcLevel.getSelectionModel().select(0);
        destLevel.setItems(levels);
        destLevel.getSelectionModel().select(0);
        num.setItems(options);
        num.getSelectionModel().select(0);


        Set<Integer> alliances = player.getAlliances();
        alliances.add(player.getId());
        ObservableList<String> s = FXCollections.observableArrayList();

        for(String key: territories.keySet()){
            Territory t = territories.get(key);
            if(alliances.contains(t.getOwner())){
                s.add(key);
            }
        }
        source.setItems(s);
        source.getSelectionModel().select(0);

    }

    public void finishUpgradeAction() throws IOException {
        System.out.println("pop Back main");
        BufferedWriter bw1;
        try {
            bw1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("./history"+player.getId()+".txt"),this.sameAround)));
            bw1.write("U"+"\n");
            bw1.write(source.getValue()+"\n");
            bw1.write(srcLevel.getValue()+"\n");
            bw1.write(num.getValue()+"\n");
            bw1.write(destLevel.getValue()+"\n");
            bw1.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        this.sameAround=true;

        TextPrinter t1 = new ChiTextPrinter();
        String description1 = t1.appendUpgradeHistory(num.getValue(), srcLevel.getValue(), destLevel.getValue());
        this.savedText.addActionC(description1);

        TextPrinter t2 = new EngTextPrinter();
        String description2 = t2.appendUpgradeHistory(num.getValue(), srcLevel.getValue(), destLevel.getValue());
        this.savedText.addActionE(description2);


        FXMLLoader MainRoot =new FXMLLoader(getClass().getResource("Main.fxml"));
        MainRoot.setControllerFactory(c-> new mainController(this.windows,this.player, this.territories,this.sc,this.os,this.is,this.sameAround,
        this. savedText,this.sender,this.language));
        Scene nextScene=new Scene(MainRoot.load());
        this.windows.setScene(nextScene);
        nextScene.getStylesheets().add(
                getClass().getResource("MainStyle.css")
                        .toExternalForm());
        this.windows.show();
    }

    //From Map button 1 to 12
    public void showMapInfo_1(){
        String name = Map1.getText();
        Territory t = territories.get(name);

        if(this.language == 0) {
            TextPrinter t1 = new ChiTextPrinter();
            String description1 = t1.appendMapButton(t);
            this.mapInfo.setText(description1);
        }
        else{
            TextPrinter t2 = new EngTextPrinter();
            String description2 = t2.appendMapButton(t);
            this.mapInfo.setText(description2);
        }
    }

    public void showMapInfo_2(){
        String name = Map2.getText();
        Territory t = territories.get(name);

        if(this.language == 0) {
            TextPrinter t1 = new ChiTextPrinter();
            String description1 = t1.appendMapButton(t);
            this.mapInfo.setText(description1);
        }
        else{
            TextPrinter t2 = new EngTextPrinter();
            String description2 = t2.appendMapButton(t);
            this.mapInfo.setText(description2);
        }
    }

    public void showMapInfo_3(){
        String name = Map3.getText();
        Territory t = territories.get(name);

        if(this.language == 0) {
            TextPrinter t1 = new ChiTextPrinter();
            String description1 = t1.appendMapButton(t);
            this.mapInfo.setText(description1);
        }
        else{
            TextPrinter t2 = new EngTextPrinter();
            String description2 = t2.appendMapButton(t);
            this.mapInfo.setText(description2);
        }
    }

    public void showMapInfo_4(){
        String name = Map4.getText();
        Territory t = territories.get(name);

        if(this.language == 0) {
            TextPrinter t1 = new ChiTextPrinter();
            String description1 = t1.appendMapButton(t);
            this.mapInfo.setText(description1);
        }
        else{
            TextPrinter t2 = new EngTextPrinter();
            String description2 = t2.appendMapButton(t);
            this.mapInfo.setText(description2);
        }
    }

    public void showMapInfo_5(){
        String name = Map5.getText();
        Territory t = territories.get(name);

        if(this.language == 0) {
            TextPrinter t1 = new ChiTextPrinter();
            String description1 = t1.appendMapButton(t);
            this.mapInfo.setText(description1);
        }
        else{
            TextPrinter t2 = new EngTextPrinter();
            String description2 = t2.appendMapButton(t);
            this.mapInfo.setText(description2);
        }
    }

    public void showMapInfo_6(){
        String name = Map6.getText();
        Territory t = territories.get(name);

        if(this.language == 0) {
            TextPrinter t1 = new ChiTextPrinter();
            String description1 = t1.appendMapButton(t);
            this.mapInfo.setText(description1);
        }
        else{
            TextPrinter t2 = new EngTextPrinter();
            String description2 = t2.appendMapButton(t);
            this.mapInfo.setText(description2);
        }
    }

    public void showMapInfo_7(){
        String name = Map7.getText();
        Territory t = territories.get(name);

        if(this.language == 0) {
            TextPrinter t1 = new ChiTextPrinter();
            String description1 = t1.appendMapButton(t);
            this.mapInfo.setText(description1);
        }
        else{
            TextPrinter t2 = new EngTextPrinter();
            String description2 = t2.appendMapButton(t);
            this.mapInfo.setText(description2);
        }
    }

    public void showMapInfo_8(){
        String name = Map8.getText();
        Territory t = territories.get(name);

        if(this.language == 0) {
            TextPrinter t1 = new ChiTextPrinter();
            String description1 = t1.appendMapButton(t);
            this.mapInfo.setText(description1);
        }
        else{
            TextPrinter t2 = new EngTextPrinter();
            String description2 = t2.appendMapButton(t);
            this.mapInfo.setText(description2);
        }
    }

    public void showMapInfo_9(){
        String name = Map9.getText();
        Territory t = territories.get(name);

        if(this.language == 0) {
            TextPrinter t1 = new ChiTextPrinter();
            String description1 = t1.appendMapButton(t);
            this.mapInfo.setText(description1);
        }
        else{
            TextPrinter t2 = new EngTextPrinter();
            String description2 = t2.appendMapButton(t);
            this.mapInfo.setText(description2);
        }
    }

    public void showMapInfo_10(){
        String name = Map10.getText();
        Territory t = territories.get(name);

        if(this.language == 0) {
            TextPrinter t1 = new ChiTextPrinter();
            String description1 = t1.appendMapButton(t);
            this.mapInfo.setText(description1);
        }
        else{
            TextPrinter t2 = new EngTextPrinter();
            String description2 = t2.appendMapButton(t);
            this.mapInfo.setText(description2);
        }
    }

    public void showMapInfo_11(){
        String name = Map11.getText();
        Territory t = territories.get(name);

        if(this.language == 0) {
            TextPrinter t1 = new ChiTextPrinter();
            String description1 = t1.appendMapButton(t);
            this.mapInfo.setText(description1);
        }
        else{
            TextPrinter t2 = new EngTextPrinter();
            String description2 = t2.appendMapButton(t);
            this.mapInfo.setText(description2);
        }
    }

    public void showMapInfo_12(){
        String name = Map12.getText();
        Territory t = territories.get(name);

        if(this.language == 0) {
            TextPrinter t1 = new ChiTextPrinter();
            String description1 = t1.appendMapButton(t);
            this.mapInfo.setText(description1);
        }
        else{
            TextPrinter t2 = new EngTextPrinter();
            String description2 = t2.appendMapButton(t);
            this.mapInfo.setText(description2);
        }
    }
}
