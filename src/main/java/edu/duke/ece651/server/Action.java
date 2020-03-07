package edu.duke.ece651.server;

public class Action {
    private String name;//Move or Attack
    private String start;//start territory name
    private String end;
    private int num;//num of soldier

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
