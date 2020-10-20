import seaBattle.SeaBattle;
import seaBattle.server.Client;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
//        SeaBattle newGame = new SeaBattle();
//        newGame.start();
        Client a = new Client();
        a.connect();
    }
}
