package edu.duke.ece651.risc;

import org.junit.jupiter.api.Test;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class TestChat {
    @Test
    public void test() {
        int game_num = 2;
        int player_num = 2;
        for (int i = 0; i < game_num; i++) {
            ChatServer cs = new ChatServer(7999 - i, player_num);
            cs.start();
        }
        for (int i = 0; i < game_num; i++) {
            for (int j = 0; j < player_num; j++) {
                try {
                    Socket socket = new Socket("localhost", 7999 - i);
                    System.out.println("Player " + j + " Connected");
                    ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
                    Sender sender = new Sender(os, j);
                    Receiver receiver = new Receiver(is, j);
                    receiver.start();
                    sender.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

//    public static void main(String[] args) {
//        TestChat cc = new TestChat();
//        cc.test();
//    }


    class Sender extends Thread {
        private ObjectOutputStream os;
        private int id;
        Sender(ObjectOutputStream os, int id) {
            this.os = os;
            this.id = id;
        }
        @Override
        public void run() {
            try {
                Message msg = new Message("Message from "+id, id, 1 - id);
                os.writeObject(msg);
                os.flush();
                os.reset();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    class Receiver extends Thread {
        private ObjectInputStream is;
        private int id;
        Receiver(ObjectInputStream is, int id) {
            this.is = is;
            this.id = id;
        }
        @Override
        public void run() {
            try {
                Message msg1 = (Message) is.readObject();
                System.out.println("Player " + id + " Received: " + msg1.getContent());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
