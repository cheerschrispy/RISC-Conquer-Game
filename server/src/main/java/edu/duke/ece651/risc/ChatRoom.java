package edu.duke.ece651.risc;

import java.io.ObjectOutputStream;

public class ChatRoom {
    private ObjectOutputStream[] os;
    ChatRoom(int player_num) {
        this.os = new ObjectOutputStream[player_num];
    }
    public void addUser(int sender, ObjectOutputStream os) {
        this.os[sender] = os;
    }
    public void deliver(Message msg) throws Exception {
        try {
            int receiver = msg.getReceiver();
            os[receiver].writeObject(msg);
            os[receiver].flush();
            os[receiver].reset();
            System.out.println("Delivered message: " + msg.getContent());
        } catch (Exception e) {
            Thread.sleep(1000);
            System.out.println("Redeliver message: " + msg.getContent());
            deliver(msg);
        }
    }
}
