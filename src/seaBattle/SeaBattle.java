package seaBattle;

import seaBattle.modes.GameMode;
import seaBattle.modes.types.Multiplayer;
import seaBattle.modes.types.Single;
import seaBattle.players.types.*;


public class SeaBattle {
    public static void main(String[] args) {
        try {
            // Meeting
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
            String userName = UI.input.playerName();
            UI.print.line();
            System.out.println("                         Nice to meet You, " + userName + "! (*^‿^*)");
            UI.print.line();
            // Mode choosing
            System.out.println("Please choose mode for game: \n" +
                    "    0 - Single (Fight with local players)\n" +
                    "    1 - Multiplayer (Play with gamers around the world)\n" +
                    ">>> Make your choice ヽ(o^―^o)ﾉ");
            int gameMode = UI.input.mode(2);
            GameMode game = switch (gameMode) {
                case 0 -> new Single();
                case 1 -> new Multiplayer();
                default -> throw new IllegalStateException("Unknown game mode " + gameMode);
            };
            // Running
            UI.print.space();
            game.play(userName);
            // Catches commands from user
            // TODO: Catch com. exceptions in the Multiplayer and close the socket then throw them forward
        } catch (UI.input.CommandException e) {
            String[] name = e.getClass().getCanonicalName().split("\\.");
            switch (name[name.length - 1]) {
                case "Exit" -> System.exit(69);
                case "Reset" -> {
                    UI.print.space();
                    main(null);
                }
                default -> e.printStackTrace();
            }
        }
    }
}
