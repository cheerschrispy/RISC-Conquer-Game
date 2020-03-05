package edu.duke.ece651.server;

import java.util.*;

public class Player {
    private int id;
    private List<Action> actions;

    Player(int id) {
        this.id = id;
        this.actions = new ArrayList<>();
    }
}
