package edu.duke.ece651.risc;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.*;
import java.net.Socket;
import java.util.*;
//import java.util.concurrent.Executor;


public class Client extends Thread {
    //as a client------------------
    String name;
    private Scanner sc=new Scanner(System.in);
    private ObjectOutputStream os1 = null;
    private ObjectInputStream is1 = null;
    final int totalSoldiers =3;
    private Socket socket=null;

    ////---------------------------
    ///-----As an interface--------
    ////---------------------------
    //private JTextField playerInfo
    private JButton moveButton = new JButton("Move");
    private JButton attackButton = new JButton("Attack");
    private JButton upgradeButton = new JButton("Upgrade Unit");
    private JButton upgradeTechButton = new JButton("Upgrade Tech");
    private JButton commitButton = new JButton("Commit All Actions");
    private JButton commitOnceButton=new JButton(("Commit Current Action"));
    private JButton doneButton=new JButton(("Done"));

    private JTextArea MapField = new JTextArea("Map");
    private JTextArea textField = new JTextArea("Game prompts:\n");
    private JTextArea gameInfoField = new JTextArea("Game Information:");
    private JTextArea actionField = new JTextArea();
    private JTextArea actionHistoryField = new JTextArea("Action History: \r\n\r\n");
    private JTextArea playerInfo = new JTextArea("Player Information: \r\n\r\n");
    JScrollPane scrollPane = new JScrollPane(actionHistoryField);

    JFrame jf = new JFrame("RISC evolution2");
    SpringLayout layout;
    JPanel panel2;
    private ArrayList<MapButton> mapButtons=new ArrayList<>();

    boolean sameAround=false;

    private int action = 0;
    private Player player=null;
    private Map<String, Territory> territories=null;
    ArrayList<String> stringBuffer=new ArrayList<>();


    //--------------------------------
    //FOR display the action history--
    //--------------------------------
    private StringBuilder actionPrompt = new StringBuilder();
    private String actionName = "";
    private String src = "";
    private String dest = "";
    private String temp = "";
    private ArrayList<Integer> unitsNumber = new ArrayList<>();
    //--------------------------------
    // FOR display the prompts info--
    //--------------------------------
    private int currentLevel=0;

    //--------------------------------
    // FOR choose the each level soldiers
    //--------------------------------
    private JTextArea SoldierField = new JTextArea("");
    JComboBox<String> c0 = new JComboBox<>();
    JComboBox<String> c1 = new JComboBox<>();
    JComboBox<String> c2 = new JComboBox<>();
    JComboBox<String> c3 = new JComboBox<>();
    JComboBox<String> c4 = new JComboBox<>();
    JComboBox<String> c5 = new JComboBox<>();
    JComboBox<String> c6 = new JComboBox<>();
    ArrayList<JComboBox<String>> boxes = new ArrayList<>();



    public void setAttributes(Player player, Map<String, Territory> territories){
        this.player=player;
        this.territories=territories;
    }
    public void setPlayerInfo(){
        String s = "Hi, welcome player " + player.getId() + ".\r\n" +
                "Now you are in TECHNIQUE level " + player.getTechLevel() + ".\r\n" +
                "You have " + player.getFoodResources() + " food resources.\r\n" +
                "You have " + player.getTechResources() + " tech resources.\r\n";
        playerInfo.setText("");
        playerInfo.append(s);
    }

    private void setBox() {
        for (int i = 0; i < 7; i++) {
            String temp = "Level-" + String.valueOf(i);
            temp += "                 ";
            this.SoldierField.append(temp);
        }
        this.boxes.add(c0);
        this.boxes.add(c1);
        this.boxes.add(c2);
        this.boxes.add(c3);
        this.boxes.add(c4);
        this.boxes.add(c5);
        this.boxes.add(c6);
        setBoxItems();
    }

    private void setBoxItems(){
        for(int i = 0; i < 31; i++){
            for (JComboBox<String> box : this.boxes) {
                if(i == 0){
                    box.addItem("-");
                }
                else{
                    box.addItem(String.valueOf(i - 1));
                }
            }
        }
    }

