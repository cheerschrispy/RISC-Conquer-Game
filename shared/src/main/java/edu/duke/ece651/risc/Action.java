package edu.duke.ece651.risc;

import java.io.Serializable;
import java.util.ArrayList;

public class Action implements Serializable {
    private String name;//Move or Attack or Upgrade
    private String start;//start territory name
    private String end;//end territory name
    private ArrayList<Unit> soldiers;
    private int requiredLevel;

    public int getRequiredLevel() {
        return requiredLevel;
    }

    //for Move("M") and Attack("A")
    Action(String name, String start, String end, ArrayList<Unit> soldiers) {
        this.name = name;
        this.start = start;
        this.end = end;
        this.soldiers = soldiers;
    }

    //for unit upgrade("U")
    Action(String name, ArrayList<Unit> soldiers, int requiredLevel) {
        this.name = name;
        this.soldiers = soldiers;
        this.requiredLevel = requiredLevel;
    }

    //for tech upgrade("T")
    Action(String name) {
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public String getStart(){
        return this.start;
    }

    public String getEnd(){
        return this.end;
    }

    public void addSoldier(Unit u){
        this.soldiers.add(u);
    }

    public int getLevels(){
        return this.requiredLevel;
    }

    public ArrayList<Unit> getSoldiers() {
        return soldiers;
    }
}
