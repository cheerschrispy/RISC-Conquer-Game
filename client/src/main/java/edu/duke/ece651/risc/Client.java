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
    //as a client------------------
    String name;
    private Scanner sc = new Scanner(System.in);
    private ObjectOutputStream os1 = null;
    private ObjectInputStream is1 = null;
    final int totalSoldiers = 3;
    private Socket socket = null;


    private Stage window;

    private void showMain() throws IOException {
        FXMLLoader mainRoot = new FXMLLoader(getClass().getResource("Main.fxml"));
        mainRoot.setControllerFactory(c -> {
            try {
                return new mainController(window);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
        Scene mainScene = new Scene(mainRoot.load());
        window.setTitle("RISC Game");
        window.setScene(mainScene);
        window.show();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.window = primaryStage;
        showMain();
    }


    public static void main(String[] args) {
        launch(args);
    }

}








