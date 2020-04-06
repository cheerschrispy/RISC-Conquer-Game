package edu.duke.ece651.risc;

import javax.swing.*;
import java.util.*;

public class Prompts {
    //key is player id, value is corresponding own territories
    private HashMap<Integer, ArrayList<Territory>> graphInformation = new HashMap<>();
    ///private JTextArea promptsArea;

    public Prompts(Map<String, Territory> territories){
        this.getInformation(territories);
        //this.promptsArea=prompts;

    }

    public HashMap<Integer, ArrayList<Territory>> getGraphInformation(){
        return this.graphInformation;
    }

    public void gragh_helper(int playerId,boolean isinitial){
        StringBuilder b = new StringBuilder();
        b.append("player ").append(playerId).append(":\n--------------------------\n");
        for(Territory t : this.graphInformation.get(playerId)){
            int number = t.getSoldiers().size();
            String name = t.getName();
            StringBuilder neighbor = new StringBuilder();
            int i = 0;
            for(Territory n : t.getNeighbors()){
                if(i != 0){
                    neighbor.append(", ");
                }
                neighbor.append(n.getName());
                i++;
            }
            if(!isinitial)
                b.append(number).append(" units in ").append(name).append(" (next to : ").append(neighbor).append(")\n");
            else b.append(name).append(" (next to : ").append(neighbor).append(")\n");
        }
        System.out.println(b);
        //this.promptsArea.append(b+"\n");
    }

    public void GraphPrompts(){
        //territories is the info got from server end(contains whole info of the graph)
        //this.getInformation(territories);
        for(int playerId : this.graphInformation.keySet()){
            gragh_helper(playerId,false);
        }
    }


    public void CurrentRoundHistory(ArrayList<Action> actions){
        System.out.println("***This round's movement(Press D to commit your change)***");
        int index=1;
        for(Action a :actions){
            if(a.getName().equals("M")){
                System.out.println(index+": Move "+a.getSoldiers().size()+" soldiers From "+a.getStart()+" To "+a.getEnd());}
            if(a.getName().equals("A")){
                System.out.println(index+": Attack "+a.getEnd()+" From "+a.getStart()+" With "+a.getSoldiers().size()+" soldiers");
            }
            index++;
        }
        System.out.println("**********************************************************");

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
        //
        //this.promptsArea.append("You are the "+client_name+" player, what would you like to do?");
        //this.promptsArea.append("M(ove)");
        //this.promptsArea.append("A(ttack)");
        //this.promptsArea.append("D(one)");
    }


}
