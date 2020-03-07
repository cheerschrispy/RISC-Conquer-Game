package edu.duke.ece651.server;

import java.util.*;

public class Validator {
    //T's name and T's corresponding soldier number
    private HashMap<String, Integer> record = new HashMap<>();

    public boolean InputNumber_Validate(String input){
        for (int i = 0; i < input.length(); i++) {
            if (!Character.isDigit(input.charAt(i))) {
                System.out.println("Please type valid number");
                return false;
            }
        }
        return true;
    }


    public boolean InputFormat_Validate(String input){
        //check if the user's input is valid, i.e. belonging to limited input options
        if(input.equals("M")||input.equals("A")|| input.equals("D")) return true;
        else{
            System.out.println("Invalid input! Please type the first character of each option given!");
        }
        return false;
    }


    public boolean InputOnwer_Validate(Player player, String input,Map<String, Territory> territories){
        //check if the selected territory is player's own one
        Territory t=territories.get(input);
        if(t.getOwner()==player.getId()) return true;
        else{
            System.out.println("It is NOT your territory, please choose again");
        }
        return false;
    }

    public boolean InputEnemy_Validate(Player player,String input, Map<String, Territory> territories){
        //check if the selected territory is player's  enemy one
        Territory t=territories.get(input);
        if(t.getOwner()!=player.getId())return true;
        else{
            System.out.println("It is YOUR territory, please choose your ENEMY's");
        }
        return false;
    }



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
                    player.isvalid=false;
                    return false;
                }
                this.moveChange(num, src, dest);
                if(!this.checkNumber()){
                    player.isvalid=false;
                    return false;
                }
            }
            else{
                //it is Attack command
                if(!this.findEnemy(src, dest, territories)){
                    player.isvalid=false;
                    return false;
                }
                this.attackChange(num, src);
                if(!this.checkNumber()){
                    player.isvalid=false;
                    return false;
                }
            }
        }
        player.isvalid=true;
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
                if(!visited.contains(t) && t.getOwner() == start.getOwner()){
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
