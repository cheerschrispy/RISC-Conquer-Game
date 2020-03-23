package edu.duke.ece651.risc;

import org.junit.jupiter.api.Test;

import java.util.*;

public class TestExecutor {
    @Test
    public void test_Executor() {
        Executor executor = new Executor();
        List<Player> players = new ArrayList<>();
        Map<String, Territory> territories = new HashMap<>();
        Territory t00 = new Territory("00", 0, 3);
        Territory t01 = new Territory("01", 0, 3);
        Territory t10 = new Territory("10", 1, 3);
        Territory t20 = new Territory("20", 2, 3);
        territories.put("00", t00);
        territories.put("01", t01);
        territories.put("10", t10);
        territories.put("20", t20);
        t00.connect(t01);
        t01.connect(t00);
        t01.connect(t10);
        t10.connect(t01);
        t10.connect(t20);
        t20.connect(t10);
        Player p0 = new Player(0);
        p0.getActions().add(new Action("M", "00", "01", 3));
        p0.getActions().add(new Action("A", "01", "10", 3));
        Player p2 = new Player(2);
        p2.getActions().add(new Action("A", "20", "10", 1));
        players.add(p0);
        players.add(p2);
        executor.execute(players, territories);
        executor.win_helper(territories);
        executor.checkWin(territories);
        territories.get("10").setOwner(0);
        territories.get("20").setOwner(0);
        executor.checkWin(territories);
        executor.singlePlayerFail(territories, 1);
    }
}

