package edu.duke.ece651.server;

import java.util.*;

public class Player {
    private int id;
    private List<Action> actions;

    public Player(int id) {
        this.id = id;
        this.actions = new ArrayList<>();
    }

    public int getId(){
        return this.id;
    }

    public List<Action> getActions(){
        return this.actions;
    }
}
