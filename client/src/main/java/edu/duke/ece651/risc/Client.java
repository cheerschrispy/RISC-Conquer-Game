package edu.duke.ece651.risc;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.*;
import java.net.Socket;
import java.util.*;
//import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class Client extends Thread {
    //as a client------------------
    String name;
    private Scanner sc=new Scanner(System.in);
    private ObjectOutputStream os1 = null;
    private ObjectInputStream is1 = null;
    final int totalsoldier=9;

    ////---------------------------
    ///-----As an interface--------
    ////---------------------------
    private JButton moveButton = new JButton("Move");
    private JButton attackButton = new JButton("Attack");
    private JButton upgradeButton = new JButton("Upgrade Unit");
    private JButton upgradeTechButton = new JButton("Upgrade Tech");
    private JButton commitButton = new JButton("Commit All Actions");
    private JButton commitOnceButton=new JButton(("Commit Current Action"));
    private JButton doneButton=new JButton(("Done"));

    private JTextArea MapField = new JTextArea("Map");
    private JTextArea textField = new JTextArea("Game prompts:\n");
    private JTextArea gameInfoField = new JTextArea("gameInfo");
    private JTextArea actionHistoryField = new JTextArea();

    JFrame jf = new JFrame("RISC evolution2");
    SpringLayout layout;
    JPanel panel2;
    private ArrayList<MapButton> mapButtons=new ArrayList<>();

    boolean sameAround=false;
    public boolean commited=false;
    private int action = 0;
    private Player player=null;
    private Map<String, Territory> territories=null;
    ArrayList<String> stringBuffer=new ArrayList<>();

    public void setAttributes(Player player, Map<String, Territory> territories){
        this.player=player;
        this.territories=territories;
    }

    private void setLayout(){
        //generate the map
        int playerNumber = territories.size() / 3;
        System.out.println(playerNumber);
        for(int i = 0; i < playerNumber; i++) {
            for (int j = 0; j < 3; j++) {
                char c = (char) ('a' + i);
                String name = c + String.valueOf(j);
                this.mapButtons.add(new MapButton(true, name));
            }
        }
        //other components
        //JFrame jf = new JFrame("RISC evolution2");
        jf.setSize(1050, 960);
        jf.setLocationRelativeTo(null);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //use springpanel
        layout = new SpringLayout();
        //JPanel panel1 = new JPanel(layout);
        panel2 = new JPanel(layout);

        SpringLayout.Constraints MapFieldCons = layout.getConstraints(MapField);
        MapFieldCons.setX(Spring.constant(25));
        MapFieldCons.setY(Spring.constant(25));
        MapFieldCons.setConstraint(SpringLayout.HEIGHT, Spring.constant(450));
        MapFieldCons.setConstraint(SpringLayout.WIDTH, Spring.constant(675));
        MapField.setEditable(false);


        SpringLayout.Constraints textFieldCons = layout.getConstraints(textField);
        textFieldCons.setX(Spring.constant(25));
        textFieldCons.setY(Spring.sum(MapFieldCons.getConstraint(SpringLayout.SOUTH), Spring.constant(10)));
        textFieldCons.setConstraint(SpringLayout.HEIGHT, Spring.constant(200));
        textFieldCons.setConstraint(SpringLayout.WIDTH, Spring.constant(675));
        textField.setEditable(false);


        SpringLayout.Constraints gameInfoFieldCons = layout.getConstraints(gameInfoField);
        gameInfoFieldCons.setX(Spring.sum(MapFieldCons.getConstraint(SpringLayout.EAST), Spring.constant(10)));
        gameInfoFieldCons.setY(MapFieldCons.getConstraint(SpringLayout.NORTH));
        gameInfoFieldCons.setConstraint(SpringLayout.HEIGHT, Spring.constant(450));
        gameInfoFieldCons.setConstraint(SpringLayout.WIDTH, Spring.constant(315));
        gameInfoField.setEditable(false);


        SpringLayout.Constraints actionHistoryFieldCons = layout.getConstraints(actionHistoryField);
        actionHistoryFieldCons.setX(gameInfoFieldCons.getConstraint(SpringLayout.WEST));
        actionHistoryFieldCons.setY(Spring.sum(MapFieldCons.getConstraint(SpringLayout.SOUTH), Spring.constant(10)));
        actionHistoryFieldCons.setConstraint(SpringLayout.HEIGHT, Spring.constant(200));
        actionHistoryFieldCons.setConstraint(SpringLayout.WIDTH, Spring.constant(315));
        actionHistoryField.setEditable(true);




        //button constrains
        SpringLayout.Constraints btnConsM = layout.getConstraints(moveButton);
        btnConsM.setX(Spring.constant(25));
        btnConsM.setY(Spring.sum(textFieldCons.getConstraint(SpringLayout.SOUTH), Spring.constant(10)));
        btnConsM.setConstraint(SpringLayout.HEIGHT, Spring.constant(70));
        btnConsM.setConstraint(SpringLayout.WIDTH, Spring.constant(150));

        SpringLayout.Constraints btnConsA = layout.getConstraints(attackButton);
        btnConsA.setX(Spring.sum(Spring.constant(25),btnConsM.getConstraint(SpringLayout.EAST)));
        btnConsA.setY(Spring.sum(textFieldCons.getConstraint(SpringLayout.SOUTH), Spring.constant(10)));
        btnConsA.setConstraint(SpringLayout.HEIGHT, Spring.constant(70));
        btnConsA.setConstraint(SpringLayout.WIDTH, Spring.constant(150));

        SpringLayout.Constraints btnConsU = layout.getConstraints(upgradeButton);
        btnConsU.setX(Spring.sum(Spring.constant(25),btnConsA.getConstraint(SpringLayout.EAST)));
        btnConsU.setY(Spring.sum(textFieldCons.getConstraint(SpringLayout.SOUTH), Spring.constant(10)));
        btnConsU.setConstraint(SpringLayout.HEIGHT, Spring.constant(70));
        btnConsU.setConstraint(SpringLayout.WIDTH, Spring.constant(150));

        SpringLayout.Constraints btnConsT = layout.getConstraints(upgradeTechButton);
        btnConsT.setX(Spring.sum(Spring.constant(25),btnConsU.getConstraint(SpringLayout.EAST)));
        btnConsT.setY(Spring.sum(textFieldCons.getConstraint(SpringLayout.SOUTH), Spring.constant(10)));
        btnConsT.setConstraint(SpringLayout.HEIGHT, Spring.constant(70));
        btnConsT.setConstraint(SpringLayout.WIDTH, Spring.constant(150));

        SpringLayout.Constraints btnConsC = layout.getConstraints(commitButton);
        btnConsC.setX(Spring.sum(Spring.constant(25),btnConsT.getConstraint(SpringLayout.EAST)));
        btnConsC.setY(Spring.sum(textFieldCons.getConstraint(SpringLayout.SOUTH), Spring.constant(10)));
        btnConsC.setConstraint(SpringLayout.HEIGHT, Spring.constant(70));
        btnConsC.setConstraint(SpringLayout.WIDTH, Spring.constant(150));

        SpringLayout.Constraints btnConsD = layout.getConstraints(doneButton);
        btnConsD.setX(Spring.sum(Spring.constant(25),btnConsC.getConstraint(SpringLayout.EAST)));
        btnConsD.setY(Spring.sum(textFieldCons.getConstraint(SpringLayout.SOUTH), Spring.constant(10)));
        btnConsD.setConstraint(SpringLayout.HEIGHT, Spring.constant(70));
        btnConsD.setConstraint(SpringLayout.WIDTH, Spring.constant(100));


        panel2.add(moveButton);
        panel2.add(attackButton);
        panel2.add(upgradeButton);
        panel2.add(upgradeTechButton);
        panel2.add(commitButton);
        panel2.add(doneButton);
        panel2.add(commitOnceButton);

        //panel2.add(MapField);
        panel2.add(textField);
        panel2.add(gameInfoField);
        panel2.add(actionHistoryField);

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
    private void getMovementName(JButton button, final String moveName){
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
                    b.setCanClickSelf(territories,player.getId());
                    if(b.canClick) {
                        b.setEnabled(true);
                        b.act(stringBuffer);
                    }
                    else b.setEnabled(false);
                }
            }
            if(moveName.equals("D")) {
                //commited = true;
                try {
                    System.setIn(new FileInputStream("./history"+player.getId()+".txt"));
                    sc=new Scanner(System.in);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
                System.out.println("click Commit all,now it is true");
                player.addAction(territories,name,sc);
                try {
                    os1.writeObject(player);
                    os1.flush();
                    os1.reset();
                    System.out.println("sending player...");
                    while (true) {
                        //System.out.println("receiving the player object from server");
                        player = (Player) is1.readObject();
                        if (player.isvalid) {
                            System.out.println("Waiting for other players'input....");
                            //System.out.println("------------------------");
                            break;
                        }

                        System.out.println("!!!!!!!!!!!!!!!!!!!!!!");
                        System.out.println("Collision! Type again");
                        System.out.println("!!!!!!!!!!!!!!!!!!!!!!");
                        player.addAction(territories, get_name(),sc);
                        os1.writeObject(player);
                        os1.flush();
                        os1.reset();
                    }
                    territories = (Map<String, Territory>) is1.readObject();
                    setAttributes(player,territories);

                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }


        });
    }

    private void mapButtonAction(){
        for(MapButton b : this.mapButtons){
            b.addActionListener(e -> {
                String name = b.getName();
                Territory t = territories.get(name);
                gameInfoField.setText("");
                StringBuilder s = new StringBuilder();
                s.append("This is territory ").append(t.getName()).append(", owned by ").append(t.getOwner()).append(" now.\r\n");
                for(int i = 0; i < 7; i++){
                    s.append("It has ").append(t.getSoldierNumOfLevel(i)).append(" level ").append(i).append(" soldiers.\r\n");
                }
                gameInfoField.append(String.valueOf(s));
            });
        }
    }

    private void attackCommand(Map<String, Territory> territories){
        getMovementName(attackButton,"A");
    }

    private void moveCommand(Map<String, Territory> territories){
        getMovementName(moveButton,"M");
    }

    private void UpdateTechCommand(Map<String, Territory> territories){
        getMovementName(upgradeTechButton,"T");
    }

    private void UpgradeCommand(Map<String, Territory> territories){
        getMovementName(upgradeButton,"U");
    }

    private void commitCommand(Map<String, Territory> territories){
        getMovementName(commitButton,"D");
    }

    //好像是一个run forever的东西？？？？
    public void setListener() {
        //setLayout();
        mapButtonAction();
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
                    b.setCanClickEnemy(territories, player.getId());
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

    public String get_name(){
        return this.name;
    }

    @Override
    public void run() {
        Socket socket = null;
        Executor end_helper= new Executor();
        try {
            socket = new Socket("localhost", 8000);
            System.out.println("Client Connected");
            this.os1 = new ObjectOutputStream(socket.getOutputStream());
            this.is1 = new ObjectInputStream(socket.getInputStream());

            //receive player
            Player player = (Player) is1.readObject();
            setName(player.getId());
            //receive map to be completed
            Map<String, Territory> territories = (Map<String, Territory>) is1.readObject();
//new Added!
            //fill the map in player
            HashMap<String, Integer> init_info = new HashMap<>();
            player.initial_game(territories, sc, totalsoldier, init_info);

            //send it to server
            os1.writeObject(init_info);
            os1.flush();
            os1.reset();
            //wait server send back completed map
            territories = (Map<String, Territory>) is1.readObject();


            setAttributes(player, territories);
            setLayout();
            setListener();
            //now game begins
            //System.setIn(new FileInputStream("./history"+player.getId()+".txt"));
            int times=0;
            while(true) {
                if(end_helper.checkWin(territories)) break;
            }
            //tell server it will close the client
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(socket.getOutputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        String msg="exit";
            pw.println(msg);
            pw.flush();

            //close resources
            pw.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try{
                System.out.println("Closing Client Socket");
                assert socket != null;
                socket.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    private void setName(int id) {
        this.name = String.valueOf(id);
    }

    public static void main(String[] args) throws Exception {

        Client c = new Client();
        c.start();
    }


}










