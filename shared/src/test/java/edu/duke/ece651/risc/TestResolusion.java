package edu.duke.ece651.risc;

import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TestResolusion {
    @Test
    public void test_attack() {
        Executor tEX = new Executor();
        Map<String, Territory> territories = new HashMap<String, Territory>();
        Map<String, Map<Integer, ArrayList<Unit>>> testAttacks = new HashMap<String, Map<Integer, ArrayList<Unit>>>();



        ArrayList<Unit> ozListA = new ArrayList<Unit>(
                Arrays.asList(new Unit(4),
                        new Unit(11),
                        new Unit(20),
                        new Unit(4))
        );

        ArrayList<Unit> azerothListA = new ArrayList<Unit>(
                Arrays.asList(new Unit(14),
                        new Unit(10),
                        new Unit(6),
                        new Unit(9))
        );

        ArrayList<Unit> lordaeronListA = new ArrayList<Unit>(
                Arrays.asList(new Unit(3),
                        new Unit(22),
                        new Unit(2),
                        new Unit(15))
        );
        ArrayList<Unit> ozListD = new ArrayList<Unit>(
                Arrays.asList(new Unit(25),
                        new Unit(11),
                        new Unit(20),
                        new Unit(14))
        );

        ArrayList<Unit> azerothListD = new ArrayList<Unit>(
                Arrays.asList(new Unit(5),
                        new Unit(3),
                        new Unit(3),
                        new Unit(9))
        );

        ArrayList<Unit> lordaeronListD = new ArrayList<Unit>(
                Arrays.asList(new Unit(3),
                        new Unit(22),
                        new Unit(2),
                        new Unit(15))
        );

        Territory terrO = new Territory("OZ", 1);
        terrO.setSoldiers(ozListD);
        Territory terrA = new Territory("Azeroth", 2);
        terrA.setSoldiers(azerothListD);
        Territory terrL = new Territory("Lordaeron", 3);
        terrL.setSoldiers(lordaeronListD);

        territories.put("OZ", terrO);
        territories.put("Azeroth", terrA);
        territories.put("Lordaeron", terrL);

        Map<Integer, ArrayList<Unit>> testOZ = new HashMap<Integer, ArrayList<Unit>>();
        testOZ.put(2, ozListA);

        Map<Integer, ArrayList<Unit>> testAzeroth = new HashMap<Integer, ArrayList<Unit>>();
        testAzeroth.put(3, azerothListA);

        Map<Integer, ArrayList<Unit>> testLordaeron = new HashMap<Integer, ArrayList<Unit>>();
        testLordaeron.put(1, lordaeronListA);


        testAttacks.put("OZ", testOZ);
        testAttacks.put("Azeroth", testAzeroth);
        testAttacks.put("Lordaeron", testLordaeron);

        tEX.attack(testAttacks, territories);
        Set<String> tkeys = territories.keySet();
        for(String k: tkeys) {
            System.out.println(k + " is now owned by " + territories.get(k).getOwner());
        }
    }

}
