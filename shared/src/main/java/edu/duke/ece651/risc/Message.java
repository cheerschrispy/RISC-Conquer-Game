package edu.duke.ece651.risc;

import java.io.Serializable;

public class Message implements Serializable {
    private String content;
    private int sender;
    private int receiver;
    Message(String content, int sender, int receiver) {
        this.content = content;
        this.sender = sender;
        this.receiver = receiver;
    }
    public String getContent() {
        return content;
    }

    public int getSender() {
        return sender;
    }

    public int getReceiver() {
        return receiver;
    }
}
