package edu.duke.ece651.risc;

public class savedText {
    private String actionHistory;
    private String chattingHistory;

    public savedText(){
        this.actionHistory="Current Round History:\n\n";
        this.chattingHistory="Chatting History:\n\n";
    }

    public String getActionHistory(){
        return this.actionHistory;
    }
    public String getChattingHistory(){
        return this.actionHistory;
    }
    public void clearActionHistory(){
        this.actionHistory="Current Round History:\n\n";
    }
    public void addAction(String action){
        this.actionHistory+=action;
        this.actionHistory+="-------------------\n";
    }

    public void addChat(){

    }
}
