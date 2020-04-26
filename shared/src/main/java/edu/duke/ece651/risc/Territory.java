package edu.duke.ece651.risc;

import java.io.Serializable;
import java.util.*;

public class Territory implements Serializable {
    private String name;
    private int owner;
    private HashMap<Integer, ArrayList<Unit>> allies;
    private ArrayList<Unit> soldiers;
    private Set<Territory> neighbors;
    private int size;
    private int foodProduct;
    private int techProduct;

    public int getFoodProduct() {
        return foodProduct;
    }

    public int getTechProduct() {
        return techProduct;
    }
    
    public ArrayList<Unit> getOwnUnit(int i){
        return allies.get(i);
    }

    public HashMap<Integer, ArrayList<Unit>> getAllies(){
        return this.allies;
    }

    Territory(String name, int owner) {
        this.name = name;
        this.owner = owner;
        this.soldiers = new ArrayList<>();
        this.neighbors = new HashSet<>();
        this.allies = new HashMap<>();
        this.size = 3;
        this.foodProduct = 5;
        this.techProduct = 10;
    }

    public void addFoodPro() {
        this.foodProduct += 5;
    }

    public void addTechPro() {
        this.techProduct += 5;
    }

    //add  other territory to its neighbor list
    public void connect(Territory t) {
        this.neighbors.add(t);
    }

    public int getOwner(){
        return this.owner;
    }

    public String getName(){
        return this.name;
    }

    public Set<Territory> getNeighbors(){
        return this.neighbors;
    }

    public void setOwner(int i){
        this.owner = i;
    }

    public int getSize(){
        return this.size;
    }

    public ArrayList<Unit> getSoldiers() {
        return soldiers;
    }

    public void setSoldiers(ArrayList<Unit> s) {
        this.soldiers = s;
    }

    //get the totoal num of indicated level
    public int getSoldierNumOfLevel(int level){
        int ans=0;
        for(Unit s:this.soldiers){
            if(s.getLevel()==level) ans++;
        }
        return ans;
    }
    //get indicated num of indicated level of soldiers
    public ArrayList<Unit> getSoldierOfLevel(int level, int toadd){
        ArrayList <Unit> ans=new ArrayList<Unit>();
        int total=toadd;
        for(Unit s:this.soldiers){
            if(s.getLevel()==level) {
                ans.add(s);
                total--;
            }
            if(total<=0) break;
        }
        return ans;
    }
    
    //get the total num of indicated level
    public int getAllyNumOfLevel(int level, int id){
        int ans = 0;
        ArrayList<Unit> a = getOwnUnit(id);
        for(Unit s : a){
            if(s.getLevel() ==level)
                ans++;
        }
        return ans;
    }

    //init territory to have num basic soldiers
    public void initSoldiers(int num) {
        for (int i = 0; i < num; i++) {
            soldiers.add(new Unit());
        }
    }
}
