package edu.duke.ece651.risc;

import java.util.*;

public class ForcePair {
  private int owner;
  private int soldierNum;

  public ForcePair() {
    this.owner = -1;
    this.soldierNum = -1;
  }
  // Constructor
  public ForcePair(int o, int s) {
    this.owner = o;
    this.soldierNum = s;
  }
  
   /*public static Position copyPos(Position p) {
    Position out = new Position(p.x, p.y);
    return out;
  }*/

  @Override
  public boolean equals(Object o) { 
  
  if(!o.getClass().equals(ForcePair.class)) {
    return false;
  }
              
  // typecast o to Complex so that we can compare data members  
  ForcePair c = (ForcePair) o; 
          
  // Compare the data members and return accordingly  
  return Integer.compare(this.owner, c.owner) == 0
                && Integer.compare(this.soldierNum, c.soldierNum) == 0; 
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.owner, this.soldierNum);
  }


  public int getOwner() {
    return this.owner;
  }

  public int getSoldier() {
    return this.soldierNum;
  }

  public void lossFight() {
    if(this.soldierNum > 0) {
      this.soldierNum--;
    }
  }

/*  public void setOwner(int x) {
    this.x = x;
  }

  public void setY(int y) {
    this.y = y;
  }*/

}