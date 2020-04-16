package edu.duke.ece651.risc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatRoom extends Thread {
    private int port;
    private int player_num;
    ChatRoom(int port, int player_num) {
        this.port = port;
        this.player_num = player_num;
    }

    @Override
    public synchronized void start() {
        try {
        ServerSocket ss = new ServerSocket(port);
        System.out.println("Starting ChatRoom on Port " + port);
        for (int i = 0; i < player_num; i++) {
            Socket client = ss.accept();
            System.out.println("Player " + i + " joined the chat");
            ChatThread chatThread = new ChatThread(client, ss);
            chatThread.start();
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
