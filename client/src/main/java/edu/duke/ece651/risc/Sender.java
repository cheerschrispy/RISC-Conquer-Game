package edu.duke.ece651.risc;

import java.io.ObjectOutputStream;

class Sender extends Thread {
    private ObjectOutputStream os;
    private int id;
    private int target;
    private String text;
    Sender(ObjectOutputStream os, int id) {
        this.os = os;
        this.id = id;
    }
    void setMsg(String text, int target) {
        this.text = text;
        this.target = target;
    }
    @Override
    public void run() {
        try {
            Message msg = new Message(this.text, id, this.target);
            os.writeObject(msg);
            os.flush();
            os.reset();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
