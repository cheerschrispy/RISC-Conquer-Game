package edu.duke.ece651.risc;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;

public class MapButton extends JButton {
    public boolean canClick;
    //public boolean isClicking=false;
    private String name;

    public MapButton(boolean canClick, String name) {
            this.setText(name);
            this.canClick=canClick;
            this.name=name;
    };

    public void setCanClickSelf(Map<String, Territory> territories, int playerID){
        this.canClick= territories.get(this.name).getOwner() == playerID;
    }

    public void setCanClickEnemy(Map<String, Territory> territories, int playerID) {
        this.canClick= !(territories.get(this.name).getOwner() == playerID);
    }

    //only can be performed when canClick is true;
    public void act(final ArrayList<String> buffer){
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    System.out.println(name);
                    buffer.add(name);
            }
        });
    }

    public String getName(){
        return this.name;
    }
}
