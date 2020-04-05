package edu.duke.ece651.risc;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;


public class Server {
    public  Map<String, Territory> territories;
    private List<Player> players;
    private Executor executor;
    private static final int port = 8000;
    private final int player_num=2;
    private ServerSocket ss = null;
    private ArrayList<ObjectOutputStream> os;
    private ArrayList<ObjectInputStream> is;


    Server() {
        this.territories = new HashMap<>();
        this.players = new ArrayList<>();
        this.executor = new Executor();
        this.os=new ArrayList<>();
        this.os=new ArrayList<>();
        this.is=new ArrayList<>();

    }

    public void run() {
        //------------------no need to set number for now------------
        createTerritories();
        createPlayers();

        //System.out.println("Territory is "+this.territories);
        //System.out.println("player is "+this.players);
        try {
            ss = new ServerSocket(port);
            System.out.println("Starting Server on Port " + port);
            Socket[] clients = new Socket[player_num];

            initialSend(clients);

            updatePeople(clients);

            play(clients);
            endGame(clients);

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

    //after every client close,it will close
    private void endGame(Socket[] clients) throws IOException {
        for(int i=0;i<player_num;i++) {
            BufferedReader end_signal = new BufferedReader(new InputStreamReader(clients[i].getInputStream()));
            String line = end_signal.readLine();
            if (line.equals("exit")) {
                System.out.println("Player " + i + " exits");
            }
        }
    }

    //receive player & send territories to each player
    private void play(Socket[] clients) throws InterruptedException, IOException {
        Receiver[] receivers = new Receiver[player_num];
        while (!executor.checkWin(territories)) {
            //players = new ArrayList<>();
            System.out.println("new around");
            players.clear();
            for (int i = 0; i < player_num; i++) {
                receivers[i] = new Receiver(clients[i], i,this.os.get(i),this.is.get(i));
                //get the correct actions list from user
                receivers[i].start();

            }

            for (Thread thread : receivers) {
                thread.join();
            }

            mapSoldiers(players);

            //operate on map and send map back
            executor.execute(players, territories);
            for (int i = 0; i < player_num; i++) {
                for (Player player : players) {
                    if (player.getId() == i) {
                        os.get(i).writeObject(player);
                        os.get(i).flush();
                        os.get(i).reset();
                        break;
                    }
                }
                os.get(i).writeObject(territories);
                os.get(i).flush();
                os.get(i).reset();
                System.out.println("new territory is ");
                Prompts p=new Prompts(territories);
                p.GraphPrompts();
            }

            System.out.println("sent back new terri");
        }
    }

    private void mapSoldiers(List<Player> players) {
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
                    for (Unit unit : territory.getSoldiers()) {
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

    //update people in territories based on user input
    private void updatePeople(Socket[] clients) throws InterruptedException, IOException {
        Initializer inits[] = new Initializer[player_num];
        for (int i = 0; i < player_num; i++) {
            inits[i] = new Initializer(clients[i], i,this.os.get(i),this.is.get(i));
            inits[i].start();
        }

        for (Thread thread : inits) {
            thread.join();
        }

        for (int i = 0; i < player_num; i++) {
            os.get(i).writeObject(territories);
            os.get(i).flush();
            os.get(i).reset();
        }
    }

    //send player & territories to each player
    private void initialSend(Socket[] clients) throws IOException {
        for (int i = 0; i < player_num; i++) {
            clients[i] = ss.accept();
            System.out.println("Connected to player " + i);
            ObjectOutputStream os1 = new ObjectOutputStream(clients[i].getOutputStream());
            ObjectInputStream is1 = new ObjectInputStream(clients[i].getInputStream());
            this.os.add(os1);
            this.is.add(is1);

            os1.writeObject(players.get(i));
            os1.flush();
            os1.reset();
            //------at this point,it sends a incompleted map-------------
            os1.writeObject(territories);
            os1.flush();
            os1.reset();
        }
    }

    public void createTerritories() {
        Territory[][] matrix = new Territory[player_num][3];
        for (int i = 0; i < player_num; i++) {
            for (int j = 0; j < 3; j++) {
                String tName = (char) ('a' + i) + String.valueOf(j);
                Territory newTerritory = new Territory(tName, i);
                territories.put(tName, newTerritory);
                matrix[i][j] = newTerritory;
                if (j > 0) {
                    connect(newTerritory, matrix[i][j - 1]);
                }
                if (i > 0) {
                    connect(newTerritory, matrix[i - 1][j]);
                }
            }
        }
    }

    private void connect(Territory t1, Territory t2) {
        t1.connect(t2);
        t2.connect(t1);
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
        ObjectOutputStream os1;
        ObjectInputStream is1;
        Receiver(Socket socket, int id,ObjectOutputStream os1,ObjectInputStream is1) {
            this.client = socket;
            this.id = id;
            this.os1=os1;
            this.is1=is1;
        }
        @Override
        public void run() {
            try {
                Player player = (Player) this.is1.readObject();
                Validator validator = new Validator();
                //there is collision, send player object back to player
                while (!validator.validate(player, territories)) {
                    os1.writeObject(player);
                    os1.flush();
                    os1.reset();
                    player = (Player) this.is1.readObject();
                }
                System.out.println("input is valid");
                //send the correct player whose flag is true;
                //now the new input from user is valid
                players.add(player);
                //this is for debugging
                //System.out.println("valid action list is ");
                //player.showActionContent();
                //close the resources

            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class Initializer extends Thread {
        Socket client;
        int id;
        ObjectOutputStream os1;
        ObjectInputStream is1;
        Initializer(Socket socket, int id,ObjectOutputStream os1,ObjectInputStream is1) {
            this.client = socket;
            this.id = id;
            this.os1=os1;
            this.is1=is1;
        }
        @Override
        public void run() {
            try {
                HashMap<String,Integer> map = (HashMap<String,Integer>) this.is1.readObject();
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

    public static void main(String[] args) throws Exception {
        Server server = new Server();
        server.run();
    }
}
