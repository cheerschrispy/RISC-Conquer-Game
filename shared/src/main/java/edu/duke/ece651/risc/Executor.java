package edu.duke.ece651.risc;

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

    public void attack(Map<String, Map<Integer, ArrayList<Unit>>> attacks, Map<String, Territory> territories) {

        Set<String> battleFields = attacks.keySet(); // Get the names of all lands

        // Loop through each land
        for (String currBFD : battleFields) { // For each current battle field
            // System.out.println("Current battle field is: " + currBFD + "!");
            Map<Integer, ArrayList<Unit>> attackSquads = attacks.get(currBFD); // Get the attack squads
            Territory currTerr = territories.get(currBFD);                // Get the current territory
            // System.out.println(currBFD + " is owned by: " + currTerr.getOwner() 
            //                             + " with " + currTerr.getNum() + " soldiers.");
            ForcePair attacker;
            ForcePair defender;

            defender = deployDefender(currTerr);
            // Loop until one survivor
            while (!attackSquads.isEmpty()) {
                attacker = selectAttacker(attackSquads); // Select and remove a attacker from attackSquads
                Arena currArena = new Arena(attacker, defender);
                defender = currArena.run(); // One that survive become new defender
            }
            assertDomination(defender, currTerr); // Write combat result in currTerr
        }

    }

    //execution starts here
    public void execute(Player[] players, Map<String, Territory> territories) {
        Validator validator = new Validator();
        for (Player player : players) {
            List<Action> actions = player.getActions();
            move(player, actions, territories, validator);
            unitUpgrade(player, actions);
        }
        attack(convert(players, territories), territories);
        increase(players, territories);
        for (Player player : players) {
            List<Action> actions = player.getActions();
            techUpgrade(player, actions);
        }
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
        for (Map.Entry entry : territories.entrySet()) {
            Territory t = (Territory) entry.getValue();
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
                territories.get(start).getSoldiers().removeAll(soldiers);
                territories.get(end).getSoldiers().addAll(soldiers);
                player.consumeFood(validator.BFS(start, end, territories) * action.getSoldiers().size());
            }
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
                    territories.get(start).getSoldiers().removeAll(soldiers);
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
    public boolean checkWin(Map<String, Territory> territories) {
        Set<Integer> set=win_helper(territories);
        if (set.size() == 1){
            System.out.println("Player "+set.toArray()[0]+" is the Winner!");
            return true;
        }
        else return false;
    }

    public boolean singlePlayerFail(Map<String, Territory> territories,int id){
        Set<Integer> set=win_helper(territories);
        return !set.contains(id);
    }


}

