package edu.duke.ece651.risc;

import java.io.Serializable;
import java.util.ArrayList;

public class Action implements Serializable {
    private String name;//Move or Attack
    private String start;//start territory name
    private String end;
    //private int num;
    private ArrayList<Unit> soldiers;
    int requiredLevel;

    Action(String name, String start, String end) {
        this.name = name;
        this.start = start;
        this.end = end;
        this.soldiers = new ArrayList<>();
    }

    public String getName(){
        return this.name;
    }

    public String getStart(){
        return this.start;
    }

    public String getEnd(){ return this.end; }

    public void addSoldiers(Unit u){
        this.soldiers.add(u);
    }

    public void setLevels(int i){
        this.requiredLevel = i;
    }

    public int getLevels(){
        return this.requiredLevel;
    }

    public ArrayList<Unit> getSoldiers() {
        return soldiers;
    }
}
