package edu.duke.ece651.risc;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TestTerritory {
  @Test
  public void test_() {
    Territory china = new Territory("China",0,100);
    Territory japan = new Territory("Japan",1,50);
    china.connect(japan);
    assertEquals(true, china.getNeighbors().contains(japan));
    assertEquals("China", china.getName());
    assertEquals(0, china.getOwner());
    assertEquals(100, china.getNum());
    china.setNum(101);
    assertEquals(101, china.getNum());
    china.setOwner(2);
    assertEquals(2, china.getOwner());
    
  }

}
