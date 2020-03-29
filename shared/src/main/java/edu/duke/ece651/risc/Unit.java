package edu.duke.ece651.risc;

import java.io.Serializable;
import java.util.*;

public class Territory implements Serializable {
    private String name;
    private int owner;
    private int num;//soldier num in this territory
    private Set<Territory> neighbors;
    private int size;

    Territory(String name, int owner, int num) {
        this.name = name;
        this.owner = owner;
        this.num = num;
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

    public int getNum(){
        return this.num;
    }

    public String getName(){
        return this.name;
    }

    public Set<Territory> getNeighbors(){
        return this.neighbors;
    }

    public void setNum(int i){
        this.num = i;
    }

    public void setOwner(int i){
        this.owner = i;
    }

    public int getSize(){
        return this.size;
    }
}
