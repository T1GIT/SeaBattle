package seaBattle;

import seaBattle.modes.GameMode;
import seaBattle.modes.types.Multiplayer;
import seaBattle.modes.types.Single;
import seaBattle.players.types.UI;

import java.util.InputMismatchException;


/**
 * The Java realisation of the game Battleship.
 * 
 * <p>Game going through players in the room. The room may
 *  contains 2 or more players. If the room includes more
 *  then 2 players than game going through cyclic. Example:
 *  We have room with 3 players: A, B, C.
 *  Games looks so:
 *  1. A attacks B
 *  2. B attacks C
 *  3. C attacks A
 *  ... again in the same way
 *  When one of them won't have alive boats he'll lose
 *  and skipped. Player before them will attack next
 *  player. Game continues while all players almost one
 *  will lose.
 *
 * <p>This game provides two types of player: PC and UI.
 * You can choose every player whom it can be.
 *
 * <p>You can play via the internet. And you can secure
 * your room with password. In web mode you can chat
 * with all competitors. Just type "chat <message>"
 * when coordinates for attack was requested. And in any
 * mode you can make your computer play for you, for
 * that you should call yourself "PC".
 *
 * @author T1MON
 * @version 1.0
 */
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
            int gameMode = UI.input.variant(2, "Game mode");
            GameMode game = switch (gameMode) {
                case 0 -> new Single();
                case 1 -> new Multiplayer();
                default -> throw new IllegalStateException("Unknown game mode " + gameMode);
            };
            // Running
            game.play(userName);
            // Ended
            System.out.println("Is it time to say good bye? (>﹏<)");
            try { UI.input.command(); }
            catch (InputMismatchException e) { throw new UI.input.CommandException.Exit(); }
            // Catches commands from user
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
