package edu.duke.ece651.risc;

import java.util.*;

public class ForcePair {
  // Constructor: Auto sort the taken in soldiers array
  public ForcePair(int o, ArrayList<Unit> s) {
    this.owner = o;
    this.soldiers = sortByBonus(s);
  }
  
  // Check if there is no soldier left
  public boolean isLost() {
    return soldiers.isEmpty();
  }

  public int getOwner() {
    return this.owner;
  }

  public ArrayList<Unit> getSoldiers() {
    return this.soldiers;
  }

  public int getBonus(int id) {
    return this.soldiers.get(id).getBonus();
  }

  // Remove soldier
  public int getLowestSoldier() {
    return 0;
  }
  public int getHighestSoldier() {
    return this.soldiers.size() - 1;
  }
  public void dieByID(int id) {
    this.soldiers.remove(id);
  }

  private int owner;
  private ArrayList<Unit> soldiers;

  // Just as is named
  private ArrayList<Unit> sortByBonus(ArrayList<Unit> originalList) {
    ArrayList<Unit> out = new ArrayList<Unit>();

    // While old list is not empty
    while(!originalList.isEmpty()){

      int minBonus = originalList.get(0).getBonus();
      int minIndex = 0;


      // Find the smallest bounus index
      for(int i = 0; i < originalList.size(); i++) {
        Unit currU = originalList.get(i);
        if(minBonus > currU.getBonus()) {
          minBonus = currU.getBonus();
          minIndex = i;
        }
      }

      // Remove from old list, insert into new list
      out.add(originalList.remove(minIndex));

    }

    return out;

  }

}