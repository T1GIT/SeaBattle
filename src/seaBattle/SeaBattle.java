package seaBattle;

import seaBattle.modes.GameMode;
import seaBattle.modes.Multiplayer;
import seaBattle.modes.Single;
import seaBattle.players.types.*;


public class SeaBattle {


    public void start() {
        try {
            System.out.println("\n" +
                    "█████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████\n" +
                    "█░░░░░░░░░░░░░░█░░░░░░░░░░░░░░█░░░░░░░░░░░░░░████░░░░░░░░░░░░░░███░░░░░░░░░░░░░░█░░░░░░░░░░░░░░█░░░░░░░░░░░░░░█░░░░░░█████████░░░░░░░░░░░░░░█\n" +
                    "█░░▄▀▄▀▄▀▄▀▄▀░░█░░▄▀▄▀▄▀▄▀▄▀░░█░░▄▀▄▀▄▀▄▀▄▀░░████░░▄▀▄▀▄▀▄▀▄▀░░███░░▄▀▄▀▄▀▄▀▄▀░░█░░▄▀▄▀▄▀▄▀▄▀░░█░░▄▀▄▀▄▀▄▀▄▀░░█░░▄▀░░█████████░░▄▀▄▀▄▀▄▀▄▀░░█\n" +
                    "█░░▄▀░░░░░░░░░░█░░▄▀░░░░░░░░░░█░░▄▀░░░░░░▄▀░░████░░▄▀░░░░░░▄▀░░███░░▄▀░░░░░░▄▀░░█░░░░░░▄▀░░░░░░█░░░░░░▄▀░░░░░░█░░▄▀░░█████████░░▄▀░░░░░░░░░░█\n" +
                    "█░░▄▀░░█████████░░▄▀░░█████████░░▄▀░░██░░▄▀░░████░░▄▀░░██░░▄▀░░███░░▄▀░░██░░▄▀░░█████░░▄▀░░█████████░░▄▀░░█████░░▄▀░░█████████░░▄▀░░█████████\n" +
                    "█░░▄▀░░░░░░░░░░█░░▄▀░░░░░░░░░░█░░▄▀░░░░░░▄▀░░████░░▄▀░░░░░░▄▀░░░░█░░▄▀░░░░░░▄▀░░█████░░▄▀░░█████████░░▄▀░░█████░░▄▀░░█████████░░▄▀░░░░░░░░░░█\n" +
                    "█░░▄▀▄▀▄▀▄▀▄▀░░█░░▄▀▄▀▄▀▄▀▄▀░░█░░▄▀▄▀▄▀▄▀▄▀░░████░░▄▀▄▀▄▀▄▀▄▀▄▀░░█░░▄▀▄▀▄▀▄▀▄▀░░█████░░▄▀░░█████████░░▄▀░░█████░░▄▀░░█████████░░▄▀▄▀▄▀▄▀▄▀░░█\n" +
                    "█░░░░░░░░░░▄▀░░█░░▄▀░░░░░░░░░░█░░▄▀░░░░░░▄▀░░████░░▄▀░░░░░░░░▄▀░░█░░▄▀░░░░░░▄▀░░█████░░▄▀░░█████████░░▄▀░░█████░░▄▀░░█████████░░▄▀░░░░░░░░░░█\n" +
                    "█████████░░▄▀░░█░░▄▀░░█████████░░▄▀░░██░░▄▀░░████░░▄▀░░████░░▄▀░░█░░▄▀░░██░░▄▀░░█████░░▄▀░░█████████░░▄▀░░█████░░▄▀░░█████████░░▄▀░░█████████\n" +
                    "█░░░░░░░░░░▄▀░░█░░▄▀░░░░░░░░░░█░░▄▀░░██░░▄▀░░████░░▄▀░░░░░░░░▄▀░░█░░▄▀░░██░░▄▀░░█████░░▄▀░░█████████░░▄▀░░█████░░▄▀░░░░░░░░░░█░░▄▀░░░░░░░░░░█\n" +
                    "█░░▄▀▄▀▄▀▄▀▄▀░░█░░▄▀▄▀▄▀▄▀▄▀░░█░░▄▀░░██░░▄▀░░████░░▄▀▄▀▄▀▄▀▄▀▄▀░░█░░▄▀░░██░░▄▀░░█████░░▄▀░░█████████░░▄▀░░█████░░▄▀▄▀▄▀▄▀▄▀░░█░░▄▀▄▀▄▀▄▀▄▀░░█\n" +
                    "█░░░░░░░░░░░░░░█░░░░░░░░░░░░░░█░░░░░░██░░░░░░████░░░░░░░░░░░░░░░░█░░░░░░██░░░░░░█████░░░░░░█████████░░░░░░█████░░░░░░░░░░░░░░█░░░░░░░░░░░░░░█\n" +
                    "█████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████");

            System.out.println("\n\n        Hello, my Dear Friend! Let's play in SEA BATTLE ヽ(*⌒▽⌒*)ﾉ\n\n" +
                    ">>> How I can call You? (ಠ_ಠ)");
            String userName = UI.input.name();
            UI.print.line();
            System.out.println("                         Nice to meet You, " + userName + "! (*^‿^*)");
            UI.print.line();
            System.out.println("Please choose mode for game: \n" +
                    "    0 - Single (Fight with local players)\n" +
                    "    1 - Multiplayer (Play with gamers around the world)\n" +
                    ">>> Make your choice ヽ(o^―^o)ﾉ");
            int gameMode = UI.input.mode(1);
            GameMode game;
            switch (gameMode) {
                case 0: game = new Single(userName); break;
                case 1: game = new Multiplayer(userName); break;
                default: throw new IllegalStateException("Unknown game mode " + gameMode);
            }
            game.play();
        } catch (UI.input.CommandException e) {
            String[] name = e.getClass().getCanonicalName().split("\\.");
            switch (name[name.length - 1]) {
                case "Exit":
                    System.exit(69);
                    break;
                case "Reset":
                    UI.print.space();
                    start();
                default:
                    e.printStackTrace();
            }
        }
    }
}
