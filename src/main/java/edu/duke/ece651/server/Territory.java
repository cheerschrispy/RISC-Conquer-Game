package edu.duke.ece651.server;

import java.util.*;

public class Territory {
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
        neighbors.add(t);
    }
}
