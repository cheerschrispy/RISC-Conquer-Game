package edu.duke.ece651.risc;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

//for attack
public class mainController {

    //----------------
    //--private fields--
    //----------------
    private int status;
    private int playerNum = 3;
    //private Map<String, Territory> territories;
    private Player player;
    private Map<String, Territory> territories;
    private Scanner sc;
    private ObjectOutputStream os1;
    private ObjectInputStream is1;
    private savedText savedText;


    private boolean sameAround;


    @FXML private Label gameStatus;
    @FXML private Button attack;
    @FXML private Button upgrade;
    @FXML private Button move;
    @FXML private Button commit;
    @FXML private Button alliance;

    @FXML private TextArea mapInfo;
    @FXML private TextArea playerInfo;
    @FXML private TextArea history;

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
    //current windows
    private Stage windows;


    //------------------
    //----Constructor---
    //------------------
    //from mainWindow to subWindows
    public mainController(Stage windows, Player player, Map<String, Territory> territories, Scanner sc, ObjectOutputStream os,
                          ObjectInputStream is,savedText savedText) throws IOException {
        this.windows=windows;
        this.player=player;
        this.territories=territories;
        this.sc=sc;
        this.is1=is;
        this.os1=os;
        //append or override the history file
        this.sameAround=false;
        this.savedText=savedText;

    }
    //from subWindows to mainWindow
    public mainController(Stage windows, Player player, Map<String, Territory> territories, Scanner sc, ObjectOutputStream os,
                          ObjectInputStream is,Boolean sameAround,savedText savedText) throws IOException {
        this.windows = windows;
        this.player = player;
        this.territories = territories;
        this.sc = sc;
        this.is1 = is;
        this.os1 = os;
        //append or override the history file
        this.sameAround = sameAround;
        this.savedText=savedText;
    }
    //------------------
    //----function------
    //------------------
    @FXML
    public void initialize(){
        String s = "Hi, welcome player " + player.getId() + ".\r\n" +
                "Now you are in TECHNIQUE level " + player.getTechLevel() + ".\r\n" +
                "You have " + player.getFoodResources() + " food resources.\r\n" +
                "You have " + player.getTechResources() + " tech resources.\r\n";
        playerInfo.setText(s);
        //this.savedText.clearActionHistory();
        history.setText(this.savedText.getActionHistory());
    }


    //------------------
    //----Button function------
    //------------------

