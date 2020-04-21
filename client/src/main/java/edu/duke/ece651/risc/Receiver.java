package edu.duke.ece651.risc;

import java.io.ObjectInputStream;

class Receiver extends Thread {
    private ObjectInputStream is;
    private int id;
    savedText savedText;
    Receiver(ObjectInputStream is, int id, savedText savedText) {
        this.is = is;
        this.id = id;
        this.savedText = savedText;
    }
    @Override
    public void run() {
        try {
            while (true) {
                Message msg = (Message) is.readObject();
                savedText.addChat(msg.getContent());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}