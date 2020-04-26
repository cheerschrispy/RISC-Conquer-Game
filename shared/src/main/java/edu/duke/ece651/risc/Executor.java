package edu.duke.ece651.risc;

import javax.swing.*;
import java.util.*;

public class Executor {

    private ForcePair selectAttacker(Map<Integer, ArrayList<Unit>> attackSquads) {

        Integer[] keyArray = attackSquads.keySet().toArray(new Integer[0]); // Store all keys in an array
        Random rand = new Random(System.currentTimeMillis());

        // Generate random integers in range 0 to keyArray size
        int rand_index = rand.nextInt(keyArray.length);
        int squad_id = keyArray[rand_index];
        ArrayList<Unit> soldierList = attackSquads.remove(squad_id); // Remove from attackSquads
        // System.out.println("Selecting attacker ... Squad " + squad_id 
        //                                        + "! (" + soldier_num + " soldiers)");

        ForcePair out = new ForcePair(squad_id, soldierList);

        return out;
    }

    private ForcePair deployDefender(Territory terr) {
        ArrayList<Unit> soldiers = terr.getSoldiers();
        ForcePair out = new ForcePair(terr.getOwner(), soldiers);
        return out;
    }

    private void assertDomination(ForcePair survivor, Territory terr) {

        // System.out.println(terr.getName() + "'s new owner is " + survivor.getOwner() + 
        //                            " with " + survivor.getSoldier() + " soldiers");
        terr.setOwner(survivor.getOwner());
        terr.setSoldiers(survivor.getSoldiers());
    }

    public void attack(Map<String, Map<Integer, ArrayList<Unit>>> attacks, Map<String, Territory> territories, Player[] players) {

        Set<String> battleFields = attacks.keySet(); // Get the names of all lands

        // Loop through each land
        for (String currBFD : battleFields) { // For each current battle field
            // System.out.println("Current battle field is: " + currBFD + "!");
            Map<Integer, ArrayList<Unit>> attackSquads = attacks.get(currBFD); // Get the attack squads
            Territory currTerr = territories.get(currBFD); // Get the current territory
            // System.out.println(currBFD + " is owned by: " + currTerr.getOwner()
            // + " with " + currTerr.getNum() + " soldiers.");
            ForcePair attacker;
            ForcePair defender;

            defender = deployDefender(currTerr);
            // Loop until one survivor
            while (!attackSquads.isEmpty()) {
                attacker = selectAttacker(attackSquads); // Select and remove a attacker from attackSquads
                Arena currArena = new Arena(attacker, defender);
                defender = currArena.run(); // One that survive become new defender
            }

            // If the origin owner lost its control, allies come to "help"
            if(defender.getOwner() != currTerr.getOwner()) {
                attackSquads = currTerr.getAllies();
                while (!attackSquads.isEmpty()) {
                    attacker = selectAttacker(attackSquads); // Select and remove a attacker from attackSquads
                    Arena currArena = new Arena(attacker, defender);
                    defender = currArena.run(); // One that survive become new defender
                }

                // No allies any more
                for (Map.Entry<Integer, ArrayList<Unit>> entry : currTerr.getAllies().entrySet()) {
                    currTerr.getAllies().put(entry.getKey(), new ArrayList<>());
                }
            }

            assertDomination(defender, currTerr); // Write combat result in currTerr
        }

    }



    //execution starts here
    public void execute(Player[] players, Map<String, Territory> territories) {
        breakAlly(players, territories);
        Validator validator = new Validator();
        for (Player player : players) {
            List<Action> actions = player.getActions();
            move(player, actions, territories, validator);
            unitUpgrade(player, actions);
        }
        attack(convert(players, territories), territories, players);
        increase(players, territories);
        for (Player player : players) {
            List<Action> actions = player.getActions();
            techUpgrade(player, actions);
            techSpeed(player, actions);
            foodSpeed(player, actions);
        }
        findAlliance(players);
    }

    //speed tech
    public void techSpeed(Player player, List<Action> actions) {
        for (Action action : actions) {
            if (action.getName().equals("P")) {
                player.consumeTech((player.getTechLevel() + 1) * 10);
                player.addTechSpeed();
                return;
            }
        }
    }

    //speed food
    public void foodSpeed(Player player, List<Action> actions) {
        for (Action action : actions) {
            if (action.getName().equals("Q")) {
                player.consumeFood((player.getTechLevel() + 1) * 10);
                player.addFoodSpeed();
                return;
            }
        }
    }

    private void breakAlly(Player[] players, Map<String, Territory> territories) {
        for (Player player : players) {
            List<Action> actions = player.getActions();
            for (Action action : actions) {
                if (action.getName().equals("A")) {
                    String end = action.getEnd();
                    Player p2 = players[territories.get(end).getOwner()];
                    if (player.getAlliances().contains(p2.getId())) {
                        player.getAlliances().remove(p2.getId());
                        p2.getAlliances().remove(player.getId());
                        for (String src : territories.keySet()) {
                            if (territories.get(src).getOwner() == player.getId()) {
                                bfs(src, p2, territories);
                            }
                            if (territories.get(src).getOwner() == p2.getId()) {
                                bfs(src, player, territories);
                            }
                        }
                    }
                }
            }
        }
    }

