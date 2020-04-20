package edu.duke.ece651.risc;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class Mapbutton extends Button {
    private String name;
    public Mapbutton(String name){
        this.setText(name);
    }

    public void showMapInfo(TextArea mapInfo){
        this.setOnAction(e->{
            mapInfo.setText(this.name);
        });
    }

}
