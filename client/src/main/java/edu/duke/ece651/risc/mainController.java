package edu.duke.ece651.risc;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
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
    private Receiver receiver;
    private Sender sender;
    private int language = 1;

    private boolean sameAround;


    @FXML private Label gameStatus;
    @FXML private Button attack;
    @FXML private Button upgrade;
    @FXML private Button move;
    @FXML private Button commit;
    @FXML private Button alliance;

    @FXML private TextArea chatInput;
    @FXML private TextArea chatOutput;


    @FXML private TextArea mapInfo;
    @FXML private TextArea playerInfo;
    @FXML private TextArea history;
    @FXML private ImageView map;

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
                          ObjectInputStream is,savedText savedText, Receiver receiver, Sender sender){//todo: sender) {
        this.windows=windows;
        this.player=player;
        this.territories=territories;
        this.sc=sc;
        this.is1=is;
        this.os1=os;
        //append or override the history file
        this.sameAround=false;
        this.savedText=savedText;
        this.receiver = receiver;
        this.sender = sender;
    }
    //from subWindows to mainWindow
    public mainController(Stage windows, Player player, Map<String, Territory> territories, Scanner sc, ObjectOutputStream os,
                          ObjectInputStream is,Boolean sameAround,savedText savedText) {
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
        /*
        String s = "Hi, welcome player " + player.getId() + ".\r\n" +
                "Now you are in TECHNIQUE level " + player.getTechLevel() + ".\r\n\n" +
                "You have " + player.getFoodResources() + " food resources.\r\n" +
                "You have " + player.getTechResources() + " tech resources.\r\n" +
                "You current food production speed is " + player.getFoodSpeed() + ".\r\n" +
                "You current technology production speed is " + player.getTechSpeed() + ".\r\n";
        playerInfo.setText(s);
        */
        //this.savedText.clearActionHistory();
        history.setText(this.savedText.getActionHistoryE());
        Image mapImage=new Image("file:./map.jpg");
        map.setImage(mapImage);

        //receiver.start();
        TextPrinter t1 = new EngTextPrinter();
        String s = t1.appendPlayerInfo(player.getId(), player.getTechLevel(), player.getFoodResources(), player.getTechResources());
        playerInfo.setText(s);
    }

    public void changeLanguage(){
        this.language = (this.language + 1) % 2;
        //Player Information
        if(this.language == 0){
            //Player Information
            TextPrinter t1 = new ChiTextPrinter();
            String s = t1.appendPlayerInfo(player.getId(), player.getTechLevel(), player.getFoodResources(), player.getTechResources());
            playerInfo.setText(s);

            mapInfo.clear();
            history.setText(savedText.getActionHistoryC());
        }
        else{
            TextPrinter t1 = new EngTextPrinter();
            String s = t1.appendPlayerInfo(player.getId(), player.getTechLevel(), player.getFoodResources(), player.getTechResources());
            playerInfo.setText(s);

            mapInfo.clear();
            history.setText(savedText.getActionHistoryE());
        }
    }

    //------------------
    //----Button function------
    //------------------

    public void send() {
        //todo: parse in target id
        int target = 0;
        String text = chatInput.getText();
        sender.setMsg(text, target);
        sender.start();
    }

    public void alliancePop(){
        int result = chooseAlliance("Choose the player");
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
            this.savedText.addActionE("Make Alliance with: Player " + result + "\n");
            this.savedText.addActionC("和玩家 " + result + "结盟\n");

            if(this.language == 1)
                this.history.setText(this.savedText.getActionHistoryE());
            else
                this.history.setText(this.savedText.getActionHistoryC());
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
            this.savedText.addActionE("Upgrade Technology Level\n");
            this.savedText.addActionC("升级科技等级\n");
            //------------------
            if(this.language == 1)
                this.history.setText(this.savedText.getActionHistoryE());
            else
                this.history.setText(this.savedText.getActionHistoryC());
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
            this.savedText.addActionE("Speed Up Technology Resource Generating Rate\n");
            this.savedText.addActionC("升级科技资源生产速度\n");

            if(this.language == 1)
                this.history.setText(this.savedText.getActionHistoryE());
            else
                this.history.setText(this.savedText.getActionHistoryC());
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
            this.savedText.addActionE("Speed Up Food Resource Generating Rate\n");
            this.savedText.addActionC("升级粮食资源生产速度\n");
            //------------------
            if(this.language == 1)
                this.history.setText(this.savedText.getActionHistoryE());
            else
                this.history.setText(this.savedText.getActionHistoryC());
        }
        //if it is "cancel" option, just leave it alone
    }


    public void AttackStage() throws IOException {
        System.out.println("pop to attack stage");

        FXMLLoader Root = new FXMLLoader(getClass().getResource("Attack.fxml"));
        Root.setControllerFactory(c-> new AttackController(this.windows,this.player,this.territories,this.sc,this.os1,this.is1,this.sameAround,
                this.savedText));
        Scene nextScene = new Scene(Root.load());
        nextScene.getStylesheets().add(
                getClass().getResource("MainStyle.css")
                        .toExternalForm());
        this.windows.setScene(nextScene);
        windows.show();
    }

    public void MoveStage() throws IOException {
        FXMLLoader Root = new FXMLLoader(getClass().getResource("Move.fxml"));
        Root.setControllerFactory(c-> new MoveController(this.windows,this.player,this.territories,this.sc,this.os1,this.is1,this.sameAround,
                this.savedText));
        Scene nextScene = new Scene(Root.load());
        nextScene.getStylesheets().add(
                getClass().getResource("MainStyle.css")
                        .toExternalForm());
        this.windows.setScene(nextScene);
        windows.show();
    }



    public void commitAction() throws IOException, ClassNotFoundException {
        this.language = (this.language + 1) % 2;
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
        System.out.println("boolean is "+ this.sameAround);

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
        this.savedText.clearActionHistoryC();
        this.savedText.clearActionHistoryE();

        FXMLLoader MainRoot =new FXMLLoader(getClass().getResource("Main.fxml"));
        MainRoot.setControllerFactory(c->{
            return new mainController(this.windows,this.player,this.territories,this.sc,this.os1,this.is1,
                    this.savedText, this.receiver, this.sender);
        });
        Scene nextScene=new Scene(MainRoot.load());
        this.windows.setScene(nextScene);
        nextScene.getStylesheets().add(
                getClass().getResource("MainStyle.css")
                        .toExternalForm());
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
        quitButton.setOnAction(e -> window.close());

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


    private int chooseAlliance(String ins){
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
        Root.setControllerFactory(c-> new upgradeController(this.windows,this.player, this. territories,this.sc,this.os1,this.is1,this.sameAround,
                this.savedText));
        Scene nextScene = new Scene(Root.load());
        nextScene.getStylesheets().add(
                getClass().getResource("MainStyle.css")
                        .toExternalForm());
        this.windows.setScene(nextScene);
        nextScene.getStylesheets().add(
                getClass().getResource("MainStyle.css")
                        .toExternalForm());
        windows.show();
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
