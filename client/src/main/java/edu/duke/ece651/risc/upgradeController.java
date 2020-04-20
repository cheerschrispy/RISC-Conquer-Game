package edu.duke.ece651.risc;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
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

    private Stage windows;

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
                             ObjectOutputStream os, ObjectInputStream is,Boolean sameAround,savedText savedText){
        this.windows=windows;
        this.player=player;
        this.territories=territories;
        this.sc=sc;
        this.os=os;
        this.is=is;
        this.sameAround=sameAround;
        this.savedText= savedText;
    }

    @FXML
    public void initialize(){
        String pInfo = "Hi, welcome player " + player.getId() + ".\r\n" +
                "Now you are in TECHNIQUE level " + player.getTechLevel() + ".\r\n" +
                "You have " + player.getFoodResources() + " food resources.\r\n" +
                "You have " + player.getTechResources() + " tech resources.\r\n";
        playerInfo.setText(pInfo);

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
        //write into history fields
        StringBuilder description=new StringBuilder();
        description.append("Upgrade: ").append(num.getValue()+" Lv"+srcLevel.getValue()+" Soldiers");
        description.append(" to Lv"+destLevel.getValue()+"\n");
        this.savedText.addAction(String.valueOf(description));


        FXMLLoader MainRoot =new FXMLLoader(getClass().getResource("Main.fxml"));
        MainRoot.setControllerFactory(c->{
            try {
                return new mainController(this.windows,this.player, this.territories,this.sc,this.os,this.is,this.sameAround,
                this. savedText);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
        Scene nextScene=new Scene(MainRoot.load());
        this.windows.setScene(nextScene);
        this.windows.show();
    }

    //From Map button 1 to 12
    public void showMapInfo_1(){
        //todo: find the territory in map
        String name = Map1.getText();
        Territory t = territories.get(name);
        StringBuilder s = new StringBuilder();
        s.append("Territory Information:\r\n\r\n").append("This is territory ").append(t.getName())
                .append(", owned by ").append(t.getOwner()).append(" now.\r\n");
        for(int i = 0; i < 7; i++){
            s.append("It has   ").append(t.getSoldierNumOfLevel(i)).append("   level-").append(i).append(" soldiers.\r\n");
        }
        s.append("\r\n");
        HashMap<Integer, ArrayList<Unit>> alliances = t.getAllies();
        for(int key : alliances.keySet()){
            ArrayList<Unit> ally = alliances.get(key);
            s.append("It has ally player ").append(key).append(" in this territory");
            for(int i = 0; i < 7; i++){
                int num = t.getAllyNumOfLevel(i, key);
                if(num != 0){
                    s.append("It has   ").append(num).append("   level-").append(i).append(" soldiers.\r\n");
                }
            }
            s.append("\r\n");
        }
        this.mapInfo.setText(String.valueOf(s));
    }

    public void showMapInfo_2(){
        //todo: find the territory in map
        String name = Map2.getText();
        Territory t = territories.get(name);
        StringBuilder s = new StringBuilder();
        s.append("Territory Information:\r\n\r\n").append("This is territory ").append(t.getName())
                .append(", owned by ").append(t.getOwner()).append(" now.\r\n");
        for(int i = 0; i < 7; i++){
            s.append("It has   ").append(t.getSoldierNumOfLevel(i)).append("   level-").append(i).append(" soldiers.\r\n");
        }
        s.append("\r\n");
        HashMap<Integer, ArrayList<Unit>> alliances = t.getAllies();
        for(int key : alliances.keySet()){
            ArrayList<Unit> ally = alliances.get(key);
            s.append("It has ally player ").append(key).append(" in this territory");
            for(int i = 0; i < 7; i++){
                int num = t.getAllyNumOfLevel(i, key);
                if(num != 0){
                    s.append("It has   ").append(num).append("   level-").append(i).append(" soldiers.\r\n");
                }
            }
            s.append("\r\n");
        }
        this.mapInfo.setText(String.valueOf(s));
    }

    public void showMapInfo_3(){
        //todo: find the territory in map
        String name = Map3.getText();
        Territory t = territories.get(name);
        StringBuilder s = new StringBuilder();
        s.append("Territory Information:\r\n\r\n").append("This is territory ").append(t.getName())
                .append(", owned by ").append(t.getOwner()).append(" now.\r\n");
        for(int i = 0; i < 7; i++){
            s.append("It has   ").append(t.getSoldierNumOfLevel(i)).append("   level-").append(i).append(" soldiers.\r\n");
        }
        s.append("\r\n");
        HashMap<Integer, ArrayList<Unit>> alliances = t.getAllies();
        for(int key : alliances.keySet()){
            ArrayList<Unit> ally = alliances.get(key);
            s.append("It has ally player ").append(key).append(" in this territory");
            for(int i = 0; i < 7; i++){
                int num = t.getAllyNumOfLevel(i, key);
                if(num != 0){
                    s.append("It has   ").append(num).append("   level-").append(i).append(" soldiers.\r\n");
                }
            }
            s.append("\r\n");
        }
        this.mapInfo.setText(String.valueOf(s));
    }

    public void showMapInfo_4(){
        //todo: find the territory in map
        String name = Map4.getText();
        Territory t = territories.get(name);
        StringBuilder s = new StringBuilder();
        s.append("Territory Information:\r\n\r\n").append("This is territory ").append(t.getName())
                .append(", owned by ").append(t.getOwner()).append(" now.\r\n");
        for(int i = 0; i < 7; i++){
            s.append("It has   ").append(t.getSoldierNumOfLevel(i)).append("   level-").append(i).append(" soldiers.\r\n");
        }
        s.append("\r\n");
        HashMap<Integer, ArrayList<Unit>> alliances = t.getAllies();
        for(int key : alliances.keySet()){
            ArrayList<Unit> ally = alliances.get(key);
            s.append("It has ally player ").append(key).append(" in this territory");
            for(int i = 0; i < 7; i++){
                int num = t.getAllyNumOfLevel(i, key);
                if(num != 0){
                    s.append("It has   ").append(num).append("   level-").append(i).append(" soldiers.\r\n");
                }
            }
            s.append("\r\n");
        }
        this.mapInfo.setText(String.valueOf(s));
    }

    public void showMapInfo_5(){
        //todo: find the territory in map
        String name = Map5.getText();
        Territory t = territories.get(name);
        StringBuilder s = new StringBuilder();
        s.append("Territory Information:\r\n\r\n").append("This is territory ").append(t.getName())
                .append(", owned by ").append(t.getOwner()).append(" now.\r\n");
        for(int i = 0; i < 7; i++){
            s.append("It has   ").append(t.getSoldierNumOfLevel(i)).append("   level-").append(i).append(" soldiers.\r\n");
        }
        s.append("\r\n");
        HashMap<Integer, ArrayList<Unit>> alliances = t.getAllies();
        for(int key : alliances.keySet()){
            ArrayList<Unit> ally = alliances.get(key);
            s.append("It has ally player ").append(key).append(" in this territory");
            for(int i = 0; i < 7; i++){
                int num = t.getAllyNumOfLevel(i, key);
                if(num != 0){
                    s.append("It has   ").append(num).append("   level-").append(i).append(" soldiers.\r\n");
                }
            }
            s.append("\r\n");
        }
        this.mapInfo.setText(String.valueOf(s));
    }

    public void showMapInfo_6(){
        //todo: find the territory in map
        String name = Map6.getText();
        Territory t = territories.get(name);
        StringBuilder s = new StringBuilder();
        s.append("Territory Information:\r\n\r\n").append("This is territory ").append(t.getName())
                .append(", owned by ").append(t.getOwner()).append(" now.\r\n");
        for(int i = 0; i < 7; i++){
            s.append("It has   ").append(t.getSoldierNumOfLevel(i)).append("   level-").append(i).append(" soldiers.\r\n");
        }
        s.append("\r\n");
        HashMap<Integer, ArrayList<Unit>> alliances = t.getAllies();
        for(int key : alliances.keySet()){
            ArrayList<Unit> ally = alliances.get(key);
            s.append("It has ally player ").append(key).append(" in this territory");
            for(int i = 0; i < 7; i++){
                int num = t.getAllyNumOfLevel(i, key);
                if(num != 0){
                    s.append("It has   ").append(num).append("   level-").append(i).append(" soldiers.\r\n");
                }
            }
            s.append("\r\n");
        }
        this.mapInfo.setText(String.valueOf(s));
    }

    public void showMapInfo_7(){
        //todo: find the territory in map
        String name = Map7.getText();
        Territory t = territories.get(name);
        StringBuilder s = new StringBuilder();
        s.append("Territory Information:\r\n\r\n").append("This is territory ").append(t.getName())
                .append(", owned by ").append(t.getOwner()).append(" now.\r\n");
        for(int i = 0; i < 7; i++){
            s.append("It has   ").append(t.getSoldierNumOfLevel(i)).append("   level-").append(i).append(" soldiers.\r\n");
        }
        s.append("\r\n");
        HashMap<Integer, ArrayList<Unit>> alliances = t.getAllies();
        for(int key : alliances.keySet()){
            ArrayList<Unit> ally = alliances.get(key);
            s.append("It has ally player ").append(key).append(" in this territory");
            for(int i = 0; i < 7; i++){
                int num = t.getAllyNumOfLevel(i, key);
                if(num != 0){
                    s.append("It has   ").append(num).append("   level-").append(i).append(" soldiers.\r\n");
                }
            }
            s.append("\r\n");
        }
        this.mapInfo.setText(String.valueOf(s));
    }

    public void showMapInfo_8(){
        //todo: find the territory in map
        String name = Map8.getText();
        Territory t = territories.get(name);
        StringBuilder s = new StringBuilder();
        s.append("Territory Information:\r\n\r\n").append("This is territory ").append(t.getName())
                .append(", owned by ").append(t.getOwner()).append(" now.\r\n");
        for(int i = 0; i < 7; i++){
            s.append("It has   ").append(t.getSoldierNumOfLevel(i)).append("   level-").append(i).append(" soldiers.\r\n");
        }
        s.append("\r\n");
        HashMap<Integer, ArrayList<Unit>> alliances = t.getAllies();
        for(int key : alliances.keySet()){
            ArrayList<Unit> ally = alliances.get(key);
            s.append("It has ally player ").append(key).append(" in this territory");
            for(int i = 0; i < 7; i++){
                int num = t.getAllyNumOfLevel(i, key);
                if(num != 0){
                    s.append("It has   ").append(num).append("   level-").append(i).append(" soldiers.\r\n");
                }
            }
            s.append("\r\n");
        }
        this.mapInfo.setText(String.valueOf(s));
    }

    public void showMapInfo_9(){
        //todo: find the territory in map
        String name = Map9.getText();
        Territory t = territories.get(name);
        StringBuilder s = new StringBuilder();
        s.append("Territory Information:\r\n\r\n").append("This is territory ").append(t.getName())
                .append(", owned by ").append(t.getOwner()).append(" now.\r\n");
        for(int i = 0; i < 7; i++){
            s.append("It has   ").append(t.getSoldierNumOfLevel(i)).append("   level-").append(i).append(" soldiers.\r\n");
        }
        s.append("\r\n");
        HashMap<Integer, ArrayList<Unit>> alliances = t.getAllies();
        for(int key : alliances.keySet()){
            ArrayList<Unit> ally = alliances.get(key);
            s.append("It has ally player ").append(key).append(" in this territory");
            for(int i = 0; i < 7; i++){
                int num = t.getAllyNumOfLevel(i, key);
                if(num != 0){
                    s.append("It has   ").append(num).append("   level-").append(i).append(" soldiers.\r\n");
                }
            }
            s.append("\r\n");
        }
        this.mapInfo.setText(String.valueOf(s));
    }

    public void showMapInfo_10(){
        //todo: find the territory in map
        String name = Map10.getText();
        Territory t = territories.get(name);
        StringBuilder s = new StringBuilder();
        s.append("Territory Information:\r\n\r\n").append("This is territory ").append(t.getName())
                .append(", owned by ").append(t.getOwner()).append(" now.\r\n");
        for(int i = 0; i < 7; i++){
            s.append("It has   ").append(t.getSoldierNumOfLevel(i)).append("   level-").append(i).append(" soldiers.\r\n");
        }
        s.append("\r\n");
        HashMap<Integer, ArrayList<Unit>> alliances = t.getAllies();
        for(int key : alliances.keySet()){
            ArrayList<Unit> ally = alliances.get(key);
            s.append("It has ally player ").append(key).append(" in this territory");
            for(int i = 0; i < 7; i++){
                int num = t.getAllyNumOfLevel(i, key);
                if(num != 0){
                    s.append("It has   ").append(num).append("   level-").append(i).append(" soldiers.\r\n");
                }
            }
            s.append("\r\n");
        }
        this.mapInfo.setText(String.valueOf(s));
    }

    public void showMapInfo_11(){
        //todo: find the territory in map
        String name = Map11.getText();
        Territory t = territories.get(name);
        StringBuilder s = new StringBuilder();
        s.append("Territory Information:\r\n\r\n").append("This is territory ").append(t.getName())
                .append(", owned by ").append(t.getOwner()).append(" now.\r\n");
        for(int i = 0; i < 7; i++){
            s.append("It has   ").append(t.getSoldierNumOfLevel(i)).append("   level-").append(i).append(" soldiers.\r\n");
        }
        s.append("\r\n");
        HashMap<Integer, ArrayList<Unit>> alliances = t.getAllies();
        for(int key : alliances.keySet()){
            ArrayList<Unit> ally = alliances.get(key);
            s.append("It has ally player ").append(key).append(" in this territory");
            for(int i = 0; i < 7; i++){
                int num = t.getAllyNumOfLevel(i, key);
                if(num != 0){
                    s.append("It has   ").append(num).append("   level-").append(i).append(" soldiers.\r\n");
                }
            }
            s.append("\r\n");
        }
        this.mapInfo.setText(String.valueOf(s));
    }

    public void showMapInfo_12(){
        //todo: find the territory in map
        String name = Map12.getText();
        Territory t = territories.get(name);
        StringBuilder s = new StringBuilder();
        s.append("Territory Information:\r\n\r\n").append("This is territory ").append(t.getName())
                .append(", owned by ").append(t.getOwner()).append(" now.\r\n");
        for(int i = 0; i < 7; i++){
            s.append("It has   ").append(t.getSoldierNumOfLevel(i)).append("   level-").append(i).append(" soldiers.\r\n");
        }
        s.append("\r\n");
        HashMap<Integer, ArrayList<Unit>> alliances = t.getAllies();
        for(int key : alliances.keySet()){
            ArrayList<Unit> ally = alliances.get(key);
            s.append("It has ally player ").append(key).append(" in this territory");
            for(int i = 0; i < 7; i++){
                int num = t.getAllyNumOfLevel(i, key);
                if(num != 0){
                    s.append("It has   ").append(num).append("   level-").append(i).append(" soldiers.\r\n");
                }
            }
            s.append("\r\n");
        }
        this.mapInfo.setText(String.valueOf(s));
    }

}
