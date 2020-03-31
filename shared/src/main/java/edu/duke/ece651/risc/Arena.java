package edu.duke.ece651.risc;

import java.util.*;

public class Arena {

  private final int TICK = 1;
  private final int TOCK = 2;

  // Constructor:
  public Arena(ForcePair att, ForcePair def) {
    this.attacker = att;
    this.defender = def;
    this.tickTock = TICK;
  }


  // Perform the combat, return the winner
  public ForcePair run() {
    int attID, defID;
    int attDice, defDice;
 
    while (!attacker.isLost() && !defender.isLost()) {
      if(this.tickTock == TICK) {
        attID = this.attacker.getHighestSoldier();
        defID = this.defender.getLowestSoldier();
        this.tickTock = TOCK;
      }

      else {
        attID = this.attacker.getLowestSoldier();
        defID = this.defender.getHighestSoldier();
        this.tickTock = TICK;
      }

      attDice = rollDice(this.attacker.getBonus(attID));
      defDice = rollDice(this.defender.getBonus(defID));
   
      if (attDice <= defDice) {
        attacker.dieByID(attID);
      } 
      else {
        defender.dieByID(defID);  
      }

    }

    if (attacker.isLost()) {     
      return defender;
    } 
    else {
      return attacker;
    }
  }
 

  private ForcePair attacker;
  private ForcePair defender;
  private int tickTock;

  // Roll a 20 sided dice and add bonus
  private int rollDice(int bonus) {
    Random rand = new Random(System.nanoTime());
    return rand.nextInt(20) + bonus;
  }

}

            // System.out.println("Attacker (owned by " + attacker.getOwner() + 
            //                        ") current: " + attacker.getSoldier() + " soldiers");
            // System.out.println("Defender (owned by " + defender.getOwner() + 
            //                        ") current: " + defender.getSoldier() + " soldiers");
            // System.out.println("Attacker roll dice: " + attDice);
            // System.out.println("Defender roll dice: " + defDice);

              // System.out.println("Attacker lost a fight!");
 // System.out.println("Defender lost a fight!");
 // System.out.println("Defender won the combat!");
  // System.out.println("Attacker won the combat!");