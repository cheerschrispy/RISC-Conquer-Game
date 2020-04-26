package edu.duke.ece651.risc;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GameServer extends Thread {
    public  Map<String, Territory> territories;
    private Player[] players;
    private Executor executor;
    private int port;
    private int player_num;
    private ServerSocket ss = null;
    private ObjectOutputStream[] os;
    private ObjectInputStream[] is;
    private Map<Integer, Credential> credentials;


    GameServer(int port, int player_num) {
        this.territories = new HashMap<>();
        this.players = new Player[player_num];
        this.executor = new Executor();
        this.os = new ObjectOutputStream[player_num];
        this.is = new ObjectInputStream[player_num];
        this.credentials = new HashMap<>();
        this.port = port;
        this.player_num = player_num;
    }

    public void run() {
        //------------------no need to set number for now------------
        createTerritories();
        createPlayers();

        System.out.println("Territory is "+this.territories);
        System.out.println("player is "+this.players);
        try {
            ss = new ServerSocket(port);
            System.out.println("Starting Server on Port " + port);
            Socket[] clients = new Socket[player_num];
            for (int i = 0; i < player_num; i++) {
                clients[i] = ss.accept();
                System.out.println("Connected to player " + i);
                ObjectOutputStream os1 = new ObjectOutputStream(clients[i].getOutputStream());
                ObjectInputStream is1 = new ObjectInputStream(clients[i].getInputStream());
                os[i] = os1;
                is[i] = is1;
            }

            authenticate(clients);

            initialSend(clients);

            updatePeople(clients);

            play(clients);

//            endGame(clients);

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

    //record credentials
    private void authenticate(Socket[] clients) throws IOException, ClassNotFoundException {
        for (int i = 0; i < player_num; i++) {
            Credential credential = (Credential) is[i].readObject();
            credentials.put(i, credential);
            os[i].writeObject(true);
            os[i].flush();
            os[i].reset();
            os[i].writeObject(false);
            os[i].flush();
            os[i].reset();
        }
    }

    //after every client close,it will close
//    private void endGame(Socket[] clients) throws IOException {
//        for(int i=0;i<player_num;i++) {
//            BufferedReader end_signal = new BufferedReader(new InputStreamReader(clients[i].getInputStream()));
//            String line = end_signal.readLine();
//            if (line.equals("exit")) {
//                System.out.println("Player " + i + " exits");
//            }
//        }
//    }

    //receive player & send territories to each player
    private void play(Socket[] clients) throws InterruptedException, IOException {
        Receiver[] receivers = new Receiver[player_num];
        Boolean[] fails = new Boolean[player_num];
        while (executor.checkWin(territories) == -1) {
            //players = new ArrayList<>();
            System.out.println("new around");
            for (int i = 0; i < player_num; i++) {
                if (executor.singlePlayerFail(territories, i)) {
                    if (!fails[i]) {
                        fails[i] = true;
                        sendAll(i);
                    }
                    continue;
                }
                receivers[i] = new Receiver(clients, i);
                //get the correct actions list from user
                receivers[i].start();

            }

            for (Thread thread : receivers) {
                if (thread == null) {
                    continue;
                }
                thread.join();
            }

            mapSoldiers(players);

            //operate on map and send map back
            executor.execute(players, territories);
            for (int i = 0; i < player_num; i++) {
                if (executor.singlePlayerFail(territories, i)) {
                    if (!fails[i]) {
                        fails[i] = true;
                        sendAll(i);
                    }
                    continue;
                }
                try {
                    sendAll(i);
                    System.out.println("new territory is ");
                    Prompts p=new Prompts(territories);
                    p.GraphPrompts();
                } catch (Exception e) {
                    continue;
                }
            }
            System.out.println("sent back");
        }
    }

    private void mapSoldiers(Player[] players) {
        for (Player player : players) {
            for (Action action : player.getActions()) {
                String start = action.getStart();
                ArrayList<Unit> soldiers = action.getSoldiers();
                if (soldiers == null) {
                    break;
                }
                Territory territory = territories.get(start);
                ArrayList<Unit> mappedSoldiers = new ArrayList<>();
                while (!soldiers.isEmpty()) {
                    Unit soldier = soldiers.remove(0);
                    List<Unit> units;
                    if (player.getId() == territory.getOwner()) {
                        units = territory.getSoldiers();
                    } else {
                        units = territory.getAllies().get(player.getId());
                    }
                    for (Unit unit : units) {
                        if (soldier.getLevel() == unit.getLevel() && !mappedSoldiers.contains(unit)) {
                            mappedSoldiers.add(unit);
                            break;
                        }
                    }
                }
                soldiers.addAll(mappedSoldiers);
            }
        }
    }

    private void sendAll(int id) throws IOException {
        os[id].writeObject(players[id]);
        os[id].flush();
        os[id].reset();
        os[id].writeObject(territories);
        os[id].flush();
        os[id].reset();
    }

    //update people in territories based on user input
    private void updatePeople(Socket[] clients) throws InterruptedException, IOException {
        Initializer inits[] = new Initializer[player_num];
        for (int i = 0; i < player_num; i++) {
            inits[i] = new Initializer(clients, i);
            inits[i].start();
        }

        for (Thread thread : inits) {
            thread.join();
        }

        for (int i = 0; i < player_num; i++) {
            os[i].writeObject(territories);
            os[i].flush();
            os[i].reset();
        }
    }

    //send player & territories to each player
    private void initialSend(Socket[] clients) throws IOException {
        for (int i = 0; i < player_num; i++) {
            sendAll(i);
        }
    }

    public void createTerritories() {
        int n = 12/player_num;
        for (int i = 1; i < 13; i++) {
            String tName = "T" + i;
            Territory t = new Territory(tName, (i-1)/n);
            territories.put(tName, t);
        }
        connect("T1", "T5");
        connect("T1", "T10");
        connect("T2", "T5");
        connect("T2", "T3");
        connect("T3", "T4");
        connect("T4", "T6");
        connect("T5", "T6");
        connect("T6", "T7");
        connect("T6", "T9");
        connect("T7", "T8");
        connect("T7", "T11");
        connect("T7", "T12");
        connect("T8", "T9");
        connect("T10", "T11");
        connect("T11", "T12");
        for (Territory t : territories.values()) {
            for (int i = 0; i < player_num; i++) {
                t.getAllies().put(i, new ArrayList<>());
            }
        }
    }

    private void connect(String t1, String t2) {
        territories.get(t1).connect(territories.get(t2));
        territories.get(t2).connect(territories.get(t1));
    }

    private void createPlayers() {
        for (int i = 0; i < player_num; i++) {
            Player player = new Player(i);
            players[i] = player;
        }
    }

    class Receiver extends Thread {
        int id;
        Socket[] clients;
        Receiver(Socket[] clients, int id) {
            this.id = id;
            this.clients = clients;
        }
        @Override
        public void run() {
            try {
                Player player = (Player) is[id].readObject();
                Validator validator = new Validator();
                //there is collision, send player object back to player
                while (!validator.validate(player, territories)) {
                    os[id].writeObject(player);
                    os[id].flush();
                    os[id].reset();
                    player = (Player) is[id].readObject();
                }
                System.out.println("input is valid");
                //send the correct player whose flag is true;
                //now the new input from user is valid
                players[id] = player;
                //this is for debugging
                //System.out.println("valid action list is ");
                //player.showActionContent();
                //close the resources

            } catch (Exception e) {
                reconnect();
                run();
            }
        }

        private void reconnect() {
            try {
                System.out.println("Player " + id + " disconnected");
                clients[id] = ss.accept();
                ObjectOutputStream os1 = new ObjectOutputStream(clients[id].getOutputStream());
                ObjectInputStream is1 = new ObjectInputStream(clients[id].getInputStream());
                os[id] = os1;
                is[id] = is1;
                Credential credential = (Credential) is[id].readObject();
                if (!credentials.get(id).check(credential)) {
                    System.out.println("Wrong credentials");
                    os[id].writeObject(false);
                    os[id].flush();
                    os[id].reset();
                    clients[id].close();
                    reconnect();
                    return;
                }
                System.out.println("Reconnected to player " + id);
                os[id].writeObject(true);
                os1.flush();
                os1.reset();
                os[id].writeObject(true);
                os1.flush();
                os1.reset();
                sendAll(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class Initializer extends Thread {
        int id;
        Socket[] clients;
        Initializer(Socket[] clients, int id) {
            this.id = id;
            this.clients = clients;
        }
        @Override
        public void run() {
            try {
                HashMap<String,Integer> map = (HashMap<String,Integer>) is[id].readObject();
                for (Map.Entry entry : map.entrySet()) {
                    String key = (String) entry.getKey();
                    int value = (int) entry.getValue();
                    territories.get(key).initSoldiers(value);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
