package edu.duke.ece651.risc;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

//for attack
public class MoveController {
    //All fields
    //private Map<String, Territory> territories;
    //private Player player;

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

    @FXML
    public void initialize(){
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



        /*
        Set<Integer> alliances = player.getAlliances();
        alliances.add(player.getId());
        ObservableList<Integer> s = FXCollections.observableArrayList();
        ObservableList<Integer> d = FXCollections.observableArrayList();

        for(String key: territories.keySet()){
            Territory t = territories.get(key);
            if(alliances.contains(t.getOwner())){
                s.add(key);
            }
        }
        Src.setItems(s);
        Src.getSelectionModel().select(0);
        Dest.setItems(s);
        Dest.getSelectionModel().select(0);
        */
    }

    //current windows
    private Stage windows;

    public MoveController(Stage windows){
        this.windows=windows;
    }

    public void finish_AM_Action() throws IOException {
        System.out.println("pop Back");
        FXMLLoader MainRoot =new FXMLLoader(getClass().getResource("Main.fxml"));
        MainRoot.setControllerFactory(c->{
            try {
                return new mainController(this.windows);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
        Scene nextScene=new Scene(MainRoot.load());
        this.windows.setScene(nextScene);
        this.windows.show();
    }
}
