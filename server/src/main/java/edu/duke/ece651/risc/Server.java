package edu.duke.ece651.risc;

public class Server {
    public static void main(String[] args) throws Exception {
        int game_num = 2;
        int player_num = 2;
        for (int i = 0; i < game_num; i++) {
            Game game = new Game(8000 + i, player_num);
            game.start();
        }
    }
}
