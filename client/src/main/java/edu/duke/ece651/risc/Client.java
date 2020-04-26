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
    //------------ -Fields--------------------
    //----------------------------------------
    ///basic information
    private int gameId;
    //final int totalSoldiers = 3;
    private Player player=null;
    private Map<String, Territory> territories=null;

    //communication stuffs
    private Scanner sc = new Scanner(System.in);
    private ObjectOutputStream os1 = null;
    private ObjectInputStream is1 = null;
    private ObjectOutputStream cos =null;
    private ObjectInputStream cis=null;

    private Socket socket = null;
    private Receiver receiver=null;
    private Sender sender=null;

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


    private void connectServer() throws IOException{

        System.out.println("Choose one game to play (start from 0):");
        gameId = Integer.parseInt(sc.nextLine());
        try {
            socket = new Socket("localhost", 8000 + gameId);
            System.out.println("Client Connected");
            this.os1 = new ObjectOutputStream(socket.getOutputStream());
            this.is1 = new ObjectInputStream(socket.getInputStream());
        } catch (ConnectException e) {
            System.out.println("Game not available, please choose another game:");
            connectServer();
            return;
        }

    }

    //--------------------------------------------
    //----------------Initialize Game-------------
    //--------------------------------------------

    private void InitializeGame() throws IOException, ClassNotFoundException {

        //receive info
        this.player = (Player) is1.readObject();
        System.out.println("recevied player");
        //receive map to be completed
        this.territories = (Map<String, Territory>) is1.readObject();
        System.out.println("recevied t");

        //connect to Chat Server
        Socket chattingSocket = new Socket("localhost", 7999 - gameId);
        System.out.println("Connected to Chat Server");
        this.cos = new ObjectOutputStream(chattingSocket.getOutputStream());
        this. cis = new ObjectInputStream(chattingSocket.getInputStream());
        this.receiver = new Receiver(cis, player.getId(), savedText);
        receiver.start();
        this.sender = new Sender(cos, player.getId());

    }


    //show the man page , all button function defined in controller file
    private void showMain() throws IOException {
        FXMLLoader mainRoot = new FXMLLoader(getClass().getResource("Login.fxml"));
        mainRoot.setControllerFactory(c -> {
            try {
                return new LoginController(this.window,this.player,this.territories,this.sc,this.os1,this.is1,
                        this.savedText, this.sender,this.gameId,this.cis,this.cos,this.receiver);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
        Scene mainScene = new Scene(mainRoot.load());
        mainScene.getStylesheets().add(
                getClass().getResource("LoginStyle.css")
                        .toExternalForm());

        window.setTitle("RISC Game");
        window.setScene(mainScene);
        window.show();
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        this.window = primaryStage;
        //this.territories=createTerritories();

        connectServer();
        showMain();
        //InitializeGame();
        //showMain();
    }

    //for testing
    public static void main(String[] args) {

        Client client=new Client();
        launch(args);
    }

}