    public void alliancePop(){
        int result=chooseAlliacne("Choose the player");
        if(result!=-1){
            BufferedWriter bw1;
            try {
                bw1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("./history"+player.getId()+".txt"),this.sameAround)));
                bw1.write("Alliance"+"\n");
                bw1.write(result+"\n");
                bw1.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            this.sameAround=true;
            //write into history fields
            StringBuilder description=new StringBuilder();
            description.append("Make Alliance with: Player ").append(result+"\n");
            this.savedText.addAction(String.valueOf(description));
            this.history.setText(this.savedText.getActionHistory());
            //------------------
        }
        //if it is cancel, nothing happens

    }
    public void upgradePop() throws IOException {
        int result=popHelper("Choose Action");
        if(result==1){
            //store current action to history
            BufferedWriter bw1;
            try {
                bw1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("./history"+player.getId()+".txt"),this.sameAround)));
                bw1.write("T"+"\n");
                bw1.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            this.sameAround=true;
            //write into history fields
            StringBuilder description=new StringBuilder();
            description.append("Upgrade Technology Level\n");
            this.savedText.addAction(String.valueOf(description));
            //------------------
            this.history.setText(this.savedText.getActionHistory());

        }
        else if(result==2){
            //enter upgrade interface
            UpgradeUnitStage();
        }
        else if(result==3){
            //enter Tech speed up
            BufferedWriter bw1;
            try {
                bw1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("./history"+player.getId()+".txt"),this.sameAround)));
                bw1.write("TechSpeedUp"+"\n");
                bw1.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            this.sameAround=true;
            //write into history fields
            StringBuilder description=new StringBuilder();
            description.append("Speed Up Technology Resource Generating Rate\n");
            this.savedText.addAction(String.valueOf(description));
            this.history.setText(this.savedText.getActionHistory());


            //------------------
        }
        else if(result==4){
            //food speed up
            BufferedWriter bw1;
            try {
                bw1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("./history"+player.getId()+".txt"),this.sameAround)));
                bw1.write("FoodSpeedUp"+"\n");
                bw1.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            this.sameAround=true;
            //write into history fields
            StringBuilder description=new StringBuilder();
            description.append("Speed Up Food Resource Generating Rate\n");
            this.savedText.addAction(String.valueOf(description));
            //------------------
            this.history.setText(this.savedText.getActionHistory());
        }
        //if it is "cancel" option, just leave it alone
    }


    public void AttackStage() throws IOException {
        System.out.println("pop to attack stage");

        FXMLLoader Root = new FXMLLoader(getClass().getResource("Attack.fxml"));
        Root.setControllerFactory(c->{
            try {
                return new AttackController(this.windows,this.player,this.territories,this.sc,this.os1,this.is1,this.sameAround,
                        this.savedText);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
        Scene nextScene = new Scene(Root.load());
        this.windows.setScene(nextScene);
        windows.show();
    }

    public void MoveStage() throws IOException {
        FXMLLoader Root = new FXMLLoader(getClass().getResource("Move.fxml"));
        Root.setControllerFactory(c->{
            try {
                return new MoveController(this.windows,this.player,this.territories,this.sc,this.os1,this.is1,this.sameAround,
                        this.savedText);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
        Scene nextScene = new Scene(Root.load());
        this.windows.setScene(nextScene);
        windows.show();
    }



    public void commitAction() throws IOException, ClassNotFoundException {
        //append a "D" in  history file
        BufferedWriter bw1;
        try {
            bw1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("./history"+player.getId()+".txt"),this.sameAround)));
            bw1.write("D"+"\n");
            bw1.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        this.sameAround=false;
        System.out.println("boolean is "+this.sameAround);

        Executor endHelper = new Executor();
        //redirect setin
        System.setIn(new FileInputStream("./history" + player.getId() + ".txt"));
        sc = new Scanner(System.in);

        //read the content in file
        //the format cannot be wrong this time
        player.addAction(this.territories, String.valueOf(this.player.getId()), this.sc);

        //send the player to server
        os1.writeObject(player);
        os1.flush();
        os1.reset();
        //this.gameStatus.setText("Status: Waiting for other players...");

        //then getting the feedback
        this.player = (Player) is1.readObject();
        if (player.isvalid) {
            this.gameStatus.setText("Status: Waiting For Other Players...");
            territories = (Map<String, Territory>) is1.readObject();
            int winnerId=endHelper.checkWin(territories);
            if (winnerId== -1) {
                //if  current player does not lose
                if (!endHelper.singlePlayerFail(this.territories, this.player.getId())) {
                    this.gameStatus.setText("Status: Please Choose Your Action");
                    return;
                }
                else {
                    // when end the game, popup to close the whole games
                    endPop("Game Over! You Are Defeated");
                    this.windows.close();
                }
            }
            else{
                // print winnerId
                endPop("Game Over! Player "+ winnerId+" Is The Winner");
                this.windows.close();
            }
        }
        //else , it is not valid
        System.out.println("Collision! Choose Action Again");
        endPop("Collisions In Your Input!");

        //display again
        this.savedText.clearActionHistory();

        FXMLLoader MainRoot =new FXMLLoader(getClass().getResource("Main.fxml"));
        MainRoot.setControllerFactory(c->{
            try {
                return new mainController(this.windows,this.player,this.territories,this.sc,this.os1,this.is1,
                        this.savedText);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
        Scene nextScene=new Scene(MainRoot.load());
        this.windows.setScene(nextScene);
        this.windows.show();
    }






    //-------------------------
    //----helper function------
    //------------------------


    private void endPop(String information){
        Stage window = new Stage();
        //int status=0;

        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        //window.setTitle("RISC");
        window.setMinWidth(250);

        Label label = new Label();
        label.setText(information);

        Button quitButton =new Button(("OK"));
        quitButton.setOnAction(e ->{
            window.close();
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, quitButton);
        layout.setAlignment(Pos.TOP_CENTER);


        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    private int popHelper(String instruction){
        Stage window = new Stage();
        //int status=0;

        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        //window.setTitle("RISC");
        window.setMinWidth(250);

        Label label = new Label();
        label.setText(instruction);

        Button cancelButton =new Button(("Cancel"));
        cancelButton.setOnAction(e ->{
            status=0;
            window.close();
        });

        //four choices
        Button techButton = new Button("Upgrade Tech Level");
        techButton.setOnAction(e -> {
            status=1;
            window.close();

        });

        Button unitButton =new Button(("Upgrade Unit Level"));
        unitButton.setOnAction(e ->{
            status=2;
            window.close();
        });

        Button techSpeedButton = new Button("Upgrade Tech Generate Speed");
        techSpeedButton.setOnAction(e -> {
            status=3;
            window.close();

        });

        Button foodSpeedButton = new Button("Upgrade Food Generate Speed");
        foodSpeedButton.setOnAction(e -> {
            status=4;
            window.close();

        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, techButton,unitButton,techSpeedButton,foodSpeedButton,cancelButton);
        layout.setAlignment(Pos.CENTER);


        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        return status;
    }


    private int chooseAlliacne(String ins){
        Stage window = new Stage();
        //int status=0;

        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        //window.setTitle("RISC");
        window.setMinWidth(250);

        Label label = new Label();
        label.setText(ins);

        Button cancelButton =new Button(("Cancel"));
        cancelButton.setOnAction(e ->{
            status=-1;
            window.close();
        });

        VBox layout = new VBox(10);
        layout.getChildren().add(label);
        layout.setAlignment(Pos.TOP_CENTER);

        //ArrayList<Button> allPlayer=new ArrayList<>();
        for(int i=0;i<playerNum;i++){
            //todo:
            if(player.getId()==i) continue;
            Button button =new Button(("Player "+i));
            button.setOnAction(e ->{
                status=Integer.parseInt(button.getText().substring(7));
                window.close();
            });
            layout.getChildren().add(button);
        }

        layout.getChildren().add(cancelButton);
        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        return status;

    }


    private void UpgradeUnitStage() throws IOException {
        FXMLLoader Root = new FXMLLoader(getClass().getResource("Upgrade.fxml"));
        Root.setControllerFactory(c->{
            return new upgradeController(this.windows,this.player, this. territories,this.sc,this.os1,this.is1,this.sameAround,
                    this.savedText);
        });
        Scene nextScene = new Scene(Root.load());
        this.windows.setScene(nextScene);
        windows.show();
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
