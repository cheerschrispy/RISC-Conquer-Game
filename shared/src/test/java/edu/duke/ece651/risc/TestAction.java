package edu.duke.ece651.risc;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TestAction {
  @Test
  public void test_() {
    Action action=new Action("M","a1","b1",2);
    assertEquals("M",action.getName());
    assertEquals("a1", action.getStart());
    assertEquals("b1", action.getEnd());
    assertEquals(2, action.getNum());
    
  }

}
