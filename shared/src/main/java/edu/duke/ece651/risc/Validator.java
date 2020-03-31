package edu.duke.ece651.risc;

import java.util.*;

public class Validator {
    //T's name and T's corresponding soldier number
    private HashMap<String, ArrayList<Integer>> record;
    private int foodRecord;
    private int techRecord;
    private static int[] upLevelCost= {50, 75, 125, 200, 300};
    private static int[] upSoldierCost= {0, 3, 11, 30, 55, 90, 140};

    public Validator(){
        this.foodRecord = 0;
        this.techRecord = 0;
        this.record = new HashMap<>();
    }

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
        for(String name : territories.keySet()){
           if(name.equals(input)){
               Territory t=territories.get(input);
               if(t.getOwner()==player.getId()) return true;
               else{
                   System.out.println("It is NOT your territory, please choose again");
               }
               return false;
           }
        }
        System.out.println("Your input is not valid territory name");
        return false;
    }

    public boolean InputEnemy_Validate(Player player,String input, Map<String, Territory> territories){
        //check if the selected territory is player's  enemy one
        for(String name : territories.keySet()){
            if(name.equals(input)){
                Territory t=territories.get(input);
                if(t.getOwner()!=player.getId()) return true;
                else{
                    System.out.println("It is NOT your enemy territory, please choose again");
                }
                return false;
            }
        }
        System.out.println("Your input is not valid territory name");
        return false;
    }



    //Check whether the actions of this player is valid
    public boolean validate(Player player, Map<String, Territory> territories) {
        initializeRecord(player, territories);
        for(Action a: player.getActions()){
            String action = a.getName();
            String src = a.getStart();
            String dest = a.getEnd();
            ArrayList<Unit> soldiers = a.getSoldiers();

            if(action.equals("M")){
                int step = this.BFS(src, dest, territories);
                if(step == -1){
                    player.isvalid = false;
                    System.out.println("BFS error");
                    return false;
                }
                this.foodRecord += soldiers.size() * territories.get(src).getSize();
                this.moveChange(soldiers, src, dest);
                if(!this.checkNumber()){
                    player.isvalid = false;
                    System.out.println("cannot moveChange due to number of units");
                    return false;
                }
                if(foodRecord > player.getFoodResources()){
                    player.isvalid = false;
                    System.out.println("cannot moveChange due to lack of food");
                    return false;
                }
            }
            else if(action.equals("A")){
                //it is Attack command
                if(!this.findEnemy(src, dest, territories)){
                    player.isvalid = false;
                    System.out.println("cannot find enemy");
                    return false;
                }
                this.foodRecord += soldiers.size();
                this.attackChange(soldiers, src);
                if(!this.checkNumber()){
                    player.isvalid = false;
                    System.out.println("check number error");
                    return false;
                }
                if(foodRecord > player.getFoodResources()){
                    player.isvalid = false;
                    System.out.println("cannot moveChange due to lack of food");
                    return false;
                }
            }
            else if(action.equals("U1")){
                int currLevel = player.getTechLevel();
                if(currLevel == 6){
                    continue;
                }
                else{
                    this.techRecord += upLevelCost[currLevel];
                }
                if(techRecord > player.getTechResources()){
                    player.isvalid = false;
                    System.out.println("cannot update tech due to lack of TechResources");
                    return false;
                }
            }
            else{
                int requestLevel = a.getLevels();
                int num = soldiers.size();
                int currLevel = soldiers.get(0).getLevel();
                if(record.get(src).get(currLevel) < num){
                    player.isvalid = false;
                    System.out.println("cannot update soldiers due to lack of numbers");
                    return false;
                }
                else if(player.getTechLevel() < requestLevel){
                    player.isvalid = false;
                    System.out.println("Maximum tech level is not allowed");
                    return false;
                }

                techRecord += num * (upSoldierCost[requestLevel] - upSoldierCost[currLevel]);
                if(techRecord > player.getTechResources()){
                    player.isvalid = false;
                    System.out.println("cannot update soldiers due to lack of TechResources");
                    return false;
                }
            }
        }
        player.isvalid = true;
        return true;
    }

    //Initialize the record of the number in each territory
    public void initializeRecord(Player player, Map<String, Territory> territories){
        int playerId = player.getId();
        for(String key : territories.keySet()){
            if(territories.get(key).getOwner() == playerId){
                ArrayList<Integer> numbers = new ArrayList<>();
                numbers.add(0);
                numbers.add(0);
                numbers.add(0);
                numbers.add(0);
                numbers.add(0);
                numbers.add(0);
                record.put(territories.get(key).getName(), numbers);
                ArrayList<Unit> units = territories.get(key).getSoldiers();
                for(Unit u : units){
                    int level = u.getLevel();
                    int curr = numbers.get(level);
                    numbers.set(level, 1 + curr);
                }
            }
        }
    }

    //Check whether the number is still larger or equal than 0
    private boolean checkNumber(){
        for(String key : record.keySet()){
            ArrayList<Integer> numbers = record.get(key);
            for(int i = 0; i < 6; i++){
                if(numbers.get(i) < 0){
                    return false;
                }
            }
        }
        return true;
    }

    //Search whether these two territories are connected
    private int BFS(String src, String dest, Map<String, Territory> territories){
        Queue<Territory> q = new LinkedList<>();
        HashSet<Territory> visited = new HashSet<>();
        Territory start = territories.get(src);
        int step = 0;
        q.offer(start);
        visited.add(start);

        while(!q.isEmpty()){
            Territory curr = q.peek();
            q.poll();
            step++;
            for(Territory t : curr.getNeighbors()){
                if(!visited.contains(t) && t.getOwner() == start.getOwner()){
                    q.offer(t);
                    visited.add(t);
                    if(t.getName().equals(dest)){
                        return step;
                    }
                }
            }
        }
        return -1;
    }

    //Change the record
    private void moveChange(ArrayList<Unit> soldiers, String src, String dest){
        ArrayList<Integer> r1 = this.record.get(src);
        ArrayList<Integer> r2 = this.record.get(dest);

        for(Unit u: soldiers){
            int level = u.getLevel();

            int srcNum = r1.get(level);
            int destNum = r2.get(level);
            r1.set(level, srcNum - 1);
            r2.set(level, destNum + 1);
        }
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
    private void attackChange(ArrayList<Unit> soldiers, String src){
        ArrayList<Integer> r1 = this.record.get(src);

        for(Unit u: soldiers){
            int level = u.getLevel();

            int srcNum = r1.get(level);
            r1.set(level, srcNum - 1);
        }
    }

    public int foodConsumed(){
        return this.foodRecord;
    }

    public int techConsumed(){
        return this.techRecord;
    }
}

