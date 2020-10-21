package seaBattle.modes;

import seaBattle.elements.Field;
import seaBattle.players.Player;
import seaBattle.players.types.PC;
import seaBattle.players.types.UI;
import seaBattle.rooms.types.LocalRoom;

public class Single extends GameMode {
    public Single(String userName) { super(userName); }

    @Override
    public void play() throws UI.input.CommandException  {
        UI.print.line();
        System.out.println("             Welcome to ＳＩＮＧＬＥ　ＭＯＤＥ, Dear " + players[0].getName() + "! ヽ(・∀・)ﾉ");
        UI.print.line();
        for (int i = 1; i < GameMode.getMaxPlayers(); i++) {
            if (room.get(i-1).isHuman()) { UI.print.space(); }
            room.conn = findPlayer(room.get(i-1).getName());
        }
        for (int i = 0; i < GameMode.getMaxPlayers(); i++) {
            if (i > 0 && players[i-1].isHuman()) { UI.print.space(); }
            fillField(players[i]);
        }
        System.out.println("\n\n              \uD835\uDD43\uD835\uDD56\uD835\uDD65❜\uD835\uDD64 \uD835\uDD64\uD835\uDD65\uD835\uDD52\uD835\uDD52\uD835\uDD52\uD835\uDD52\uD835\uDD52\uD835\uDD52\uD835\uDD52\uD835\uDD52\uD835\uDD63\uD835\uDD65!  ►\n\n");
        String winnerName = mainLoop();
        String repName = winnerName.repeat(50) + "\n";
        System.out.println("\n\n" +
                "                ▒█░░▒█ ▀█▀ ▒█▄░▒█ \n" +
                "                ▒█▒█▒█ ▒█░ ▒█▒█▒█ \n" +
                "                ▒█▄▀▄█ ▄█▄ ▒█░░▀█ \n\n" +
                "       (⌐■_■)       " + winnerName + "       (╮°-°)╮┳━━┳ (╯°□°)╯ ┻━━┻ \n\n");
        System.out.println(repName.repeat(50));
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

    @Override
    public Player findPlayer(String lastPlayerName) throws UI.input.CommandException {
        Player player;
        System.out.println("It's time to choose your opponent, " + lastPlayerName + ": Q (｀⌒´Q)\n" +
                "    0 - PC (This machine is stupid, but lucky)\n" +
                "    1 - Player (Like PC, but on the contrary)\n" +
                ">>> Make your choice (◕‿◕)");
        int gameMode = UI.input.mode(1);
        UI.print.line();
        switch (gameMode) {
            case 0:
                System.out.println("Wow, good choice, our Terminator is already ready to win this game (⌐■_■)>");
                player = new PC();
                break;
            case 1:
                System.out.println("Oh, it's incredible, so many people in there (O.O)\n" +
                        ">>> What's your name, Stranger?");
                player = new UI(UI.input.name());
                break;
            default: throw new IllegalStateException("Unexpected value: " + gameMode);
        }
        return player;
    }

    @Override
    public void fillField(Player player) throws UI.input.CommandException  {
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

    @Override
    public String mainLoop() throws UI.input.CommandException {
        int len = GameMode.getMaxPlayers();
        int move = PC.rand.inRange(0, len - 1);
        Player attacking;
        Player defencing;
        while (true) {
            for (int i = 0; i < len - 1; i++) {
                int num = (move + i) % len;
                if (!players[num].isLose()) attacking = players[num];
            }
            for (int i = 1; i < len; i++) {
                int num = (move + i) % len;
                if (!players[num].isLose()) defencing = players[num];
            }
            if (defencing == attacking) break;
            int[] action = attacking.getAction(defencing);
            int answer = defencing.attackMe(action);
            attacking.retAnswer(answer);
            if (answer == 0) move++;
        }
        assert attacking != null;
        return attacking.getName();
    }
}
