package edu.duke.ece651.risc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer extends Thread {
    private int port;
    private int player_num;
    ChatServer(int port, int player_num) {
        this.port = port;
        this.player_num = player_num;
    }

    @Override
    public void run() {
        try {
        ServerSocket ss = new ServerSocket(port);
        System.out.println("Starting ChatRoom on Port " + port);
        ChatRoom chatRoom = new ChatRoom(player_num);
        for (int i = 0; i < player_num; i++) {
            Socket client = ss.accept();
            System.out.println("Player " + i + " joined the chat");
            ChatThread chatThread = new ChatThread(client, ss, chatRoom);
            chatThread.start();
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
