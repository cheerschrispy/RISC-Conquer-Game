package edu.duke.ece651.server;

import java.io.Serializable;
import java.util.*;

public class Player implements Serializable {
    private int id;
    private List<Action> actions;

    public Player(int id) {
        this.id = id;
        this.actions = new ArrayList<>();
    }

    public int getId(){
        return this.id;
    }

    public List<Action> getActions() {
        return this.actions;
    }

    public void addAction(Action action) {
        this.actions.add(action);
    }
}
