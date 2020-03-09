package edu.duke.ece651.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class Server {
    private Map<String, Territory> territories;
    private List<Player> players;
    private Executor executor;
    private static final int port = 8080;
    private final int player_num=1;
    private ServerSocket ss = null;


    Server() {
        this.territories = new HashMap<>();
        this.players = new ArrayList<>();
        this.executor = new Executor();
    }

    public void run() {
        createTerritories();
        createPlayers();
        System.out.println("Territory is "+this.territories);
        System.out.println("player is "+this.players);
        try {
            ss = new ServerSocket(port);
            System.out.println("Starting Server on Port " + port);
            Socket[] clients = new Socket[player_num];
            //send player & territories to each player
            for (int i = 0; i < player_num; i++) {
                clients[i] = ss.accept();
                System.out.println("Connected to player " + i);
                ObjectOutputStream os1 = new ObjectOutputStream(clients[i].getOutputStream());

                os1.writeObject(players.get(i));
                os1.flush();
                os1.writeObject(territories);
                os1.flush();
            }
            //receive player & send territories to each player
            while (executor.checkWin(territories)) {
                players = new ArrayList<>();
                //receive player
                Receiver[] receivers = new Receiver[player_num];
                for (int i = 0; i < player_num; i++) {
                    receivers[i] = new Receiver(clients[i], i);
                    receivers[i].start();// get the correct actions list from user
                }

                for (Thread thread : receivers) {
                    thread.join();
                }

                //operate on map and send map back
                executor.execute(players, territories);
                for (int i = 0; i < player_num; i++) {
                    ObjectOutputStream os3 = new ObjectOutputStream(clients[i].getOutputStream());
                    os3.writeObject(territories);
                    os3.flush();
                }
            }

            //after every client close,it will close

            for(int i=0;i<player_num;i++) {
                BufferedReader end_signal = new BufferedReader(new InputStreamReader(clients[i].getInputStream()));
                String line = end_signal.readLine();
                if (line.equals("exit")) {
                    System.out.println("Player " + i + " exits");
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try{
                System.out.println("Closing Server Socket");
                ss.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }

    }

    private void createTerritories() {
        territories.put("a1", new Territory("a1", 0, 3));
        territories.put("a2", new Territory("a2", 0, 3));
        territories.put("a3", new Territory("a3", 0, 3));
        //territories.put("b1", new Territory("b1", 1, 3));
        //territories.put("b2", new Territory("b2", 1, 3));
        //territories.put("b3", new Territory("b3", 1, 3));
        //territories.put("c1", new Territory("c1", 2, 3));
        //territories.put("c2", new Territory("c2", 2, 3));
        //territories.put("c3", new Territory("c3", 2, 3));
        connect("a1", "a2");
        connect("a2", "a3");
        /*connect("b1", "b2");
        connect("b2", "b3");
        connect("c1", "c2");
        connect("c2", "c3");
        connect("a1", "b1");
        connect("b1", "c1");
        connect("a2", "b2");
        connect("b2", "c2");
        connect("a3", "b3");
        connect("b3", "c3");*/
    }

    private void connect(String t1, String t2) {
        territories.get(t1).connect(territories.get(t2));
        territories.get(t2).connect(territories.get(t1));
    }

    private void createPlayers() {
        for (int i = 0; i < player_num; i++) {
            Player player = new Player(i);
            players.add(player);
        }
    }

    class Receiver extends Thread {
        Socket client;
        int id;
        Receiver(Socket socket, int id) {
            this.client = socket;
            this.id = id;
        }
        @Override
        public void run() {
            try {
                ObjectOutputStream os2 = new ObjectOutputStream(client.getOutputStream());
                ObjectInputStream is1 = new ObjectInputStream(client.getInputStream());
                Player player = (Player) is1.readObject();
                Validator validator = new Validator();
                //there is collision, send player object back to player
                while (!validator.validate(player, territories)) {
                    os2.writeObject(player);
                    os2.flush();
                    player = (Player) is1.readObject();
                }
                //send the correct player whose flag is true;
                os2.writeObject(player);
                os2.flush();
                //until the new input from user is valid
                players.add(player);
                //close the resources

            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static void main() throws IOException, InterruptedException {
        Server server = new Server();
        server.run();
    }

}
