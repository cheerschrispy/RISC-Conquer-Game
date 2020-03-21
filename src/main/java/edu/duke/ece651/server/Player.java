package edu.duke.ece651.server;

import java.io.Serializable;
import java.util.*;

public class Player implements Serializable {
    private int id;
    private ArrayList<Action> actions;
    //each player has a scanner which get user's input
    //private Scanner sc=new Scanner(System.in);
    public boolean isvalid=true;//for validating
    //private ArrayList<Integer> init_nums;


    public Player(int id) {
        this.id = id;
        this.actions = new ArrayList<>();
        //this.init_nums=new ArrayList<>();
    }

    public int getId(){
        return this.id;
    }

    public ArrayList<Action> getActions(){
        return this.actions;
    }


    //this will help fill in the arraylist the 3 number
    public void initial_game(Map<String, Territory> territories,Scanner sc,int total,HashMap<String,Integer> init_info){
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

    public void addAction_afterFail(Map<String, Territory> territories){
        System.out.println("----------YOU FAIL(Watching Mode)------------");
        Prompts prompts_helper=new Prompts(territories);
        this.actions.clear();
        prompts_helper.GraphPrompts();
    }

    public void addAction(Map<String, Territory> territories,String client_name,Scanner sc){
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
            //
            String action = sc.nextLine();
            while (!validate_helper.InputFormat_Validate(action)) {
                action = sc.nextLine();
            }
            if (action.equals("D")) break;
            System.out.println("Please assign the source territory of YOURSELF");
            String src = sc.nextLine();
            while (!validate_helper.InputOnwer_Validate(this, src, territories)) {
                src = sc.nextLine();
            }
            System.out.println("Please type the soldier number you want to send");
            String num_string = sc.nextLine();
            while (!validate_helper.InputNumber_Validate(num_string)) {
                num_string = sc.nextLine();
            }
            String des = "";
            if (action.equals("M")) {
                System.out.println("Please assign the destination of YOURSELF");
                des = sc.nextLine();
                while (!validate_helper.InputOnwer_Validate(this, des, territories)) {
                    des = sc.nextLine();
                }
            } else {// it is attack command
                System.out.println("Please assign the destination of ENEMY");
                des = sc.nextLine();
                while (!validate_helper.InputEnemy_Validate(this, des, territories)) {
                    des = sc.nextLine();
                }
            }
            //now get all three arguments a Action object need
            Action user_action = new Action(action, src, des, Integer.parseInt(num_string));
            //add some valid action list to it
            this.actions.add(user_action);
        }
    }

}
