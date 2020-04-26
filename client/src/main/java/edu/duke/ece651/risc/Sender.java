package edu.duke.ece651.risc;

import java.io.ObjectOutputStream;

class Sender {
    private ObjectOutputStream os;
    private int id;
    private int target;
    private String text;
    Sender(ObjectOutputStream os, int id) {
        this.os = os;
        this.id = id;
        try {
            Message msg = new Message("", id, -1);
            os.writeObject(msg);
            os.flush();
            os.reset();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void setMsg(String text, int target) {
        this.text = text;
        this.target = target;
    }
    public void start() {
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
