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


    public void execute(List<Player> players, Map<String, Territory> territories) {
        for (Player player : players) {
            List<Action> actions = player.getActions();
            move(actions, territories);
        }
        attack(convert(players, territories), territories);
        increase(territories);
    }

    public void increase(Map<String, Territory> territories) {
        for (Map.Entry entry : territories.entrySet()) {
            Territory t = (Territory) entry.getValue();
            t.setNum(t.getNum() + 1);
        }
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

    public Map<String, Map<Integer, Integer>> convert(List<Player> players, Map<String, Territory> territories) {
        Map<String, Map<Integer, Integer>> attacks = new HashMap<>();
        for (Player player : players) {
            List<Action> actions = player.getActions();
            for (Action action : actions) {
                if (action.getName().equals("A")) {
                    String start = action.getStart();
                    int num = action.getNum();
                    territories.get(start).setNum(territories.get(start).getNum() - num);
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

