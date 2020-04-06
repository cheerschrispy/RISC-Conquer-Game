package edu.duke.ece651.risc;

import javax.swing.*;
import java.io.Serializable;
import java.util.*;

public class Player implements Serializable {
    private int id;
    private ArrayList<Action> actions;
    public boolean isvalid = true; //for validating
    private int foodResources;
    private int techResources;
    private int techLevel;
    private static int[] costs = new int[] { 0, 50, 75, 125, 200, 300};

    public Player(int id) {
        this.id = id;
        this.actions = new ArrayList<>();
        this.foodResources = 30;
        this.techLevel = 1;
        this.techResources = 50;
    }

    public int getId(){
        return this.id;
    }

    //upgrade tech
    public void upgrade(){
        this.techLevel++;
        this.techResources -= costs[techLevel];
    }

    public int getTechLevel(){
        return this.techLevel;
    }

    public void consumeTech(int tech) {
        this.techResources -= tech;
    }

    public void addTech(int tech) {
        this.techResources += tech;
    }

    public int getFoodResources() {
        return foodResources;
    }

    public void addFood(int food) {
        this.foodResources += food;
    }

    public void consumeFood(int food) {
        this.foodResources -= food;
    }

    public int getTechResources() {
        return techResources;
    }

    public ArrayList<Action> getActions(){
        return this.actions;
    }


    //this will help fill in the arraylist the 3 number
    public void initial_game(Map<String, Territory> territories,Scanner sc,int total,HashMap<String,Integer> init_info,JTextArea promptes){
        Prompts prompts_helper=new Prompts(territories);
        Validator validator=new Validator();
        while(true){
            int total_input=0;
            init_info.clear();
            prompts_helper.gragh_helper(id, true);
            System.out.println("Your territories are as follows,please assign the soldier in each place in display order");
            HashMap<Integer, ArrayList<Territory>> temp=prompts_helper.getGraphInformation();
            for(Territory t : temp.get(id)) {
                System.out.println("How many soldiers do you want to place in "+t.getName()+"(remaing "+(total-total_input)+")");
                //System.out.println("Y");
                String num=sc.nextLine();
                while(!validator.InputNumber_Validate(num)){
                    num=sc.nextLine();
                }

                init_info.put(t.getName(),Integer.parseInt(num));
                total_input+=Integer.parseInt(num);
            }
            if(total_input!=total) System.out.println("Invalid initialization,type again");

            else return;
        }
    }


    public void addAction_afterFail(Map<String, Territory> territories,JTextArea prompts){
        System.out.println("----------YOU FAIL(Watching Mode)------------");
        Prompts prompts_helper=new Prompts(territories);
        this.actions.clear();
        prompts_helper.GraphPrompts();
    }


    //------------------------
    //addAction helper function
    //------------------------
    private String chooseFirstTerritory(Scanner sc,Map<String, Territory> territories){
        Validator validate_helper=new Validator();
        System.out.println("Please assign the source territory of YOURSELF");
        String src = sc.nextLine();
        while (!validate_helper.InputOnwer_Validate(this, src, territories)) {
            src = sc.nextLine();
        }
        return src;
    }
    private String chooseSecondTerritory(Scanner sc,Map<String, Territory> territories,String action){
        String des = "";
        Validator validate_helper=new Validator();
        if (action.equals("M")) {
            System.out.println("Please assign the destination of YOURSELF");
            des = sc.nextLine();
            while (!validate_helper.InputOnwer_Validate(this, des, territories)) {
                des = sc.nextLine();
            }
            //Action user_action = new Action(action, src, des, Integer.parseInt(num_string));
        }

        else if(action.equals("A")) {// it is attack command
            System.out.println("Please assign the destination of ENEMY");
            des = sc.nextLine();
            while (!validate_helper.InputEnemy_Validate(this, des, territories)) {
                des = sc.nextLine();
            }
        }
        return des;
    }


    private ArrayList<Unit> updateCertainlevelSoldier(Scanner sc,Map<String, Territory> territories,String src){
        ArrayList<Unit> ans=new ArrayList<>();
        Territory t=territories.get(src);
        System.out.println("Please type the soldier level you want to assign");
        int level=Integer.parseInt(sc.nextLine());
        /*int num=t.getSoldierNumOfLevel(level);
        while(num<=0){
            System.out.println("you dont have that level soldiers,type again");
            level=Integer.parseInt(sc.nextLine());
            num=t.getSoldierNumOfLevel(level);
        }*/
        System.out.println("How many soldiers in this level do you want to upgrade");
        int numToUpgrade=Integer.parseInt(sc.nextLine());
        for(int i=0;i<numToUpgrade;i++){
            ans.add(new Unit(level));
        }
        return ans;
    }



    private ArrayList<Unit> assignSoldiers(Scanner sc,Map<String, Territory> territories,String src){
        Territory t=territories.get(src);
        ArrayList<Unit> ans=new ArrayList<>();
        for(int i=0;i<7;i++){
            int num=t.getSoldierNumOfLevel(i);
            System.out.println("How many do you want to select in level "+i);
            int toSelect=Integer.parseInt(sc.nextLine());
            for(int j=0;j<toSelect;j++){
                ans.add(new Unit(i));
            }
        }
        return ans;
    }


    public void addAction(Map<String, Territory> territories,String client_name,Scanner sc,JTextArea prompts){
        //the argument is passed by server via socket
        // need to simply verify the user input
        Validator validate_helper=new Validator();
        Prompts prompts_helper=new Prompts(territories);

        this.actions.clear();
        //clear all record in current action list
        while(true) {
            prompts_helper.GraphPrompts();
            prompts_helper.OPtionsPrompts(client_name);
            //add a history to record this round's movement
            prompts_helper.CurrentRoundHistory(this.actions);
            //------------------------
            //take the action first
            //------------------------
            String action = sc.nextLine();
            while (!validate_helper.InputFormat_Validate(action)) {
                action = sc.nextLine();
            }
            if (action.equals("D")) break;
            if(action.equals("T")){
                this.actions.add(new Action(action));
                continue;
            }
            //------------------------
            //take the first territory
            String src=this.chooseFirstTerritory(sc,territories);
            //if it is upgrade unit command
            if(action.equals("U")){
                ArrayList<Unit> toUpgrade=updateCertainlevelSoldier(sc,territories,src);
                System.out.println("Select the level you want to upgrade to");
                prompts.append("Select the level you want to upgrade to");
                int desLevel=Integer.parseInt(sc.nextLine());
                actions.add(new Action(action,src,toUpgrade,desLevel));
                continue;
            }
            //it is attack/move command
            ArrayList<Unit> toAssign=assignSoldiers(sc,territories,src);
            //------------------------
            //take the second territory
            String des = this.chooseSecondTerritory(sc,territories,action);
            this.actions.add(new Action(action,src,des,toAssign));
        }
    }




    public void setActions(Action a){
        //actions.clear();
        actions.add(a);
    }
    public void clearActions(){
        actions.clear();
    }


}
