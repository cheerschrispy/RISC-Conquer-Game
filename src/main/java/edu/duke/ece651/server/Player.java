package edu.duke.ece651.server;

import java.io.Serializable;
import java.util.*;

public class Player implements Serializable {
    private int id;
    private List<Action> actions;
    //each player has a scanner which get user's input
    //private Scanner sc=new Scanner(System.in);
    public boolean isvalid=true;//for validating


    public Player(int id) {
        this.id = id;
        this.actions = new ArrayList<>();
    }

    public int getId(){
        return this.id;
    }

    public List<Action> getActions(){
        return this.actions;
    }

    public void addAction(Map<String, Territory> territories,String client_name,Scanner sc){
        //the argument is passed by server via socket
        // need to simply verify the user input
        Validator validate_helper=new Validator();
        Prompts prompts_helper=new Prompts(territories);
        //prompts_helper.OPtionsPrompts(client_name);

        actions.clear();//clear all record in current action list
        while(true) {
            prompts_helper.GraphPrompts();
            prompts_helper.OPtionsPrompts(client_name);
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
            actions.add(user_action);
        }
    }

}
