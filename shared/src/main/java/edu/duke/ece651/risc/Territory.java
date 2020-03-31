package edu.duke.ece651.risc;

import java.io.Serializable;
import java.util.*;

public class Territory implements Serializable {
    private String name;
    private int owner;
    private ArrayList<Unit> soldiers;
    private Set<Territory> neighbors;
    private int size;

    Territory(String name, int owner) {
        this.name = name;
        this.owner = owner;
        this.soldiers = new ArrayList<>();
        neighbors = new HashSet<>();
        this.size = 3;
    }

    //add  other territory to its neighbor list
    public void connect(Territory t) {
        this.neighbors.add(t);
    }

    public int getOwner(){
        return this.owner;
    }

    public String getName(){
        return this.name;
    }

    public Set<Territory> getNeighbors(){
        return this.neighbors;
    }

    public void setOwner(int i){
        this.owner = i;
    }

    public int getSize(){
        return this.size;
    }

    public ArrayList<Unit> getSoldiers() {
        return soldiers;
    }

    public void setSoldiers(ArrayList<Unit> s) {
        this.soldiers = s;
    }
}
