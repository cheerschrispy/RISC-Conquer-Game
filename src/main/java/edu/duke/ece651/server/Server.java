package edu.duke.ece651.server;

import java.util.*;

public class Server {
    private Map<String, Territory> territories;
    private List<Player> players;

    Server() {
        this.territories = new HashMap<>();
        this.players = new ArrayList<>();
    }

    public void run() {
        createTerritories();
        createPlayers();

    }

    private void createTerritories() {
        territories.put("a1", new Territory("a1", 1, 3));
        territories.put("a2", new Territory("a2", 1, 3));
        territories.put("a3", new Territory("a3", 1, 3));
        territories.put("b1", new Territory("b1", 2, 3));
        territories.put("b2", new Territory("b2", 2, 3));
        territories.put("b3", new Territory("b3", 2, 3));
        territories.put("c1", new Territory("c1", 3, 3));
        territories.put("c2", new Territory("c2", 3, 3));
        territories.put("c3", new Territory("c3", 3, 3));
        connect("a1", "a2");
        connect("a2", "a3");
        connect("b1", "b2");
        connect("b2", "b3");
        connect("c1", "c2");
        connect("c2", "c3");
        connect("a1", "b1");
        connect("b1", "c1");
        connect("a2", "b2");
        connect("b2", "c2");
        connect("a3", "b3");
        connect("b3", "c3");
    }

    private void connect(String t1, String t2) {
        territories.get(t1).connect(territories.get(t2));
        territories.get(t2).connect(territories.get(t1));
    }

    private void createPlayers() {
        for (int i = 0; i < 3; i++) {
            Player player = new Player(i);
            players.add(player);
        }
    }
}
