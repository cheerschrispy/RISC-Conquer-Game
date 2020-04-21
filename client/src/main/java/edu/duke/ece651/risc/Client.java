package edu.duke.ece651.risc;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;
//import java.util.concurrent.Executor;


public class Client extends Application {
    //----------------------------------------
    //-------------Fields--------------------
    //----------------------------------------
    ///basic information
    final int totalSoldiers = 3;
    private Player player=null;
    private Map<String, Territory> territories=null;

    //communication stuffs
    private Scanner sc = new Scanner(System.in);
    private ObjectOutputStream os1 = null;
    private ObjectInputStream is1 = null;
    private Socket socket = null;

    //for display
    private Stage window;
    private savedText savedText=new savedText();
    //--------------------------------------------
    //----------------Socket　Process-------------
    //--------------------------------------------
    private boolean authenticate() throws IOException, ClassNotFoundException {
        //set username & password
        System.out.println("username:");
        String username = sc.nextLine();
        System.out.println("password:");
        String password = sc.nextLine();
        System.out.println("Waiting for others to join...");
        Credential credential = new Credential(username, password);
        //send username & password
        os1.writeObject(credential);
        os1.flush();
        os1.reset();
        //if user exists
        return (boolean) is1.readObject();
    }


    private void connectServer() throws IOException, ClassNotFoundException {
        System.out.println("Choose one game to play (start from 0):");
        int port = Integer.parseInt(sc.nextLine());
        try {
            socket = new Socket("localhost", 8000 + port);
            System.out.println("Client Connected");
        } catch (ConnectException e) {
            System.out.println("Game not available, please choose another game:");
            connectServer();
            return;
        }
    }
    private Map<String, Territory> initMap(Player player, Map<String, Territory> territories) throws IOException, ClassNotFoundException {
        //fill the map in player
        HashMap<String, Integer> init_info = new HashMap<>();
        player.initial_game(territories, sc, totalSoldiers, init_info);

        //send it to server
        os1.writeObject(init_info);
        os1.flush();
        os1.reset();
        //wait server send back completed map
        territories = (Map<String, Territory>) is1.readObject();
        return territories;
    }
    //--------------------------------------------
    //----------------Initialize Game-------------
    //--------------------------------------------

    private void InitializeGame() throws IOException, ClassNotFoundException {
        this.os1 = new ObjectOutputStream(socket.getOutputStream());
        this.is1 = new ObjectInputStream(socket.getInputStream());

        //authenticate user
        boolean exists;
        try {
            exists = authenticate();
        } catch (SocketException e) {
            System.out.println("Authentication failed, try again");
            connectServer();
            return;
        }
        //receive info
        this.player = (Player) is1.readObject();
        //receive map to be completed
        this.territories = (Map<String, Territory>) is1.readObject();
        //initialize map if user is new
        if (!exists) {
            territories = initMap(player, territories);
        }
    }
    //show the man page , all button function defined in controller file
    private void showMain() throws IOException {
        FXMLLoader mainRoot = new FXMLLoader(getClass().getResource("Login.fxml"));
        mainRoot.setControllerFactory(c -> {
            try {
                return new LoginController(this.window,this.player,this.territories,this.sc,this.os1,this.is1, false,
                        this.savedText);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
        Scene mainScene = new Scene(mainRoot.load());
        mainScene.getStylesheets().add(
                getClass().getResource("MainStyle.css")
                        .toExternalForm());

        window.setTitle("RISC Game");
        window.setScene(mainScene);
        window.show();
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        this.window = primaryStage;
        this.territories=createTerritories();
        this.player=new Player(0);

        //connectServer();
        //InitializeGame();
        showMain();
    }

    //for testing
    public static Map<String, Territory>  createTerritories() {
        int player_num=2;
        Map<String, Territory> territories=new HashMap<>();

        Territory[][] matrix = new Territory[player_num][3];
        for (int i = 0; i < player_num; i++) {
            for (int j = 0; j < 3; j++) {
                String tName = (char) ('T'+i) + String.valueOf(j);
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
        return territories;
    }
    public static void connect(Territory t1, Territory t2) {
        t1.connect(t2);
        t2.connect(t1);
    }

    public static void main(String[] args) {

        Client client=new Client();
        launch(args);
    }

}








