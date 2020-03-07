package edu.duke.ece651.server;

import java.util.*;

public class Prompts {
    //key is player id, value is corresponding own territories
    private HashMap<Integer, ArrayList<Territory>> graphInformation = new HashMap<>();


    public void GraphPrompts(Map<String, Territory> territories){
        //territories is the info got from server end(contains whole info of the graph)
        this.getInformation(territories);
        for(int playerId : this.graphInformation.keySet()){
            StringBuilder b = new StringBuilder();
            b.append("player ").append(playerId).append(":\n--------------------------\n");
            for(Territory t : this.graphInformation.get(playerId)){
                int number = t.getNum();
                String name = t.getName();
                String neighbor = null;
                int i = 0;
                for(Territory n : t.getNeighbors()){
                    if(i != 0){
                        neighbor += ", ";
                    }
                    neighbor += n.getName();
                    i++;
                }
                b.append(number).append(" units in ").append(name).append(" (next to : ").append(neighbor).append(")\n");
            }
            System.out.println(b);
            System.out.println("\n\n");
        }

    }

    public void getInformation(Map<String, Territory> territories){
        for(String key : territories.keySet()){
            Territory t = territories.get(key);
            int playerId = t.getOwner();
            if(this.graphInformation.containsKey(playerId)){
                this.graphInformation.get(playerId).add(t);
            }
            else{
                ArrayList<Territory> temp = new ArrayList<>();
                temp.add(t);
                this.graphInformation.put(playerId, temp);
            }
        }
    }


    public void OPtionsPrompts(String client_name){
        System.out.println("You are the "+client_name+" player, what would you like to do?");
        System.out.println("M(ove)");
        System.out.println("A(ttack)");
        System.out.println("D(one)");
    }


}
