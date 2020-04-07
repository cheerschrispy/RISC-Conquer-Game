package edu.duke.ece651.risc;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class TestAction {
  @Test
  public void test_() {
    Action actionM = new Action("M","a1","b1", new ArrayList<Unit>());
    Action actionU = new Action("U","a2", new ArrayList<Unit>(), 3);
    Action actionT = new Action("T");

    assertEquals("M", actionM.getName());
    assertEquals("a1", actionM.getStart());
    assertEquals("b1", actionM.getEnd());

    actionM.addSoldier(new Unit());
    assertEquals(3, actionU.getLevels());
    actionU.getSoldiers();

    
  }

}
