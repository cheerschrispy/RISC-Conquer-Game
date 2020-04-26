package edu.duke.ece651.risc;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatThread extends Thread {
    private Socket client;
    private ServerSocket ss;
    ObjectOutputStream os;
    ChatRoom chatRoom;
    ChatThread(Socket client, ServerSocket ss, ChatRoom chatRoom) {
        this.client = client;
        this.ss = ss;
        this.os = null;
        this.chatRoom = chatRoom;
    }

    @Override
    public void run() {
        try {
            this.os = new ObjectOutputStream(client.getOutputStream());
            ObjectInputStream is = new ObjectInputStream(client.getInputStream());
            while (true) {
                Message msg = (Message) is.readObject();
                System.out.println("Got message: " + msg.getContent() + " to " + msg.getReceiver());
                chatRoom.addUser(msg.getSender(), os);
                chatRoom.deliver(msg);
            }
        } catch (Exception e) {
            try {
                client = ss.accept();
                run();
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }
    }
}
