package edu.duke.ece651.risc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EngTextPrinter implements TextPrinter {

    @Override
    public String moveButtonText() {
        return "Move";
    }

    @Override
    public String attackButtonText() {
        return "Attack";
    }

    @Override
    public String upUnitButtonText() {
        return "Upgrade Unit";
    }

    @Override
    public String upTechButtonText() {
        return "Upgrade Tech";
    }

    @Override
    public String comOnceButtonText() {
        return "Commit All";
    }

    @Override
    public String comAllButtonText() {
        return "Commit Current Action";
    }

    @Override
    public String doneButtonText() {
        return "Done";
    }

    @Override
    public String mapFieldText() {
        return "Map";
    }

    @Override
    public String textFieldText() {
        return "Game prompts:\n";
    }

    @Override
    public String printGameInfoField() {
        return "Game Information:";
    }

    @Override
    public String printActHistoryField() {
        return "Action History: \r\n\r\n";
    }

    @Override
    public String printPlayerInfoField() {
        return "Player Information: \r\n\r\n";
    }

    @Override
    public String appendPlayerInfo(Player player, int techLevel, int foodResource, int techResources) {
        String s= "Hi, welcome player " + player.getId() + ".\r\n" + "Now you are in TECHNIQUE level " + techLevel + ".\r\n"
                + "You have " + foodResource + " food resources.\r\n" + "You have " + techResources
                + " tech resources.\r\n";
        if(!player.getAlliances().isEmpty()){
            for(int a :player.getAlliances()){
                s+="Your alliance's Id is：";
                s+=a;
                s+="\n";
            }
        }
        return s;

    }
    
    @Override
    public String appendMoveHistory(String source, String destination, List<Integer> soldierDistribution) {
        
        StringBuilder description = new StringBuilder();
        description.append("Move from ").append(source).append(" to ").append(destination).append(" :\n");
        if (soldierDistribution.get(0) != 0)
        description.append(soldierDistribution.get(0)).append(" lv0 Soldiers\n");
        if (soldierDistribution.get(1) != 0)
        description.append(soldierDistribution.get(1)).append(" lv1 Soldiers\n");
        if (soldierDistribution.get(2) != 0)
        description.append(soldierDistribution.get(2)).append(" lv2 Soldiers\n");
        if (soldierDistribution.get(3) != 0)
        description.append(soldierDistribution.get(3)).append(" lv3 Soldiers\n");
        if (soldierDistribution.get(4) != 0)
        description.append(soldierDistribution.get(4)).append(" lv4 Soldiers\n");
        if (soldierDistribution.get(5) != 0)
        description.append(soldierDistribution.get(5)).append(" lv5 Soldiers\n");
        if (soldierDistribution.get(6) != 0)
        description.append(soldierDistribution.get(6)).append(" lv6 Soldiers\n");
        
        return String.valueOf(description);
    }

    @Override
    public String appendAttackHistory(String source, String destination, List<Integer> soldierDistribution) {
        StringBuilder description = new StringBuilder();
        description.append("Attack ").append(destination).append(" from ").append(source).append(" with:\n");
        if (soldierDistribution.get(0) != 0)
        description.append(soldierDistribution.get(0)).append(" lv0 Soldiers\n");
        if (soldierDistribution.get(1) != 0)
        description.append(soldierDistribution.get(1)).append(" lv1 Soldiers\n");
        if (soldierDistribution.get(2) != 0)
        description.append(soldierDistribution.get(2)).append(" lv2 Soldiers\n");
        if (soldierDistribution.get(3) != 0)
        description.append(soldierDistribution.get(3)).append(" lv3 Soldiers\n");
        if (soldierDistribution.get(4) != 0)
        description.append(soldierDistribution.get(4)).append(" lv4 Soldiers\n");
        if (soldierDistribution.get(5) != 0)
        description.append(soldierDistribution.get(5)).append(" lv5 Soldiers\n");
        if (soldierDistribution.get(6) != 0)
        description.append(soldierDistribution.get(6)).append(" lv6 Soldiers\n");
        
        return String.valueOf(description);
    }
    
    @Override
    public String appendUpgradeHistory(int soldier_num, int srcLevel, int desLevel) {
        String description = "Upgrade: " + soldier_num + " Lv" + srcLevel + " Soldiers" +
                " to Lv" + desLevel + "\n";

        return description;
    }
    
    @Override
    public String appendMapButton(Territory t) {
        
        StringBuilder s = new StringBuilder();
        s.append("Territory Information:\r\n\r\n").append("This is territory ").append(t.getName())
                .append(", owned by ").append(t.getOwner()).append(" now.\r\n");

        s.append("The current Food production speed is：").append(t.getFoodProduct()).append("\n");
        s.append("The current Technology production speed is：").append(t.getTechProduct()).append("\n\n");

        for(int i = 0; i < 7; i++){
            s.append("It has   ").append(t.getSoldierNumOfLevel(i)).append("   level-").append(i).append(" soldiers.\r\n");
        }
        s.append("\r\n");
        HashMap<Integer, ArrayList<Unit>> alliances = t.getAllies();
        for(int key : alliances.keySet()){
            //s.append("It has ally player ").append(key).append(" in this territory");
            for(int i = 0; i < 7; i++){
                int num = t.getAllyNumOfLevel(i, key);
                if(num != 0){
                    s.append("Alliance "+key+" has   ").append(num).append("   level-").append(i).append(" soldiers.\r\n");
                }
            }
            s.append("\r\n");
        }
        
        return String.valueOf(s);
    }



}