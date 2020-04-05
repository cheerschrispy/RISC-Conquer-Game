package edu.duke.ece651.risc;

import java.io.Serializable;
import java.util.*;

public class Unit implements Serializable{
    private int bonus;
    private int level;
    private String name;
    private final int[] costs = new int[] {0, 3, 8, 19, 25, 35, 50};
    private final int[] bonuses = new int[] {0, 1, 3, 5, 8, 11, 15};

    public Unit(){
        this.bonus = 0;
        this.level = 0;
        this.name = "Soldier " + level;
    }

    public Unit(int level){
        this.bonus=bonuses[level];
        this.level=level;
        this.name = "Soldier " + level;
    }


    public String getName(){
        return this.name;
    }

    public int getBonus(){
        return this.bonus;
    }

    public int getLevel(){
        return this.level;
    }

    //upgrade to level target and return cost
    public int upgrade(int target){
        int cost = 0;
        for (int i = level + 1; i <= target; i++) {
            cost += costs[i];
        }
        this.level = target;
        this.name = "Soldier " + target;
        this.bonus = bonuses[target];
        return cost;
    }
}
