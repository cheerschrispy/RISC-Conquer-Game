package edu.duke.ece651.risc;

public class savedText {
    private String actionHistoryC;
    private  String actionHistoryE;
    private String chattingHistory;

    public savedText(){
        this.actionHistoryE="Current Round History:\n\n";
        this.actionHistoryC = "本回合历史记录:\n\n";
        this.chattingHistory="Chatting History:\n\n";
    }

    public String getActionHistoryC(){
        return this.actionHistoryC;
    }

    public String getActionHistoryE(){
        return this.actionHistoryE;
    }

    public String getChattingHistory(){
        return this.chattingHistory;
    }

    public void clearActionHistoryE(){
        this.actionHistoryE="Current Round History:\n\n";
    }

    public void clearActionHistoryC(){
        this.actionHistoryC="本回合历史记录:\n\n";
    }

    public void addActionC(String action){
        this.actionHistoryC += action;
        this.actionHistoryC += "-------------------\n";
    }

    public void addActionE(String action){
        this.actionHistoryE += action;
        this.actionHistoryE += "-------------------\n";
    }

    public void addChat(String msg){
        this.chattingHistory+=msg;
        this.chattingHistory+="\n";

    }
}
