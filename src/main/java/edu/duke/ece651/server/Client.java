/*package edu.duke.ece651.server;

import java.util.ArrayList;
import java.util.HashMap;

public class Client {
    //get them from the server
    private Player player;
    //key is player id, in convenience for display the relevant info
    private HashMap<Integer, ArrayList<Territory>> graphInformation;


*/

package edu.duke.ece651.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Client extends Thread {
    String name;
    private Scanner sc=new Scanner(System.in);


    Client(String client_name) {
        this.name = client_name;
    }
    public String get_name(){
        return this.name;
    }
    @Override
    public void run() {
        Socket socket = null;
        Executor end_helper=new Executor();
        try {
            socket = new Socket("localhost", 8080);
            System.out.println("Client Connected");

            ObjectOutputStream os1 = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream is1 = new ObjectInputStream(socket.getInputStream());

            //receive player
            Player player = (Player) is1.readObject();
            //receive territories
            Map<String, Territory> territories = (Map<String, Territory>) is1.readObject();

            while(true) {
                //Map<String, Territory> new_territories = (Map<String, Territory>) is.readObject();
                //if reaching the end of game
                if(!end_helper.checkWin(territories)) break;
                //------------------------------------//
                //create action list
                player.addAction(territories, get_name(),sc);
                //send player to server
                os1.writeObject(player);
                os1.flush();
                //waiting for server's reply of validating
                while (true) {
                    ObjectInputStream is2 = new ObjectInputStream(socket.getInputStream());
                    Player temp = (Player) is2.readObject();
                    if (temp.isvalid = true) {
                       // is2.close();
                        break;
                    }
                    System.out.println("Collision actions,type again");
                    player.addAction(territories, get_name(),sc);
                    os1.writeObject(player);
                    os1.flush();
                    is2.close();
                }
                ObjectInputStream is3 = new ObjectInputStream(socket.getInputStream());
                territories = (Map<String, Territory>) is3.readObject();
            }
            //tell server it will close the client
            PrintWriter pw = new PrintWriter(socket.getOutputStream());
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



    public static void main() throws IOException, InterruptedException {
        Client c1=new Client("a");
        Client c2=new Client("b");
        Client c3=new Client("c");
        c1.start();
        TimeUnit.SECONDS.sleep(1);
        c2.start();
        TimeUnit.SECONDS.sleep(1);
        c3.start();

    }
}










