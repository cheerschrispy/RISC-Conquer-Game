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
    Action a1 = new Action("M", "a0", "a2", territories.get("a0").getSoldierOfLevel(0, 1));
    Action a2 = new Action("M", "b0", "b2", territories.get("b0").getSoldierOfLevel(0, 1));
    Action a3 = new Action("A", "a0", "b0", territories.get("a0").getSoldierOfLevel(0, 1));
    Action a4 = new Action("A", "b0", "a2", territories.get("b0").getSoldierOfLevel(0, 2));

    p0.setActions(a3);
    assertEquals(true, v.validate(p0, territories));
    p0.clearActions();

    p0.setActions(a1);
    assertEquals(true, v.validate(p0, territories));
    p0.clearActions();

    Action a5 = new Action("M", "a0", "a2", territories.get("a0").getSoldierOfLevel(0, 5));
    p0.setActions(a5);
    assertEquals(false, v.validate(p0, territories));

    //p1.setActions(a3);
    p1.setActions(a4);
    assertEquals(false, v.validate(p1, territories));
    p1.clearActions();

    Action a6 = new Action("A", "b0", "a0", territories.get("b0").getSoldierOfLevel(0, 5));
    p1.setActions(a6);
    assertEquals(false, v.validate(p1, territories));
    p1.clearActions();

   
    p1.setActions(a2);
    assertEquals(false, v.validate(p1, territories));
    p1.clearActions();
 

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

	Territory newTerritory;

	if(j == 1 && i == 1){

	   newTerritory = new Territory(tName, 0);
	   newTerritory.initSoldiers(3);

	}

	else{

        newTerritory = new Territory(tName, i);
        newTerritory.initSoldiers(3);

	}

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




