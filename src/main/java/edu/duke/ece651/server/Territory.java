package edu.duke.ece651.server;

import java.io.Serializable;
import java.util.*;

public class Territory implements Serializable {
    private String name;
    private int owner;
    private int num;
    private Set<Territory> neighbors;

    Territory(String name, int owner, int num) {
        this.name = name;
        this.owner = owner;
        this.num = num;
        neighbors = new HashSet<>();
    }

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
}
