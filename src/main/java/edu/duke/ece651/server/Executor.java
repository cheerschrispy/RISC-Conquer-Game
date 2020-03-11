package edu.duke.ece651.server;

import java.util.*;

public class Executor {
    
    private ForcePair selectAttacker(Map<Integer, Integer> attackSquads) {
    
        Integer[] keyArray = attackSquads.keySet().toArray(new Integer[0]); // Store all keys in an array
        Random rand = new Random(System.currentTimeMillis());

        // Generate random integers in range 0 to keyArray size
        int rand_index = rand.nextInt(keyArray.length);
        int squad_id = keyArray[rand_index];
        int soldier_num = attackSquads.remove(squad_id); // Remove from attackSquads
        // System.out.println("Selecting attacker ... Squad " + squad_id 
        //                                        + "! (" + soldier_num + " soldiers)");
    
        ForcePair out = new ForcePair(squad_id, soldier_num);

        return out;
    }

    private ForcePair deployDefender(Territory terr) {
  
        ForcePair out = new ForcePair(terr.getOwner(), terr.getNum());
        return out;
    }

    private ForcePair combat(ForcePair attacker, ForcePair defender) {
    
        Random rand = new Random(System.currentTimeMillis());
        while (attacker.getSoldier() != 0 && defender.getSoldier() != 0) {
            int attDice = rand.nextInt();
            int defDice = rand.nextInt();
            // System.out.println("Attacker (owned by " + attacker.getOwner() + 
            //                        ") current: " + attacker.getSoldier() + " soldiers");
            // System.out.println("Defender (owned by " + defender.getOwner() + 
            //                        ") current: " + defender.getSoldier() + " soldiers");
            // System.out.println("Attacker roll dice: " + attDice);
            // System.out.println("Defender roll dice: " + defDice);
            if (attDice <= defDice) {
                attacker.lossFight();
                // System.out.println("Attacker lost a fight!");
            } 
            else {
                defender.lossFight();
                // System.out.println("Defender lost a fight!");
            }
        }

        if (attacker.getSoldier() == 0) {
            // System.out.println("Defender won the combat!");
            return defender;
        } 
        else {
            // System.out.println("Attacker won the combat!");
            return attacker;
        }
  }

    private void assertDomination(ForcePair survivor, Territory terr) {
     
        // System.out.println(terr.getName() + "'s new owner is " + survivor.getOwner() + 
        //                            " with " + survivor.getSoldier() + " soldiers");
        terr.setOwner(survivor.getOwner());
        terr.setNum(survivor.getSoldier());
    }

    public void attack(Map<String, Map<Integer, Integer>> attacks, Map<String, Territory> territories) {
        Set<String> battleFields = attacks.keySet(); // Get the names of all lands

        // Loop through each land
        for (String currBFD : battleFields) { // For each current battle field
            // System.out.println("Current battle field is: " + currBFD + "!");
            Map<Integer, Integer> attackSquads = attacks.get(currBFD);
            Territory currTerr = territories.get(currBFD);
            // System.out.println(currBFD + " is owned by: " + currTerr.getOwner() 
            //                             + " with " + currTerr.getNum() + " soldiers.");
            ForcePair attacker = new ForcePair();
            ForcePair defender = new ForcePair();

            defender = deployDefender(currTerr);
            // Loop until one survivor
            while (!attackSquads.isEmpty()) {
                attacker = selectAttacker(attackSquads); // Select and remove a attacker from attackSquads
                defender = combat(attacker, defender); // One that survive become new defender
            }
            assertDomination(defender, currTerr); // Write combat result in currTerr
        }

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

