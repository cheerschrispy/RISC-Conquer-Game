package edu.duke.ece651.risc;

import java.io.Serializable;
import java.util.*;

public class Unit {
    private int bonus;
    private int level;
    private String name;

    public Unit(){
        this.bonus = 0;
        this.level = 0;
    }

    public String getName(){
        return this.name;
    }

    public void setName(){
        this.name = "Soldier " + level;
    }

    public void setBonus(int i){
        this.bonus = i;
    }

    public int getBonus(){
        return this.bonus;
    }

    public int getLevel(){
        return this.level;
    }

    public void setLevel(int i){
        this.level = i;
    }
}
