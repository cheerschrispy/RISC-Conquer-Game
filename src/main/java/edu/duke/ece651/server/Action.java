package edu.duke.ece651.server;

import java.io.Serializable;

public class Action implements Serializable {
    private String name;
    private String start;
    private String end;
    private int num;

    Action(String name, String start, String end, int num) {
        this.name = name;
        this.start = start;
        this.end = end;
        this.num = num;
    }

    public String getName(){
        return this.name;
    }

    public String getStart(){
        return this.start;
    }

    public String getEnd(){
        return this.end;
    }

    public int getNum(){
        return this.num;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
