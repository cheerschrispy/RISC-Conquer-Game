package edu.duke.ece651.risc;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;

public class upgradeController {
    //All fields
    //private Map<String, Territory> territories;
    //private Player player;

    private Stage windows;
    //all the button
    @FXML private Button done;//return to main page
    @FXML private ComboBox<String> source;
    @FXML private ComboBox<Integer> srcLevel;
    @FXML private ComboBox<Integer> destLevel;
    @FXML private ComboBox<Integer> num;

    public upgradeController(Stage windows){
        this.windows=windows;
    }

    @FXML
    public void initialize(){
        ObservableList<Integer> options= FXCollections.observableArrayList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25);
        ObservableList<Integer> levels = FXCollections.observableArrayList(0, 1, 2, 3, 4, 5, 6);
        srcLevel.setItems(levels);
        srcLevel.getSelectionModel().select(0);
        destLevel.setItems(levels);
        destLevel.getSelectionModel().select(0);
        num.setItems(options);
        num.getSelectionModel().select(0);

        /*
        Set<Integer> alliances = player.getAlliances();
        alliances.add(player.getId());
        ObservableList<Integer> s = FXCollections.observableArrayList();

        for(String key: territories.keySet()){
            Territory t = territories.get(key);
            if(alliances.contains(t.getOwner())){
                s.add(key);
            }
        }
        source.setItems(s);
        source.getSelectionModel().select(0);
        */
    }

    public void finishUpgradeAction() throws IOException {
        System.out.println("pop Back main");

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