    private void setInitialInfo(){
        //generate the map
        int playerNumber = territories.size() / 3;
        //System.out.println(playerNumber);
        for(int i = 0; i < playerNumber; i++) {
            for (int j = 0; j < 3; j++) {
                char c = (char) ('a' + i);
                String name = c + String.valueOf(j);
                this.mapButtons.add(new MapButton(true, name));
            }
        }
        setPlayerInfo();

    }
    private void setLayout(){
        //other components
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
        MapFieldCons.setConstraint(SpringLayout.HEIGHT,Spring.constant(250));
        MapFieldCons.setConstraint(SpringLayout.WIDTH,Spring.constant(675));
        MapField.setEditable(false);
        //soldiers
        setBox();
        SpringLayout.Constraints soldierFieldCons = layout.getConstraints(SoldierField);
        soldierFieldCons.setX(Spring.constant(25));
        soldierFieldCons.setY(Spring.sum(MapFieldCons.getConstraint(SpringLayout.SOUTH),Spring.constant(100)));
        soldierFieldCons.setConstraint(SpringLayout.HEIGHT,Spring.constant(20));
        soldierFieldCons.setConstraint(SpringLayout.WIDTH,Spring.constant(675));
        SoldierField.setEditable(false);

        SpringLayout.Constraints textFieldCons = layout.getConstraints(textField);
        textFieldCons.setX(Spring.constant(25));
        textFieldCons.setY(Spring.sum(MapFieldCons.getConstraint(SpringLayout.SOUTH),Spring.constant(210)));
        textFieldCons.setConstraint(SpringLayout.HEIGHT,Spring.constant(200));
        textFieldCons.setConstraint(SpringLayout.WIDTH,Spring.constant(675));
        textField.setEditable(false);
        Font font=new Font("buzhidao",Font.PLAIN,12);
        textField.setFont(font);
        textField.setForeground(Color.BLUE);

        SpringLayout.Constraints playerInfoFieldCons = layout.getConstraints(playerInfo);
        playerInfoFieldCons.setX(Spring.sum(MapFieldCons.getConstraint(SpringLayout.EAST),Spring.constant(10)));
        playerInfoFieldCons.setY(MapFieldCons.getConstraint(SpringLayout.NORTH));
        playerInfoFieldCons.setConstraint(SpringLayout.HEIGHT,Spring.constant(100));
        playerInfoFieldCons.setConstraint(SpringLayout.WIDTH,Spring.constant(315));
        playerInfo.setEditable(false);

        SpringLayout.Constraints gameInfoFieldCons = layout.getConstraints(gameInfoField);
        gameInfoFieldCons.setX(Spring.sum(MapFieldCons.getConstraint(SpringLayout.EAST),Spring.constant(10)));
        gameInfoFieldCons.setY(Spring.sum(playerInfoFieldCons.getConstraint(SpringLayout.SOUTH),Spring.constant(10)));
        gameInfoFieldCons.setConstraint(SpringLayout.HEIGHT,Spring.constant(170));
        gameInfoFieldCons.setConstraint(SpringLayout.WIDTH,Spring.constant(315));
        gameInfoField.setEditable(false);

        SpringLayout.Constraints actionHistoryFieldCons = layout.getConstraints(scrollPane);
        actionHistoryFieldCons.setX(Spring.sum(MapFieldCons.getConstraint(SpringLayout.EAST),Spring.constant(10)));
        actionHistoryFieldCons.setY(Spring.sum(gameInfoFieldCons.getConstraint(SpringLayout.SOUTH),Spring.constant(10)));
        actionHistoryFieldCons.setConstraint(SpringLayout.HEIGHT,Spring.constant(340));
        actionHistoryFieldCons.setConstraint(SpringLayout.WIDTH,Spring.constant(315));
        actionHistoryField.setEditable(false);


        SpringLayout.Constraints actionFieldCons = layout.getConstraints(actionField);
        actionFieldCons.setX(gameInfoFieldCons.getConstraint(SpringLayout.WEST));
        actionFieldCons.setY(Spring.sum(actionHistoryFieldCons.getConstraint(SpringLayout.SOUTH),Spring.constant(10)));
        actionFieldCons.setConstraint(SpringLayout.HEIGHT,Spring.constant(20));
        actionFieldCons.setConstraint(SpringLayout.WIDTH,Spring.constant(315));
        actionField.setEditable(true);


        //button constrains
        SpringLayout.Constraints btnConss = layout.getConstraints(commitOnceButton);
        btnConss.setConstraint(SpringLayout.HEIGHT,Spring.constant(50));

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


        panel2.add(moveButton);
        panel2.add(attackButton);
        panel2.add(upgradeButton);
        panel2.add(upgradeTechButton);
        panel2.add(commitButton);
        panel2.add(doneButton);
        panel2.add(commitOnceButton);
        panel2.add(SoldierField);

        panel2.add(textField);
        panel2.add(gameInfoField);
        panel2.add(actionField);
        panel2.add(playerInfo);
        //panel2.add(actionHistoryField);
        panel2.add(scrollPane);

        //add all map
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
        //scrollPane_1.setViewportView(this.actionHistoryField);
        //panel2.add(scrollPane);
        int i = 0;
        for(JComboBox<String> box : this.boxes){
            SpringLayout.Constraints boxCons = layout.getConstraints(box);
            boxCons.setX(Spring.sum(Spring.constant(i * 101),MapFieldCons.getConstraint(SpringLayout.WEST)));
            boxCons.setY(Spring.sum(Spring.constant(10),soldierFieldCons.getConstraint(SpringLayout.SOUTH)));
            panel2.add(box);
            i++;
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
    private void getMovementName(JButton button, final String actionName){
        button.addActionListener(e -> {
            switch (actionName) {
                case "M":
                    //this.currentLevel=0;
                    this.actionName = "Move";
                    textField.setText("");
                    textField.append("Move Instruction:\n\n");
                    textField.append("1.Choose the source territory,then click Done button.\n");
                    textField.append("2.Choose the number of soldiers you want to move in each level,then click Done button.\n\n");
                    textField.append("Attention!:Do Not Leave Num Default\n\n");
                    textField.append("3.Choose the source territory,then click Done button.\n");
                    textField.append("4.Click the left-top button to commit current single action.\n");
                    break;
                case "A":
                    action = 1;
                    //this.currentLevel=0;
                    this.actionName = "Attack";
                    textField.setText("");
                    textField.append("Attack Instruction:\n\n");
                    textField.append("1.Choose the source territory,then click Done button.\n");
                    textField.append("2.Choose the number of soldiers you want to assign to attack in each level,then click Done button.\n\n");
                    textField.append("Attention!:Do Not Leave Num Default\n\n");
                    textField.append("3.Choose your enemy territory,then click Done button.\n");
                    textField.append("4.Click the left-top button to commit current single action.\n");
                    break;
                case "U":
                    action = 2;
                    this.actionName = "Update";
                    textField.setText("");
                    textField.append("Upgrade Unit Instruction:\n\n");
                    textField.append("1.Choose the source territory the units in,then click Done button.\n");
                    textField.append("2.Type assigned Units' current level ,then click Done button.\n");
                    textField.append("3.Type the number of soldiers you want to upgrade  ,then click Done button.\n");
                    textField.append("4.Type the level you want to upgrade to ,then click Done button.\n");
                    textField.append("5.Click the left-top button to commit current single action.\n");
                    break;
                case "T":
                    textField.setText("");
                    textField.append("Upgrade Tech Instruction:\n\n");
                    //textField.append("1.Choose the source territory the units in,then click Done button.\n");
                    textField.append("1.Click the left-top button to commit current single action.\n");
                    //textField.append("2.Click the left-top button to commit current single action.\n");

                    this.actionName = "Update Technique";
                    break;
            }

            if(!actionName.equals("D"))
                setEnableAllButton(false);
            BufferedWriter bw1;
            try {
                bw1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("./history"+player.getId()+".txt"),sameAround)));
                sameAround = !actionName.equals("D");
                bw1.write(actionName+"\n");
                bw1.close();

            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if(actionName.equals("A") || actionName.equals("U") || actionName.equals("M")) {
                for(MapButton b:mapButtons){
                    b.setCanClickSelf(territories,player.getId());
                    if(b.canClick) {
                        b.setEnabled(true);
                        b.act(stringBuffer);
                    }
                    else b.setEnabled(false);
                }
            }
            //if it is commitAllActions
            if(actionName.equals("D")) {
                Executor endHelper=new Executor();
                actionHistoryField.setText("");
                try {
                    System.setIn(new FileInputStream("./history"+player.getId()+".txt"));
                    sc=new Scanner(System.in);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
                System.out.println("click Commit all");
                if(!player.addAction(territories,name,sc)){
                    //if the given format is wrong
                    textField.setText("");
                    textField.append("Your Given Input Order is Wrong, Please Follow the Instruction!");
                    setEnableAllButton(true);
                    actionHistoryField.setText("Action History: \r\n\r\n");
                    return;
                }else {
                    textField.setText("");
                    textField.append("Waiting for other players'input....\n");
                }
                try {
                    os1.writeObject(player);
                    os1.flush();
                    os1.reset();
                    System.out.println("sending player...");
                    //this.textField.append("Sending Information to Server for Validating\n");
                    //System.out.println("receiving the player object from server");
                    this.player = (Player) is1.readObject();
                    if (player.isvalid) {
                        System.out.println("Waiting for other players'input....");

                        territories = (Map<String, Territory>) is1.readObject();
                        if(!endHelper.checkWin(territories,this.textField)) {
                            if(!endHelper.singlePlayerFail(this.territories,this.player.getId())) {
                                this.textField.append("This is New Round\n");
                                setAttributes(player, territories);
                                setEnableAllButton(true);
                                setPlayerInfo();
                                return;
                            }
                            else{
                                textField.setText("");
                                textField.append("Sorry, You Lose!");
                                endGame();
                            }
                        }
                        endGame();
                    }
                    //else , it is not valid
                    System.out.println("Collision! Type again");
                    this.textField.setText("");
                    this.textField.append("!!!!!!!!!!!!!!!!!!!!!!\n");
                    this.textField.append("Collision! Operate Again\n");
                    this.textField.append("!!!!!!!!!!!!!!!!!!!!!!\n");
                    setEnableAllButton(true);
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
    private void commitOnceCommand(){
        commitOnceButton.addActionListener(e -> {
            action = 0;
            setEnableAllButton(true);
            for(MapButton b : mapButtons){
                b.setEnabled(true);
            }

            if(this.actionName.equals("Move") || this.actionName.equals("Attack")){
                actionPrompt.append(this.actionName).append(" from ").append(this.src).append(" to ").append(this.dest).append(": ");
                for(int i = 0; i < this.unitsNumber.size(); i++){
                    if(this.unitsNumber.get(i) != 0){
                        actionPrompt.append("\n").append(this.unitsNumber.get(i)).append("  level-").append(i).append(" soldiers").append(".");
                    }
                }
            }
            else if(this.actionName.equals("Update Technique")){
                actionPrompt.append("Update Technique");
            }
            else{
                actionPrompt.append("Update soldiers: ").append(this.unitsNumber.get(1)).append("    level-").append(this.unitsNumber.get(0));
                actionPrompt.append(" solider(s) to level ").append(this.unitsNumber.get(2));
            }
            actionPrompt.append("\n=================================================\n");
            actionHistoryField.append(String.valueOf(actionPrompt));
            actionPrompt = new StringBuilder();
            actionName = "";
            src = "";
            dest = "";
            temp = "";
            unitsNumber = new ArrayList<>();
        });
    }

    private void mapButtonAction(){
        for(MapButton b : this.mapButtons){
            b.addActionListener(e -> {
                String name = b.getName();
                this.temp = name;
                Territory t = territories.get(name);
                gameInfoField.setText("Game Information:\r\n\r\n");
                StringBuilder s = new StringBuilder();
                s.append("This is territory ").append(t.getName()).append(", owned by ").append(t.getOwner()).append(" now.\r\n");
                for(int i = 0; i < 7; i++){
                    s.append("It has   ").append(t.getSoldierNumOfLevel(i)).append("   level-").append(i).append(" soldiers.\r\n");
                }
                gameInfoField.append(String.valueOf(s));
            });
        }
    }
    private void doneButton(){
        doneButton.addActionListener(e -> {
            //Validator helper = new Validator();
            String content = actionField.getText();
            actionField.setText("");

            if (!content.equals("")) {

                this.unitsNumber.add(Integer.parseInt(content));
                //this.currentLevel++;
                BufferedWriter bw1;
                try {
                    bw1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("./history"+player.getId()+".txt"), sameAround)));
                    bw1.write(content + "\n");
                    bw1.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            String start = (String) this.boxes.get(0).getSelectedItem();
            assert start != null;
            if (!start.equals("-")){
                for (JComboBox<String> box : this.boxes) {
                    String temp = (String) box.getSelectedItem();
                    assert temp != null;
                    this.unitsNumber.add(Integer.parseInt(temp));
                    BufferedWriter bw1;
                    try {
                        bw1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("./history"+player.getId()+".txt"), sameAround)));
                        bw1.write(temp + "\n");
                        bw1.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                for (JComboBox<String> box : this.boxes) {
                    box.removeAllItems();
                }
                setBoxItems();
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
                    bw1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("./history"+player.getId()+".txt"), sameAround)));
                    bw1.write(stringBuffer.get(stringBuffer.size() - 1) + "\n");
                    bw1.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            //Action History Addition
            if(this.src.equals("")){
                this.src = this.temp;
            }
            else{
                this.dest = this.temp;
            }
            stringBuffer.clear();
        });
    }

    private void attackCommand(){
        getMovementName(attackButton,"A");
    }
    private void moveCommand(){
        getMovementName(moveButton,"M");
    }
    private void UpdateTechCommand(){
        getMovementName(upgradeTechButton,"T");
    }
    private void UpgradeCommand(){
        getMovementName(upgradeButton,"U");
    }
    private void commitCommand(){
        getMovementName(commitButton,"D");
    }

    //好像是一个run forever的东西？？？？
    public void setListener() {
        //setLayout();
        moveCommand();
        attackCommand();
        UpdateTechCommand();
        UpgradeCommand();
        commitCommand();
        mapButtonAction();
        commitOnceCommand();
        doneButton();
    }

    private void starWork(){
        setInitialInfo();
        setLayout();
        setListener();
    }

    //---------------------
    //as a normal client---
    //---------------------
    public String get_name(){
        return this.name;
    }

    @Override
    public void run() {
        //Socket socket = null;
        Executor end_helper= new Executor();
        try {
            System.out.println("Choose one game to play (start from 0):");
            int port = Integer.parseInt(sc.nextLine());

            socket = new Socket("localhost", 8000 + port);
            System.out.println("Client Connected");
            this.os1 = new ObjectOutputStream(socket.getOutputStream());
            this.is1 = new ObjectInputStream(socket.getInputStream());

            //authenticate user
            boolean exists = authenticate();

            //receive player
            Player player = (Player) is1.readObject();
            setName(player.getId());
            //receive map to be completed
            Map<String, Territory> territories = (Map<String, Territory>) is1.readObject();
//new Added!
            //initialize map if user is new
            if (!exists) {
                territories = initMap(player, territories);
            }

            setAttributes(player, territories);
            starWork();
            //now game begins
            System.out.println("Welcome to our games, now enter GUI");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void endGame() throws IOException {
        setEnableAllButton(false);
        this.doneButton.setEnabled(false);
        this.commitOnceButton.setEnabled(false);
        this.commitButton.setEnabled(false);
        closeSocket();
    }
    private void closeSocket() throws IOException {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(socket.getOutputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        String msg="exit";
        assert pw != null;
        pw.println(msg);
        pw.flush();
        //close resources
        pw.close();

        //new added

        System.out.println("Closing Client Socket");
        assert socket != null;
        socket.close();

    }

    private boolean authenticate() throws IOException, ClassNotFoundException {
        //set username & password
        System.out.println("username:");
        String username = sc.nextLine();
        System.out.println("password:");
        String password = sc.nextLine();
        Credential credential = new Credential(username, password);
        //send username & password
        os1.writeObject(credential);
        os1.flush();
        os1.reset();
        //if user exists
        return (boolean) is1.readObject();
    }

    private Map<String, Territory> initMap(Player player, Map<String, Territory> territories) throws IOException, ClassNotFoundException {
        //fill the map in player
        HashMap<String, Integer> init_info = new HashMap<>();
        player.initial_game(territories, sc, totalSoldiers, init_info,textField);

        //send it to server
        os1.writeObject(init_info);
        os1.flush();
        os1.reset();
        //wait server send back completed map
        territories = (Map<String, Territory>) is1.readObject();
        return territories;
    }

    private void setName(int id) {
        this.name = String.valueOf(id);
    }

    public static void main(String[] args) throws Exception {

        Client c = new Client();
        c.start();
    }


}










