package edu.duke.ece651.risc;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.util.*;
//import java.util.Map;

import org.junit.jupiter.api.Test;

import javax.swing.*;

public class TestPlayer {
  // private Territory territories;
  public  Map<String, Territory> territories=new HashMap<>();
  @Test
  public void test_()throws FileNotFoundException,IOException {
    Player player = new Player(0);
    //Scanner sc = new Scanner(System.in);
    assertEquals(0, player.getId());
    assertEquals(0, player.getActions().size());
    createTerritories();
    System.setIn(new FileInputStream("src/test/resources/input1.txt"));
    // FileInputStream
    Scanner sc = new Scanner(System.in);
    
    HashMap<String, Integer> init_info = new HashMap<>();
    player.initial_game(territories, sc, 9, init_info, new JTextArea());
    player.addAction(territories, "a", sc);
    territories.get("a2").setOwner(1);
    player.addAction(territories, "a", sc);

    
    player.addAction_afterFail(territories, new JTextArea());
    //player.addAction(territories, "a", sc);
    
  }

 public void createTerritories() {
    int player_num=1;
    
        Territory[][] matrix = new Territory[player_num][3];
        for (int i = 0; i < player_num; i++) {
            for (int j = 0; j < 3; j++) {
                String tName = (char) ('a' + i) + String.valueOf(j);
                Territory newTerritory = new Territory(tName,  3);
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
  

}
