package edu.duke.ece651.risc;



import static org.junit.jupiter.api.Assertions.*;



import org.junit.jupiter.api.Test;



import java.util.*;



public class TestValidator {

  @Test  

  public void test_Validator() {

    Validator v = new Validator();

    createTerritories();

    Player p0 = new Player(0);

    Player p1 = new Player(1);

    Action a1 = new Action("M", "a0", "a2", 1);

    Action a2 = new Action("A", "a1", "b1", 2);

    // Action a3 = new Action("A", "b1", "a1", 2);

    Action a4 = new Action("A", "b0", "a2", 2);



    p0.setActions(a1);

    p0.setActions(a2);

    assertEquals(true, v.validate(p0, territories));

    p0.clearActions();



    Action a5 = new Action("M", "a0", "a2", 5);

    p0.setActions(a5);

    assertEquals(false, v.validate(p0, territories));



    //p1.setActions(a3);

    p1.setActions(a4);

    assertEquals(false, v.validate(p1, territories));

    p1.clearActions();



    Action a6 = new Action("A", "b0", "a0", 5);

    p1.setActions(a6);

    assertEquals(false, v.validate(p1, territories));



    

    Prompts pp = new Prompts(territories);

    pp.GraphPrompts();

    pp.CurrentRoundHistory(p0.getActions());

    pp.CurrentRoundHistory(p1.getActions());

    pp.OPtionsPrompts("a1");

  }

  

  public  Map<String, Territory> territories = new HashMap<>();



  public void createTerritories() {

    Territory[][] matrix = new Territory[2][3];

    for (int i = 0; i < 2; i++) {

      for (int j = 0; j < 3; j++) {

        String tName = (char) ('a' + i) + String.valueOf(j);

        Territory newTerritory = new Territory(tName, i, 3);

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


