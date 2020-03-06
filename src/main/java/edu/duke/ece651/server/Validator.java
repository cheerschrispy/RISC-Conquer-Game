package edu.duke.ece651.server;

import java.util.*;

public class Validator {
    private HashMap<String, Integer> record = new HashMap<>();

    public boolean validate(Player player, Map<String, Territory> territories) {
        return true;
    }

    public void initializeRecord(Player player, Map<String, Territory> territories){
        int playerId = player.getId();
        for(String key : territories.keySet()){
            if(territories.get(key).getOwner() == playerId){
                record.put(territories.get(key).getName(), territories.get(key).getNum());
            }
        }
    }

    private boolean checkNumber(){
        for(String key : record.keySet()){
            if(record.get(key) < 0){
                return false;
            }
        }
        return true;
    }
}
