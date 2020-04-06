package edu.duke.ece651.risc;

import javax.swing.*;
import java.io.*;
import java.util.*;


public class Interface {

    ArrayList<String> stringBuffer=new ArrayList<>();

    //private Player player;
    JButton moveButton = new JButton("Move");
    JButton attackButton = new JButton("Attack");
    JButton upgradeButton = new JButton("Upgrade Unit");
    JButton upgradeTechButton = new JButton("Upgrade Tech");
    JButton commitButton = new JButton("Commit All Actions");
    JButton commitOnceButton=new JButton(("Commit Current Action"));
    JButton doneButton=new JButton(("Done"));
    int playerID=0;


    JTextArea MapField = new JTextArea("Map");
    JTextArea textField = new JTextArea("Game prompts:\n");
    JTextArea gameInfoField = new JTextArea("gameInfo");
    JTextArea actionHistoryField = new JTextArea();
    JScrollPane scrollPane_1 = new JScrollPane();
    //
    ArrayList<MapButton> mapButtons=new ArrayList<>();

    private boolean sameAround = false;
    private int action = 0;

    Interface(Map<String, Territory> territories){

    }


    private void setLayout(){
        JFrame jf = new JFrame("RISC evolution2");
        jf.setSize(1050, 960);
        jf.setLocationRelativeTo(null);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        SpringLayout layout = new SpringLayout();
        //JPanel panel1 = new JPanel(layout);
        JPanel panel2 = new JPanel(layout);

        SpringLayout.Constraints MapFieldCons = layout.getConstraints(MapField);
        MapFieldCons.setX(Spring.constant(25));
        MapFieldCons.setY(Spring.constant(25));
        MapFieldCons.setConstraint(SpringLayout.HEIGHT,Spring.constant(450));
        MapFieldCons.setConstraint(SpringLayout.WIDTH,Spring.constant(675));
        MapField.setEditable(false);

        SpringLayout.Constraints textFieldCons = layout.getConstraints(textField);
        textFieldCons.setX(Spring.constant(25));
        textFieldCons.setY(Spring.sum(MapFieldCons.getConstraint(SpringLayout.SOUTH),Spring.constant(10)));
        textFieldCons.setConstraint(SpringLayout.HEIGHT,Spring.constant(200));
        textFieldCons.setConstraint(SpringLayout.WIDTH,Spring.constant(675));
        textField.setEditable(false);

        SpringLayout.Constraints gameInfoFieldCons = layout.getConstraints(gameInfoField);
        gameInfoFieldCons.setX(Spring.sum(MapFieldCons.getConstraint(SpringLayout.EAST),Spring.constant(10)));
        gameInfoFieldCons.setY(MapFieldCons.getConstraint(SpringLayout.NORTH));
        gameInfoFieldCons.setConstraint(SpringLayout.HEIGHT,Spring.constant(450));
        gameInfoFieldCons.setConstraint(SpringLayout.WIDTH,Spring.constant(315));
        gameInfoField.setEditable(false);

        SpringLayout.Constraints actionHistoryFieldCons = layout.getConstraints(scrollPane_1);
        actionHistoryFieldCons.setX(gameInfoFieldCons.getConstraint(SpringLayout.WEST));
        actionHistoryFieldCons.setY(Spring.sum(MapFieldCons.getConstraint(SpringLayout.SOUTH),Spring.constant(10)));
        actionHistoryFieldCons.setConstraint(SpringLayout.HEIGHT,Spring.constant(200));
        actionHistoryFieldCons.setConstraint(SpringLayout.WIDTH,Spring.constant(315));
        actionHistoryField.setEditable(true);


        //button constrains
        SpringLayout.Constraints btnConsM = layout.getConstraints(moveButton);
        btnConsM.setX(Spring.constant(25));
        btnConsM.setY(Spring.sum(textFieldCons.getConstraint(SpringLayout.SOUTH),Spring.constant(10)));
        btnConsM.setConstraint(SpringLayout.HEIGHT,Spring.constant(70));
        btnConsM.setConstraint(SpringLayout.WIDTH,Spring.constant(150));

        SpringLayout.Constraints btnConsA = layout.getConstraints(attackButton);
        btnConsA.setX(Spring.sum(Spring.constant(25),btnConsM.getConstraint(SpringLayout.EAST)));
        btnConsA.setY(Spring.sum(textFieldCons.getConstraint(SpringLayout.SOUTH),Spring.constant(10)));
        btnConsA.setConstraint(SpringLayout.HEIGHT,Spring.constant(70));
        btnConsA.setConstraint(SpringLayout.WIDTH,Spring.constant(150));

        SpringLayout.Constraints btnConsU = layout.getConstraints(upgradeButton);
        btnConsU.setX(Spring.sum(Spring.constant(25),btnConsA.getConstraint(SpringLayout.EAST)));
        btnConsU.setY(Spring.sum(textFieldCons.getConstraint(SpringLayout.SOUTH),Spring.constant(10)));
        btnConsU.setConstraint(SpringLayout.HEIGHT,Spring.constant(70));
        btnConsU.setConstraint(SpringLayout.WIDTH,Spring.constant(150));

        SpringLayout.Constraints btnConsT = layout.getConstraints(upgradeTechButton);
        btnConsT.setX(Spring.sum(Spring.constant(25),btnConsU.getConstraint(SpringLayout.EAST)));
        btnConsT.setY(Spring.sum(textFieldCons.getConstraint(SpringLayout.SOUTH),Spring.constant(10)));
        btnConsT.setConstraint(SpringLayout.HEIGHT,Spring.constant(70));
        btnConsT.setConstraint(SpringLayout.WIDTH,Spring.constant(150));

        SpringLayout.Constraints btnConsC = layout.getConstraints(commitButton);
        btnConsC.setX(Spring.sum(Spring.constant(25),btnConsT.getConstraint(SpringLayout.EAST)));
        btnConsC.setY(Spring.sum(textFieldCons.getConstraint(SpringLayout.SOUTH),Spring.constant(10)));
        btnConsC.setConstraint(SpringLayout.HEIGHT,Spring.constant(70));
        btnConsC.setConstraint(SpringLayout.WIDTH,Spring.constant(150));

        SpringLayout.Constraints btnConsD = layout.getConstraints(doneButton);
        btnConsD.setX(Spring.sum(Spring.constant(25),btnConsC.getConstraint(SpringLayout.EAST)));
        btnConsD.setY(Spring.sum(textFieldCons.getConstraint(SpringLayout.SOUTH),Spring.constant(10)));
        btnConsD.setConstraint(SpringLayout.HEIGHT,Spring.constant(70));
        btnConsD.setConstraint(SpringLayout.WIDTH,Spring.constant(100));

        JTextArea ta = new JTextArea();
        JScrollPane sp = new JScrollPane(ta);
        panel2.add(sp);
        panel2.add(moveButton);
        panel2.add(attackButton);
        panel2.add(upgradeButton);
        panel2.add(upgradeTechButton);
        panel2.add(commitButton);
        panel2.add(doneButton);
        panel2.add(commitOnceButton);
        scrollPane_1.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        //panel2.add(MapField);
        panel2.add(textField);
        panel2.add(gameInfoField);
        panel2.add(actionHistoryField);
        panel2.add(scrollPane_1);
        scrollPane_1.setViewportView(actionHistoryField);
        //panel2.add(scrollPane_1);

        int group = 0;
        for(MapButton b : this.mapButtons){
            SpringLayout.Constraints btnCons = layout.getConstraints(b);
            btnCons.setX(Spring.sum(Spring.constant(10 + group % 3 * 60),MapFieldCons.getConstraint(SpringLayout.WEST)));
            btnCons.setY(Spring.constant(100 + group / 3 * 60));
            btnCons.setConstraint(SpringLayout.HEIGHT,Spring.constant(50));
            btnCons.setConstraint(SpringLayout.WIDTH,Spring.constant(50));
            panel2.add(b);
            group++;
        }
        jf.setContentPane(panel2);
        jf.setVisible(true);

    }

