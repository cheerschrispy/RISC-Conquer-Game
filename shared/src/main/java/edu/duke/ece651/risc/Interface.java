package edu.duke.ece651.risc;

import javax.swing.*;
import javax.swing.Action;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Interface {
    //private Player player;
    JButton moveButtton = new JButton("Move");
    JButton attackButtton = new JButton("Attack");
    JButton upgradeButtton = new JButton("Upgrade");
    JButton doneButtton = new JButton("Commit");

    JTextArea MapField = new JTextArea("Map");
    JTextArea textField = new JTextArea("prompts");
    JTextArea gameInfoField = new JTextArea("gameInfo");
    JTextArea actionHistoryField = new JTextArea("actionHistory");


    Interface(Player player){
        //this.player=player;
    }


    public void run(final Player player) {
        //create a frame as a top container
        JFrame jf = new JFrame("RISC evolution2");
        jf.setSize(1050, 960);
        jf.setLocationRelativeTo(null);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //use springpanel
        SpringLayout layout = new SpringLayout();
        JPanel panel1 = new JPanel(layout);
        JPanel panel2 = new JPanel(layout);


        //add some components
        //JLabel label = new JLabel("Game info: ");

        //add to panel
        //panel.add(label);

        moveButtton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(player.getId());
                MapField.append(String.valueOf(player.getId()));
                moveButtton.setEnabled(false);
                //moveButtton.
            }
        });

        //different fields

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
        MapField.setEditable(false);

        SpringLayout.Constraints gameInfoFieldCons = layout.getConstraints(gameInfoField);
        gameInfoFieldCons.setX(Spring.sum(MapFieldCons.getConstraint(SpringLayout.EAST),Spring.constant(10)));
        gameInfoFieldCons.setY(MapFieldCons.getConstraint(SpringLayout.NORTH));
        gameInfoFieldCons.setConstraint(SpringLayout.HEIGHT,Spring.constant(450));
        gameInfoFieldCons.setConstraint(SpringLayout.WIDTH,Spring.constant(315));
        MapField.setEditable(false);

        SpringLayout.Constraints actionHistoryFieldCons = layout.getConstraints(actionHistoryField);
        actionHistoryFieldCons.setX(gameInfoFieldCons.getConstraint(SpringLayout.WEST));
        actionHistoryFieldCons.setY(Spring.sum(MapFieldCons.getConstraint(SpringLayout.SOUTH),Spring.constant(10)));
        actionHistoryFieldCons.setConstraint(SpringLayout.HEIGHT,Spring.constant(200));
        actionHistoryFieldCons.setConstraint(SpringLayout.WIDTH,Spring.constant(315));
        MapField.setEditable(false);




        //button constrains
        SpringLayout.Constraints btnConsM = layout.getConstraints(moveButtton);
        btnConsM.setX(Spring.constant(25));
        btnConsM.setY(Spring.sum(textFieldCons.getConstraint(SpringLayout.SOUTH),Spring.constant(10)));
        btnConsM.setConstraint(SpringLayout.HEIGHT,Spring.constant(70));
        btnConsM.setConstraint(SpringLayout.WIDTH,Spring.constant(150));

        SpringLayout.Constraints btnConsA = layout.getConstraints(attackButtton);
        btnConsA.setX(Spring.sum(Spring.constant(25),btnConsM.getConstraint(SpringLayout.EAST)));
        btnConsA.setY(Spring.sum(textFieldCons.getConstraint(SpringLayout.SOUTH),Spring.constant(10)));
        btnConsA.setConstraint(SpringLayout.HEIGHT,Spring.constant(70));
        btnConsA.setConstraint(SpringLayout.WIDTH,Spring.constant(150));

        SpringLayout.Constraints btnConsU = layout.getConstraints(upgradeButtton);
        btnConsU.setX(Spring.sum(Spring.constant(25),btnConsA.getConstraint(SpringLayout.EAST)));
        btnConsU.setY(Spring.sum(textFieldCons.getConstraint(SpringLayout.SOUTH),Spring.constant(10)));
        btnConsU.setConstraint(SpringLayout.HEIGHT,Spring.constant(70));
        btnConsU.setConstraint(SpringLayout.WIDTH,Spring.constant(150));

        SpringLayout.Constraints btnConsD = layout.getConstraints(doneButtton);
        btnConsD.setX(Spring.sum(Spring.constant(25),btnConsU.getConstraint(SpringLayout.EAST)));
        btnConsD.setY(Spring.sum(textFieldCons.getConstraint(SpringLayout.SOUTH),Spring.constant(10)));
        btnConsD.setConstraint(SpringLayout.HEIGHT,Spring.constant(70));
        btnConsD.setConstraint(SpringLayout.WIDTH,Spring.constant(150));




        panel2.add(moveButtton);
        panel2.add(attackButtton);
        panel2.add(upgradeButtton);
        panel2.add(doneButtton);
        panel2.add(MapField);
        panel2.add(textField);
        panel2.add(gameInfoField);
        panel2.add(actionHistoryField);


        Box vBox = Box.createVerticalBox();
        vBox.add(panel1);
        vBox.add(panel2);
        jf.setContentPane(panel2);
        jf.setVisible(true);

    }

    public static void main(String[] args) throws Exception {
        Player player=new Player(1);
        Interface i=new Interface(player);
        i.run(player);
    }

}
