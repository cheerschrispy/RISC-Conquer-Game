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

import java.io.IOException;

//for attack
public class mainController {

    //----------------
    //--private fields--
    //----------------
    private int status;
    private int playerNum = 3;
    //private Map<String, Territory> territories;
    //Player player;
    /*
    @FXML private Button attack;
    @FXML private Button upgrade;
    @FXML private Button move;
    @FXML private Button commit;
    @FXML private Button alliance;
    */
    @FXML private TextArea mapInfo;
    @FXML private TextArea playerInfo;
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
    public mainController(Stage windows) throws IOException {
        this.windows=windows;
    }

    public void showMapInfo_1(){
        this.mapInfo.setText("This is 1");
    }

    public void showMapInfo_2(){
        this.mapInfo.setText("This is 2");
    }
    //------------------
    //----function------
    //------------------

    /*
    public void setPlayerInfo(){
        String s = "Hi, welcome player " + player.getId() + ".\r\n" +
                "Now you are in TECHNIQUE level " + player.getTechLevel() + ".\r\n" +
                "You have " + player.getFoodResources() + " food resources.\r\n" +
                "You have " + player.getTechResources() + " tech resources.\r\n";
        playerInfo.setText(s);
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
        HashMap<Integer, ArrayList<Unit>> alliances = getAllies();
        for(int key : alliances.keySet()){
            ArrayList<Unit> ally = alliances.get(key);
            s.append("It has ally player ").append(key).append(" in this territory");
            for(int i = 0; i < 7; i++){
                int num = getAllyNumOfLevel(i, key);
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
        HashMap<Integer, ArrayList<Unit>> alliances = getAllies();
        for(int key : alliances.keySet()){
            ArrayList<Unit> ally = alliances.get(key);
            s.append("It has ally player ").append(key).append(" in this territory");
            for(int i = 0; i < 7; i++){
                int num = getAllyNumOfLevel(i, key);
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
        HashMap<Integer, ArrayList<Unit>> alliances = getAllies();
        for(int key : alliances.keySet()){
            ArrayList<Unit> ally = alliances.get(key);
            s.append("It has ally player ").append(key).append(" in this territory");
            for(int i = 0; i < 7; i++){
                int num = getAllyNumOfLevel(i, key);
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
        HashMap<Integer, ArrayList<Unit>> alliances = getAllies();
        for(int key : alliances.keySet()){
            ArrayList<Unit> ally = alliances.get(key);
            s.append("It has ally player ").append(key).append(" in this territory");
            for(int i = 0; i < 7; i++){
                int num = getAllyNumOfLevel(i, key);
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
        HashMap<Integer, ArrayList<Unit>> alliances = getAllies();
        for(int key : alliances.keySet()){
            ArrayList<Unit> ally = alliances.get(key);
            s.append("It has ally player ").append(key).append(" in this territory");
            for(int i = 0; i < 7; i++){
                int num = getAllyNumOfLevel(i, key);
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
        HashMap<Integer, ArrayList<Unit>> alliances = getAllies();
        for(int key : alliances.keySet()){
            ArrayList<Unit> ally = alliances.get(key);
            s.append("It has ally player ").append(key).append(" in this territory");
            for(int i = 0; i < 7; i++){
                int num = getAllyNumOfLevel(i, key);
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
        HashMap<Integer, ArrayList<Unit>> alliances = getAllies();
        for(int key : alliances.keySet()){
            ArrayList<Unit> ally = alliances.get(key);
            s.append("It has ally player ").append(key).append(" in this territory");
            for(int i = 0; i < 7; i++){
                int num = getAllyNumOfLevel(i, key);
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
        HashMap<Integer, ArrayList<Unit>> alliances = getAllies();
        for(int key : alliances.keySet()){
            ArrayList<Unit> ally = alliances.get(key);
            s.append("It has ally player ").append(key).append(" in this territory");
            for(int i = 0; i < 7; i++){
                int num = getAllyNumOfLevel(i, key);
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
        HashMap<Integer, ArrayList<Unit>> alliances = getAllies();
        for(int key : alliances.keySet()){
            ArrayList<Unit> ally = alliances.get(key);
            s.append("It has ally player ").append(key).append(" in this territory");
            for(int i = 0; i < 7; i++){
                int num = getAllyNumOfLevel(i, key);
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
        HashMap<Integer, ArrayList<Unit>> alliances = getAllies();
        for(int key : alliances.keySet()){
            ArrayList<Unit> ally = alliances.get(key);
            s.append("It has ally player ").append(key).append(" in this territory");
            for(int i = 0; i < 7; i++){
                int num = getAllyNumOfLevel(i, key);
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
        HashMap<Integer, ArrayList<Unit>> alliances = getAllies();
        for(int key : alliances.keySet()){
            ArrayList<Unit> ally = alliances.get(key);
            s.append("It has ally player ").append(key).append(" in this territory");
            for(int i = 0; i < 7; i++){
                int num = getAllyNumOfLevel(i, key);
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
        HashMap<Integer, ArrayList<Unit>> alliances = getAllies();
        for(int key : alliances.keySet()){
            ArrayList<Unit> ally = alliances.get(key);
            s.append("It has ally player ").append(key).append(" in this territory");
            for(int i = 0; i < 7; i++){
                int num = getAllyNumOfLevel(i, key);
                if(num != 0){
                    s.append("It has   ").append(num).append("   level-").append(i).append(" soldiers.\r\n");
                }
            }
            s.append("\r\n");
        }
        this.mapInfo.setText(String.valueOf(s));
    }
    */

    public void AttackStage() throws IOException {
        System.out.println("pop to attack stage");

        FXMLLoader Root = new FXMLLoader(getClass().getResource("Attack.fxml"));
        Root.setControllerFactory(c->{
            return new AttackController(this.windows);
        });
        Scene nextScene = new Scene(Root.load());
        this.windows.setScene(nextScene);
        windows.show();
    }

    public void MoveStage() throws IOException {
        FXMLLoader Root = new FXMLLoader(getClass().getResource("Move.fxml"));
        Root.setControllerFactory(c->{
            return new MoveController(this.windows);
        });
        Scene nextScene = new Scene(Root.load());
        this.windows.setScene(nextScene);
        windows.show();
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
        techButton.setOnAction(e -> {
            status=3;
            window.close();

        });

        Button foodSpeedButton = new Button("Upgrade Food Generate Speed");
        techButton.setOnAction(e -> {
            status=4;
            window.close();

        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, techButton,unitButton,techSpeedButton,foodSpeedButton,cancelButton);
        layout.setAlignment(Pos.TOP_CENTER);


        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        return status;
    }


    public void upgradePop() throws IOException {
        int result=popHelper("Choose Action");
        if(result==1){
            //store current action to history

        }
        else if(result==2){
            //enter upgrade interface
            UpgradeUnitStage();
        }
        //if it is "cancel" option, just leave it alone
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
            // if(player.getid==i) continue;
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
    public void alliancePop(){
        int result=chooseAlliacne("Choose the player");
        if(result!=-1){
            //todo:write to txt file current action

        }
        //if it is cancel, nothing happens

    }

    private void UpgradeUnitStage() throws IOException {
        FXMLLoader Root = new FXMLLoader(getClass().getResource("Upgrade.fxml"));
        Root.setControllerFactory(c->{
            return new upgradeController(this.windows);
        });
        Scene nextScene = new Scene(Root.load());
        this.windows.setScene(nextScene);
        windows.show();
    }

}
