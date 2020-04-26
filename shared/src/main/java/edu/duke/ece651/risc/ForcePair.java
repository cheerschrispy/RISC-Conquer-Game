package edu.duke.ece651.risc;

import java.util.*;

public class ForcePair {

  // Constructor: Auto sort the taken in soldiers array
  public ForcePair(int o, ArrayList<Unit> s) {
    this.owner = o;
    this.soldiers = sortByBonus(s);
    this.isCombinedForce = false;
  }

  // Check if there is no soldier left
  public boolean isLost() {

    if(!this.soldiers.isEmpty()) { // If me is not empty, don't care friend
      return false;
    }

    if(this.isCombinedForce) { // If I lost, my friend take over
      this.owner = this.friend;
      this.soldiers = this.friendSoldiers;
      this.isCombinedForce = false;
      return false;
    }

    return true; // I am empty and have no friend, lost

  }

  public boolean isACombinedForce() {
    return this.isCombinedForce;
  }

  public int getFriend() {
    return this.friend;
  }

  public ArrayList<Unit> getFriendSoldiers() {
    return this.friendSoldiers;
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

  public void combine(ForcePair rhs) { // Combine two forces
    this.isCombinedForce = true;
    this.friend = rhs.getOwner();
    this.friendSoldiers = rhs.getSoldiers();
  }

  public void dieByID(int id) {
    this.soldiers.remove(id);
  }

  private int owner;
  private ArrayList<Unit> soldiers;

  private boolean isCombinedForce;
  private int friend;
  private ArrayList<Unit> friendSoldiers;

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