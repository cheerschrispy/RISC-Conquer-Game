/*package edu.duke.ece651.server;

import java.util.ArrayList;
import java.util.HashMap;

public class Client {
    //get them from the server
    private Player player;
    //key is player id, in convenience for display the relevant info
    private HashMap<Integer, ArrayList<Territory>> graphInformation;


*/

package edu.duke.ece651.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Client extends Thread {
    String name;
    private Scanner sc=new Scanner(System.in);
    private ObjectOutputStream os1 = null;
    private ObjectInputStream is1 = null;
    final int totalsoldier=9;
    Client(String client_name) {
        this.name = client_name;
    }

    public String get_name(){
        return this.name;
    }
    @Override
    public void run() {
        Socket socket = null;
        Executor end_helper=new Executor();
        try {
            socket = new Socket("localhost", 8000);
            System.out.println("Client Connected");
            this.os1 = new ObjectOutputStream(socket.getOutputStream());
            this.is1 = new ObjectInputStream(socket.getInputStream());



            //receive player
            Player player = (Player) is1.readObject();
            //receive map to be completed
            Map<String, Territory> territories = (Map<String, Territory>) is1.readObject();
//new Added!
            //fill the map in player
            HashMap<String,Integer> init_info=new HashMap<>();
            player.initial_game(territories,sc,totalsoldier,init_info);
            //send it to server
            os1.writeObject(init_info);
            os1.flush();
            os1.reset();
            //wait server send back completed map
            territories = (Map<String, Territory>) is1.readObject();
            //now game begins
            while(true) {
                player.addAction(territories, get_name(),sc);
                //send player to server
                os1.writeObject(player);
                os1.flush();
                os1.reset();
                //waiting for server's reply of validating
                while (true) {
                    System.out.println("receiving the player object from server");
                    Player temp = (Player) is1.readObject();
                    if (temp.isvalid) {
                        System.out.println("input is valid");
                        break;
                    }
                    System.out.println("Collision actions,type again");
                    player.addAction(territories, get_name(),sc);
                    os1.writeObject(player);
                    os1.flush();
                    os1.reset();
                }
                territories = (Map<String, Territory>) is1.readObject();
                if(!end_helper.checkWin(territories)) break;
            }


            //tell server it will close the client
            PrintWriter pw = new PrintWriter(socket.getOutputStream());
            String msg="exit";
            pw.println(msg);
            pw.flush();

            //close resources
            pw.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try{
                System.out.println("Closing Client Socket");
                assert socket != null;
                socket.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

}










