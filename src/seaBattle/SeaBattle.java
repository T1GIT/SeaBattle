package seaBattle;

import seaBattle.elements.Field;
import seaBattle.players.Player;
import seaBattle.players.types.*;

import java.util.Arrays;

public class SeaBattle {
    private final byte MAX_PLAYERS = 2;
    private final Player[] players = new Player[MAX_PLAYERS];

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
            players[0] = new UI(UI.input.name());
            UI.print.line();
            System.out.println("                         Nice to meet You, " + players[0].getName() + "! (*^‿^*)");
            UI.print.line();
            System.out.println("Please choose mode for game: \n" +
                    "    0 - Single (Fight with your PC)\n" +
                    "    1 - Multiplayer (Play with gamers around the world)\n" +
                    ">>> Make your choice ヽ(o^―^o)ﾉ");
            int gameMode = UI.input.mode(1);
            switch (gameMode) {
                case 0: single(); break;
                case 1: multiplayer(); break;
            }
        } catch (UI.input.CommandException.Reset e) {
            Arrays.fill(players, null);
            UI.print.space();
            start();
        } catch (UI.input.CommandException.Exit e) {
            System.exit(0);
        } catch (UI.input.CommandException e) {
            e.printStackTrace();
        }
    }

    private void single() throws UI.input.CommandException {
        UI.print.line();
        System.out.println("             Welcome to ＳＩＮＧＬＥ ＭＯＤＥ, Dear " + players[0].getName() + "! ヽ(・∀・)ﾉ");
        UI.print.line();
        System.out.println("It's time to choose your opponent: Q (｀⌒´Q)\n" +
                "    0 - PC (This machine is stupid, but lucky)\n" +
                "    1 - Player (Like PC, but on the contrary)\n" +
                ">>> Make your choice (◕‿◕)");
        int gameMode = UI.input.mode(1);
        UI.print.line();
        if (gameMode == 1) {
            System.out.println("Oh, it's incredible, two people in there (O.O)\n" +
                    ">>> What's your name, Stranger?");
            players[1] = new UI(UI.input.name());
        } else {
            System.out.println("Wow, good choice, our Terminator is already ready to win this game (⌐■_■)>");
            players[1] = new PC();
        }
        for (int i = 0; i < players.length; i++) {
            fillField(players[i]);
            if (i < (players.length - 1)) {
                if (players[i + 1].isHuman()) {
                    UI.print.space();
                }
            }
        }
        if (gameMode == 1) UI.print.space();
        System.out.println("\n\n              \uD835\uDD43\uD835\uDD56\uD835\uDD65❜\uD835\uDD64 \uD835\uDD64\uD835\uDD65\uD835\uDD52\uD835\uDD52\uD835\uDD52\uD835\uDD52\uD835\uDD52\uD835\uDD52\uD835\uDD52\uD835\uDD52\uD835\uDD63\uD835\uDD65!  ►\n\n");
        String winnerName = mainLoop();
        StringBuilder repName = new StringBuilder();
        do { repName.append(winnerName); }
        while (repName.length() < 50);
        System.out.println("\n\n" +
                "                ▒█░░▒█ ▀█▀ ▒█▄░▒█ \n" +
                "                ▒█▒█▒█ ▒█░ ▒█▒█▒█ \n" +
                "                ▒█▄▀▄█ ▄█▄ ▒█░░▀█ \n\n" +
                "       (⌐■_■)       " + winnerName + "       (╮°-°)╮┳━━┳ (╯°□°)╯ ┻━━┻ \n\n");
        for (int i = 0; i < 50; i++) { System.out.println(repName); }
        System.out.println("\n\n" +
                "    ███████████████████████████████████\n" +
                "    ███████████▀▀▀░░░░░░░▀▀▀███████████\n" +
                "    ████████▀░░░░░░░░░░░░░░░░░▀████████\n" +
                "    ███████│░░░░░░░░░░░░░░░░░░░│███████\n" +
                "    ██████▌│░░░░░░░░░░░░░░░░░░░│▐██████\n" +
                "    ██████░└┐░░░░░░░░░░░░░░░░░┌┘░██████\n" +
                "    ██████░░└┐░░░░░░░░░░░░░░░┌┘░░██████\n" +
                "    ██████░░┌┘▄▄▄▄▄░░░░░▄▄▄▄▄└┐░░██████\n" +
                "    ██████▌░│██████▌░░░▐██████│░▐██████\n" +
                "    ███████░│▐███▀▀░░▄░░▀▀███▌│░███████\n" +
                "    ██████▀─┘░░░░░░░▐█▌░░░░░░░└─▀██████\n" +
                "    ██████▄░░░▄▄▄▓░░▀█▀░░▓▄▄▄░░░▄██████\n" +
                "    ████████▄─┘██▌░░░░░░░▐██└─▄████████\n" +
                "    █████████░░▐█─┬┬┬┬┬┬┬─█▌░░█████████\n" +
                "    ████████▌░░░▀┬┼┼┼┼┼┼┼┬▀░░░▐████████\n" +
                "    █████████▄░░░└┴┴┴┴┴┴┴┘░░░▄█████████\n" +
                "    ███████████▄░░░░░░░░░░░▄███████████\n" +
                "    ██████████████▄▄▄▄▄▄▄██████████████");
    }

    private void multiplayer() {

    }

    private void fillField(Player player) throws UI.input.CommandException {
        if (player.isHuman()) {
            System.out.println("\nWhat a pity! " + player.getName() + ", your field is empty, let's fill it (ﾉ◕ヮ◕)ﾉ*: ･ﾟ✧");
        }
        Field field = player.getField();
        while (!field.isStorageEmpty()) {
            field.setBoat(player.getBoat());
        }
        if (player.isHuman()) {
            System.out.println("Wow, you're The God of War, " + player.getName() + "! (✯◡✯)");
            UI.print.table(player);
        }
    }

    private String mainLoop() throws UI.input.CommandException {
        int move = PC.rand.inRange(0, players.length - 1);
        int len = players.length;
        Player attacking;
        Player defencing;
        while (true) {
            attacking = players[move % len];
            defencing = players[(move + 1) % len];
            int[] action = attacking.getAction(defencing);
            int answer = defencing.attackMe(action);
            attacking.retAnswer(answer);
            if (defencing.isLose()) break;
            if (answer == 0) move++;
        }
        return attacking.getName();
    }
}
