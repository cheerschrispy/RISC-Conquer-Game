package edu.duke.ece651.server;

import java.util.*;

public class Validator {
    private HashMap<String, Integer> record = new HashMap<>();

    //Check whether the actions of this player is valid
    public boolean validate(Player player, Map<String, Territory> territories) {
        initializeRecord(player, territories);
        for(Action a: player.getActions()){
            String action = a.getName();
            String src = a.getStart();
            String dest = a.getEnd();
            int num = a.getNum();

            if(action.equals("M")){
                if(!this.BFS(src, dest, territories)){
                    return false;
                }
                this.moveChange(num, src, dest);
                if(!this.checkNumber()){
                    return false;
                }
            }
            else{
                if(!this.findEnemy(src, dest, territories)){
                    return false;
                }
                this.attackChange(num, src);
                if(!this.checkNumber()){
                    return false;
                }
            }
        }
        return true;
    }

    //Initialize the record of the number in each territory
    public void initializeRecord(Player player, Map<String, Territory> territories){
        int playerId = player.getId();
        for(String key : territories.keySet()){
            if(territories.get(key).getOwner() == playerId){
                record.put(territories.get(key).getName(), territories.get(key).getNum());
            }
        }
    }

    //Check whether the number is still larger or equal than 0
    private boolean checkNumber(){
        for(String key : record.keySet()){
            if(record.get(key) < 0){
                return false;
            }
        }
        return true;
    }

    //Search whether these two territories are connected
    private boolean BFS(String src, String dest, Map<String, Territory> territories){
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
                    if(t.getName().equals(dest)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //Change the record
    private void moveChange(int i, String src, String dest){
        int t1 = this.record.get(src);
        int t2 = this.record.get(dest);

        this.record.remove(src);
        this.record.remove(dest);

        this.record.put(src, t1 - i);
        this.record.put(dest, t2 + i);
    }

    //Find whether the enemy is connected
    private boolean findEnemy(String src, String dest, Map<String, Territory> territories){
        Territory start = territories.get(src);
        for(Territory t : start.getNeighbors()){
            if(t.getName().equals(dest)){
                return true;
            }
        }
        return false;
    }

    //Change the record
    private void attackChange(int i, String src){
        int t1 = this.record.get(src);
        this.record.remove(src);
        this.record.put(src, t1 - i);
    }
}