    //when choose one mode button, should not choose other options button again.
    private void setEnableAllButton(boolean ifEnable){
        this.moveButton.setEnabled(ifEnable);
        this.attackButton.setEnabled(ifEnable);
        this.upgradeButton.setEnabled(ifEnable);
        this.upgradeTechButton.setEnabled(ifEnable);
    }


    private void getMovementName(JButton button, final String moveName, final Map<String, Territory> territories){
        button.addActionListener(e -> {
            if(moveName.equals("A"))
                action = 1;
            else if(moveName.equals("U"))
                action = 2;

            if(!moveName.equals("D"))
                setEnableAllButton(false);
            BufferedWriter bw1;
            try {
                bw1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("./history.txt"),sameAround)));
                sameAround = !moveName.equals("D");
                bw1.write(moveName+"\n");
                bw1.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if(moveName.equals("A") || moveName.equals("U") || moveName.equals("M")) {
                for(MapButton b:mapButtons){
                    b.setCanClickSelf(territories,playerID);
                    if(b.canClick) {
                        b.setEnabled(true);
                        b.act(stringBuffer);
                    }
                    else b.setEnabled(false);
                }
            }
        });
    }



    private void attackCommand(Map<String, Territory> territories){
        getMovementName(attackButton,"A",territories);
    }

    private void moveCommand(Map<String, Territory> territories){
        getMovementName(moveButton,"M",territories);
    }

    private void UpdateTechCommand(Map<String, Territory> territories){
        getMovementName(upgradeTechButton,"T",territories);
    }

    private void UpgradeCommand(Map<String, Territory> territories){
        getMovementName(upgradeButton,"U",territories);
    }

    private void commitCommand(Map<String, Territory> territories){
        getMovementName(commitButton,"D",territories);
    }




    //好像是一个run forever的东西？？？？
    public void run(final Map<String, Territory> territories) {
        setLayout();
        moveCommand(territories);
        attackCommand(territories);
        UpdateTechCommand(territories);
        UpgradeCommand(territories);
        commitCommand(territories);

        //commitOnce Button
        commitOnceButton.addActionListener(e -> {
            action = 0;
            setEnableAllButton(true);
            for(MapButton b : mapButtons){
                b.setEnabled(true);
            }
        });

        //the Done button
        doneButton.addActionListener(e -> {
            Validator helper = new Validator();
            String content = actionHistoryField.getText();
            actionHistoryField.setText("");

            if (!helper.InputNumber_Validate(content)) {
                textField.append("Invalid number input，Type again!");
                content = "";
            }
            if (!content.equals("")) {
                BufferedWriter bw1;
                try {
                    bw1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("./history.txt"), sameAround)));
                    bw1.write(content + "\n");
                    bw1.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            if(action == 1) {
                for (MapButton b : mapButtons) {
                    b.setCanClickEnemy(territories, playerID);
                    if (b.canClick) {
                        b.setEnabled(true);
                        b.act(stringBuffer);
                    } else b.setEnabled(false);
                }
            }
            else if (action == 2){
                for (MapButton b : mapButtons) {
                    b.setEnabled(false);
                }
            }

            if(stringBuffer.size()!=0) {
                BufferedWriter bw1;
                try {
                    bw1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("./history.txt"), sameAround)));
                    bw1.write(stringBuffer.get(stringBuffer.size() - 1) + "\n");
                    bw1.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            stringBuffer.clear();
        });
    }

    public static Map<String, Territory>  createTerritories() {
        int player_num=2;
        Map<String, Territory> territories=new HashMap<>();

        Territory[][] matrix = new Territory[player_num][3];
        for (int i = 0; i < player_num; i++) {
            for (int j = 0; j < 3; j++) {
                String tName = (char) ('a' + i) + String.valueOf(j);
                Territory newTerritory = new Territory(tName, i);
                territories.put(tName, newTerritory);
                matrix[i][j] = newTerritory;
                if (j > 0) {
                    connect(newTerritory, matrix[i][j - 1]);
                }
                if (i > 0) {
                    connect(newTerritory, matrix[i - 1][j]);
                }
            }
        }
        return territories;
    }

    public static void connect(Territory t1, Territory t2) {
        t1.connect(t2);
        t2.connect(t1);
    }

    public static void main(String[] args) {
        Map<String, Territory> map=createTerritories();
        Interface i=new Interface(map);
        i.run(map);
    }
}
