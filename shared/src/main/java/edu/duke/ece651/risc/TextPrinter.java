package edu.duke.ece651.risc;

import java.util.List;

/**
 * TextPrinter
 */
public interface TextPrinter {
    
    // PlayerInfo
    String appendPlayerInfo(Player player, int techLevel, int foodResource, int techResources);
    
    // Move History : moveController Line 160-176
    String appendMoveHistory(String source, String destination, List<Integer> soldierDistribution);
    
    // Attack History : attackController Line 168-184
    String appendAttackHistory(String source, String destination, List<Integer> soldierDistribution);
    
    // Upgrade History : upgradeController Line 102-120
    String appendUpgradeHistory(int soldier_num, int srcLevel, int desLevel);
    
    // Map button : moveController Line 196-218  
    String appendMapButton(Territory t);
    
    //------------------------------------------ Deprecated? --------------------------------------------------
    // Buttons
    String moveButtonText();
    String attackButtonText();

    String upUnitButtonText();
    String upTechButtonText();

    String comOnceButtonText();
    String comAllButtonText();
    String doneButtonText();

    // Fields
    String mapFieldText();
    String textFieldText();
    String printGameInfoField();
    String printActHistoryField();
    String printPlayerInfoField();

}
