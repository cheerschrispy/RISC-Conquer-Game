package edu.duke.ece651.server;

public class Action {
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
}
