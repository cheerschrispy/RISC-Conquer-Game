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

//for attack
public class MoveController {
    //All fields
    private Map<String, Territory> territories;
    private Player player;
    private Scanner sc;
    private  Boolean sameAround;
    private ObjectOutputStream os;
    private ObjectInputStream is;
    private savedText savedText;
    private int language;
    private Sender sender;

    @FXML private TextArea mapInfo;
    @FXML private TextArea playerInfo;
    @FXML private ImageView map;
    //@FXML private TextArea playerInfo;
    //all the button
    @FXML private Button done;//return to main page
    @FXML private ComboBox<String> Src;
    @FXML private ComboBox<String> Dest;
    @FXML private ComboBox<Integer> l0;
    @FXML private ComboBox<Integer> l1;
    @FXML private ComboBox<Integer> l2;
    @FXML private ComboBox<Integer> l3;
    @FXML private ComboBox<Integer> l4;
    @FXML private ComboBox<Integer> l5;
    @FXML private ComboBox<Integer> l6;

    @FXML private ImageView lv0Soldier;
    @FXML private ImageView lv1Soldier;
    @FXML private ImageView lv2Soldier;
    @FXML private ImageView lv3Soldier;
    @FXML private ImageView lv4Soldier;
    @FXML private ImageView lv5Soldier;
    @FXML private ImageView lv6Soldier;

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

    @FXML
    public void initialize(){
        Image mapImage=new Image("file:./map.jpg");
        map.setImage(mapImage);

        ObservableList<Integer> options=FXCollections.observableArrayList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25);
        l0.setItems(options);
        l0.getSelectionModel().select(0);
        l1.setItems(options);
        l1.getSelectionModel().select(0);
        l2.setItems(options);
        l2.getSelectionModel().select(0);
        l3.setItems(options);
        l3.getSelectionModel().select(0);
        l4.setItems(options);
        l4.getSelectionModel().select(0);
        l5.setItems(options);
        l5.getSelectionModel().select(0);
        l6.setItems(options);
        l6.getSelectionModel().select(0);

        Image image0 = new Image("file:./WechatIMG1711.jpeg");
        lv0Soldier.setImage(image0);

        Image image1 = new Image("file:./WechatIMG1709.jpeg");
        lv1Soldier.setImage(image1);

        Image image2 = new Image("file:./WechatIMG1706.jpeg");
        lv2Soldier.setImage(image2);

        Image image3 = new Image("file:./WechatIMG1705.jpeg");
        lv3Soldier.setImage(image3);

        Image image4 = new Image("file:./WechatIMG1710.jpeg");
        lv4Soldier.setImage(image4);

        Image image5 = new Image("file:./WechatIMG1707.jpeg");
        lv5Soldier.setImage(image5);

        Image image6 = new Image("file:./WechatIMG1708.jpeg");
        lv6Soldier.setImage(image6);


        Set<Integer> alliances = player.getAlliances();
        ObservableList<String> s = FXCollections.observableArrayList();

        for(String key: territories.keySet()){
            Territory t = territories.get(key);
            if(alliances.contains(t.getOwner()) || t.getOwner() == player.getId()){
                s.add(key);
            }
        }
        Src.setItems(s);
        Src.getSelectionModel().select(0);
        Dest.setItems(s);
        Dest.getSelectionModel().select(0);


        if (this.language == 1){
            TextPrinter t1 = new EngTextPrinter();
            String info = t1.appendPlayerInfo(player, player.getTechLevel(), player.getFoodResources(), player.getTechResources());
            playerInfo.setText(info);
            mapInfo.clear();
        }
        else{
            TextPrinter t2 = new ChiTextPrinter();
            String info = t2.appendPlayerInfo(player, player.getTechLevel(), player.getFoodResources(), player.getTechResources());
            playerInfo.setText(info);
            mapInfo.clear();

        }


    }

    //current windows
    private Stage windows;

    public MoveController(Stage windows,Player player, Map<String, Territory> territories,Scanner sc,
                          ObjectOutputStream os, ObjectInputStream is,Boolean sameAround,savedText savedText,int language,Sender sender) {
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

    public void doneAction() throws IOException {
        //write the current action into text file
        BufferedWriter bw1;
        try {
            bw1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("./history"+player.getId()+".txt"),this.sameAround)));
            bw1.write("M"+"\n");
            bw1.write(Src.getValue()+"\n");
            bw1.write(l0.getValue()+"\n");
            bw1.write(l1.getValue()+"\n");
            bw1.write(l2.getValue()+"\n");
            bw1.write(l3.getValue()+"\n");
            bw1.write(l4.getValue()+"\n");
            bw1.write(l5.getValue()+"\n");
            bw1.write(l6.getValue()+"\n");
            bw1.write(Dest.getValue()+"\n");
            bw1.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        //write into history fields
        List<Integer> soldiers = new ArrayList<>();
        soldiers.add(l0.getValue());
        soldiers.add(l1.getValue());
        soldiers.add(l2.getValue());
        soldiers.add(l3.getValue());
        soldiers.add(l4.getValue());
        soldiers.add(l5.getValue());
        soldiers.add(l6.getValue());

        TextPrinter t1 = new ChiTextPrinter();
        String description1 = t1.appendMoveHistory(Src.getValue(), Dest.getValue(), soldiers);
        this.savedText.addActionC(description1);

        TextPrinter t2 = new EngTextPrinter();
        String description2 = t2.appendMoveHistory(Src.getValue(), Dest.getValue(), soldiers);
        this.savedText.addActionE(description2);


        //------------------
        this.sameAround=true;
        //Pop back to mainScene
        System.out.println("pop Back");
        FXMLLoader MainRoot =new FXMLLoader(getClass().getResource("Main.fxml"));
        MainRoot.setControllerFactory(c-> new mainController(this.windows,this.player,this.territories,this.sc,this.os,this.is,this.sameAround,
                this.savedText,this.sender,this.language));
        Scene nextScene=new Scene(MainRoot.load());
        nextScene.getStylesheets().add(
                getClass().getResource("MainStyle.css")
                        .toExternalForm());
        this.windows.setScene(nextScene);
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