    private void bfs(String src, Player player, Map<String, Territory> territories) {
        Queue<Territory> q = new LinkedList<>();
        HashSet<Territory> visited = new HashSet<>();
        Territory start = territories.get(src);
        q.offer(start);
        visited.add(start);
        while(!q.isEmpty()){
            Territory curr = q.peek();
            q.poll();
            for(Territory t : curr.getNeighbors()){
                if(!visited.contains(t)){
                    q.offer(t);
                    visited.add(t);
                    if(t.getOwner() == player.getId()){
                        t.getSoldiers().addAll(start.getAllies().get(player.getId()));
                        start.getAllies().get(player.getId()).clear();
                    }
                }
            }
        }
    }

    //find alliance
    public void findAlliance(Player[] players) {
        Map<Integer, Integer> map = new HashMap<>();
        for (Player player : players) {
            List<Action> actions = player.getActions();
            for (Action action : actions) {
                if (action.getName().equals("L")) {
                    map.put(player.getId(), action.getAlliance());
                    break;
                }
            }
        }
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            int key = entry.getKey();
            int value = entry.getValue();
            if (map.containsKey(value) && map.get(value) == key) {
                bind(key, value, players);
            }
        }
    }

    //form alliance
    public void bind(int key, int value, Player[] players) {
        players[key].getAlliances().add(value);
        players[value].getAlliances().add(key);
    }

    //upgrade units
    public void unitUpgrade(Player player, List<Action> actions) {
        for (Action action : actions) {
            if (action.getName().equals("U")) {
                ArrayList<Unit> soldiers = action.getSoldiers();
                int requiredLevel = action.getRequiredLevel();
                for (Unit soldier:soldiers) {
                    int cost = soldier.upgrade(requiredLevel);
                    player.consumeTech(cost);
                }
            }
        }
    }

    //upgrade tech
    public void techUpgrade(Player player, List<Action> actions) {
        for (Action action : actions) {
            if (action.getName().equals("T")) {
                player.upgrade();
                return;
            }
        }
    }

    //increase food, tech, unit
    public void increase(Player[] players, Map<String, Territory> territories) {
        for (Map.Entry<String, Territory> entry : territories.entrySet()) {
            Territory t = entry.getValue();
            t.getSoldiers().add(new Unit());
            for (Player player : players) {
                if (t.getOwner() == player.getId()) {
                    player.addFood(t.getFoodProduct());
                    player.addTech(t.getTechProduct());
                }
            }
        }
    }

    //move
    public void move(Player player, List<Action> actions, Map<String, Territory> territories, Validator validator) {
        for (Action action : actions) {
            if (action.getName().equals("M")) {
                String start = action.getStart();
                String end = action.getEnd();
                ArrayList<Unit> soldiers = action.getSoldiers();
                remove(player, territories, start, soldiers);
                add(player, territories, end, soldiers);
                player.consumeFood(validator.BFS(player,start, end, territories) * action.getSoldiers().size());
            }
        }
    }

    private void add(Player player, Map<String, Territory> territories, String end, ArrayList<Unit> soldiers) {
        if (player.getId() == territories.get(end).getOwner()) {
            territories.get(end).getSoldiers().addAll(soldiers);
        } else {
            territories.get(end).getAllies().get(player.getId()).addAll(soldiers);
        }
    }

    private void remove(Player player, Map<String, Territory> territories, String start, ArrayList<Unit> soldiers) {
        if (player.getId() == territories.get(start).getOwner()) {
            territories.get(start).getSoldiers().removeAll(soldiers);
        } else {
            territories.get(start).getAllies().get(player.getId()).removeAll(soldiers);
        }
    }

    //convert data format for attack
    public Map<String, Map<Integer, ArrayList<Unit>>> convert(Player[] players, Map<String, Territory> territories) {
        Map<String, Map<Integer, ArrayList<Unit>>> attacks = new HashMap<>();
        for (Player player : players) {
            List<Action> actions = player.getActions();
            for (Action action : actions) {
                if (action.getName().equals("A")) {
                    String start = action.getStart();
                    ArrayList<Unit> soldiers = action.getSoldiers();
                    remove(player, territories, start, soldiers);
                    String subject = action.getEnd();
                    if (attacks.containsKey(subject)) {
                        attacks.get(subject).put(player.getId(), soldiers);
                    } else {
                        Map<Integer, ArrayList<Unit>> map = new HashMap<>();
                        map.put(player.getId(), action.getSoldiers());
                        attacks.put(subject, map);
                    }
                }
            }
        }
        return attacks;
    }


    public Set<Integer> win_helper(Map<String, Territory> territories){
        Set<Integer> set = new HashSet<>();
        for (Map.Entry<String, Territory> entry : territories.entrySet()) {
            set.add(entry.getValue().getOwner());
        }
        return set;
    }
    public boolean checkWin(Map<String, Territory> territories, JTextArea textArea) {
        Set<Integer> set=win_helper(territories);
        if (set.size() == 1){
            textArea.setText("");
            textArea.append("Player "+set.toArray()[0]+" is the Winner!\n");
            textArea.append("Please Quit");
            return true;
        }
        else return false;
    }
    public int checkWin(Map<String, Territory> territories) {
        Set<Integer> set=win_helper(territories);
        if (set.size() == 1){
            System.out.println("Player "+set.toArray()[0]+" is the Winner!\n");
            return (int) set.toArray()[0];
        }
        else return -1;
    }

    public boolean singlePlayerFail(Map<String, Territory> territories,int id){
        Set<Integer> set=win_helper(territories);
        return !set.contains(id);
    }


}

