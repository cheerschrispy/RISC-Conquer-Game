package edu.duke.ece651.server;

import java.util.*;

public class Executor {
    public void attack(Map<String, Map<Integer, Integer>> attacks, Map<String, Territory> territories) {

    }

    public void execute(List<Player> players, Map<String, Territory> territories) {
        for (Player player : players) {
            List<Action> actions = player.getActions();
            move(actions, territories);
        }
        attack(convert(players), territories);
    }

    public void move(List<Action> actions, Map<String, Territory> territories) {
        for (Action action : actions) {
            if (action.getName().equals("M")) {
                String start = action.getStart();
                String end = action.getEnd();
                int num = action.getNum();
                territories.get(start).setNum(territories.get(start).getNum() - num);
                territories.get(end).setNum(territories.get(end).getNum() + num);
            }
        }
    }

    public Map<String, Map<Integer, Integer>> convert(List<Player> players) {
        Map<String, Map<Integer, Integer>> attacks = new HashMap<>();
        for (Player player : players) {
            List<Action> actions = player.getActions();
            for (Action action : actions) {
                if (action.getName().equals("A")) {
                    String subject = action.getEnd();
                    if (attacks.containsKey(subject)) {
                        attacks.get(subject).put(player.getId(), action.getNum());
                    } else {
                        Map<Integer, Integer> map = new HashMap<>();
                        map.put(player.getId(), action.getNum());
                        attacks.put(subject, map);
                    }
                }
            }
        }
        return attacks;
    }

    public boolean checkWin(Map<String, Territory> territories) {
        Set<Integer> set = new HashSet<>();
        for (Map.Entry<String, Territory> entry : territories.entrySet()) {
            set.add(entry.getValue().getOwner());
        }
        return set.size() == 1;
    }
}
