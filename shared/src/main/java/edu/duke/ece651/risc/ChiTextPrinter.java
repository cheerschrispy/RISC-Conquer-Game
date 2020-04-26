package edu.duke.ece651.risc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChiTextPrinter implements TextPrinter {

    @Override
    public String moveButtonText() {
        return "移动";
    }

    @Override
    public String attackButtonText() {
        return "攻击";
    }

    @Override
    public String upUnitButtonText() {
        return "升级单位";
    }

    @Override
    public String upTechButtonText() {
        return "升级科技";
    }

    @Override
    public String comOnceButtonText() {
        return "提交全部指令";
    }

    @Override
    public String comAllButtonText() {
        return "提交当前指令";
    }

    @Override
    public String doneButtonText() {
        return "完成";
    }

    @Override
    public String mapFieldText() {
        return "地图";
    }

    @Override
    public String textFieldText() {
        return "游戏提示词:\n";
    }

    @Override
    public String printGameInfoField() {
        return "游戏信息";
    }

    @Override
    public String printActHistoryField() {
        return "指令历史: \r\n\r\n";
    }

    @Override
    public String printPlayerInfoField() {
        return "玩家信息: \r\n\r\n";
    }

    @Override
    public String appendPlayerInfo(Player player, int techLevel, int foodResource, int techResources) {
        String s="嗨！欢迎玩家 " + player.getId() + ".\r\n" + "现在你处于科技水平 " + techLevel + ".\r\n" + "你有 " + foodResource + " 单位食物\r\n"
                + "你有 " + techResources + " 单位科技点\n";
        if(!player.getAlliances().isEmpty()){
            for(int a :player.getAlliances()){
                s+="你的盟友ID为：";
                s+=a;
                s+="\n";
            }
        }
        return s;
    }
    
    @Override
    public String appendMoveHistory(String source, String destination, List<Integer> soldierDistribution) {
        StringBuilder description = new StringBuilder();
        description.append("移动历史-从 ").append(source).append(" 到 ").append(destination).append(" 移动了 :\n");
        if (soldierDistribution.get(0) != 0)
            description.append(soldierDistribution.get(0)).append(" lv0 士兵\n");
            if (soldierDistribution.get(1) != 0)
            description.append(soldierDistribution.get(1)).append(" lv1 士兵\n");
            if (soldierDistribution.get(2) != 0)
            description.append(soldierDistribution.get(2)).append(" lv2 士兵\n");
            if (soldierDistribution.get(3) != 0)
            description.append(soldierDistribution.get(3)).append(" lv3 士兵\n");
            if (soldierDistribution.get(4) != 0)
            description.append(soldierDistribution.get(4)).append(" lv4 士兵\n");
        if (soldierDistribution.get(5) != 0)
        description.append(soldierDistribution.get(5)).append(" lv5 士兵\n");
        if (soldierDistribution.get(6) != 0)
        description.append(soldierDistribution.get(6)).append(" lv6 士兵\n");
        
        return String.valueOf(description);
    }

    @Override
    public String appendAttackHistory(String source, String destination, List<Integer> soldierDistribution) {
        StringBuilder description = new StringBuilder();
        description.append("攻击历史-从 ").append(source).append(" 进攻 ").append(destination).append(", 进攻兵力 :\n");
        if (soldierDistribution.get(0) != 0)
        description.append(soldierDistribution.get(0)).append(" lv0 士兵\n");
        if (soldierDistribution.get(1) != 0)
        description.append(soldierDistribution.get(1)).append(" lv1 士兵\n");
        if (soldierDistribution.get(2) != 0)
        description.append(soldierDistribution.get(2)).append(" lv2 士兵\n");
        if (soldierDistribution.get(3) != 0)
        description.append(soldierDistribution.get(3)).append(" lv3 士兵\n");
        if (soldierDistribution.get(4) != 0)
        description.append(soldierDistribution.get(4)).append(" lv4 士兵\n");
        if (soldierDistribution.get(5) != 0)
        description.append(soldierDistribution.get(5)).append(" lv5 士兵\n");
        if (soldierDistribution.get(6) != 0)
        description.append(soldierDistribution.get(6)).append(" lv6 士兵\n");
        
        return String.valueOf(description);
    }
    
    @Override
    public String appendUpgradeHistory(int soldier_num, int srcLevel, int desLevel) {
        String description = "升级: " + soldier_num + " 个 " + srcLevel + " 级士兵" +
                " 到 " + desLevel + " 级\n";

        return description;
    }

    @Override
    public String appendMapButton(Territory t) {
        StringBuilder s = new StringBuilder();
        s.append("领土信息:\r\n\r\n").append("领土 ").append(t.getName())
                .append(", 现在被 ").append(t.getOwner()).append(" 拥有.\r\n");
        s.append("当前食物生产速度为：").append(t.getFoodProduct()).append("\n");
        s.append("当前科技生产速度为：").append(t.getTechProduct()).append("\n\n");
        for(int i = 0; i < 7; i++){
            s.append("有   ").append(t.getSoldierNumOfLevel(i)).append("   个 ").append(i).append(" 级士兵.\r\n");
        }
        s.append("\r\n");
        HashMap<Integer, ArrayList<Unit>> alliances = t.getAllies();
        for(int key : alliances.keySet()){

            for(int i = 0; i < 7; i++){
                int num = t.getAllyNumOfLevel(i, key);
                if(num != 0){
                    s.append("盟友 "+key+" 有  ").append(num).append("  个").append(i).append(" 级士兵.\r\n");
                }
            }
            s.append("\r\n");
        }
        
        return String.valueOf(s);
    }



}
