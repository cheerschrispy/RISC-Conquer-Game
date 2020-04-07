package edu.duke.ece651.risc;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TestTerritory {
  @Test
  public void test_() {


    Territory china = new Territory("China",0);
    china.initSoldiers(100);
    Territory japan = new Territory("Japan",1);
    japan.initSoldiers(50);
    china.connect(japan);

    assertEquals(true, china.getNeighbors().contains(japan));
    assertEquals("China", china.getName());
    assertEquals(0, china.getOwner());

    china.setOwner(2);
    assertEquals(2, china.getOwner());
    assertEquals(3, china.getSize());
    china.setSoldiers(japan.getSoldiers());

    assertEquals(100, china.getSoldierOfLevel(0, china.getSoldierNumOfLevel(0)));
    
  }

}
