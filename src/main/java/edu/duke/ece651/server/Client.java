package edu.duke.ece651.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

public class Client extends Thread {
    String id;
    Client(String id) {
        this.id = id;
    }
    @Override
    public void run() {
        Socket socket = null;
        try {
            socket = new Socket("localhost", 8080);
            System.out.println("Client Connected");
            ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
            //receive player
            Player player = (Player) is.readObject();
            //receive territories
            Map<String, Territory> territories = (Map<String, Territory>) is.readObject();
            //do something
            player.addAction(new Action("M", id + "1", id + "2", 2));
            //send player
            os.writeObject(player);

            //another round
            Map<String, Territory> newTerritories = (Map<String, Territory>) is.readObject();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try{
                System.out.println("Closing Client Socket");
                socket.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
