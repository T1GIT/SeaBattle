package seaBattle.modes.types;

import seaBattle.field.Field;
import seaBattle.modes.GameMode;
import seaBattle.players.Player;
import seaBattle.players.types.PC;
import seaBattle.players.types.UI;
import seaBattle.rooms.Room;

public class Single extends GameMode {
    private final Room room = new Room();

    public Single(String userName) {
        room.connect(
                userName.toLowerCase().equals("pc")
                        ? new PC()
                        : new UI(userName));
    }

    @Override
    public void play() throws UI.input.CommandException  {
        UI.print.line();
        System.out.println("             Welcome to ＳＩＮＧＬＥ　ＭＯＤＥ, Dear " + room.getPlayer(0).getName() + "! ヽ(・∀・)ﾉ");
        UI.print.line();
        for (int i = 1; i < room.getSize(); i++) {
            Player lastPlayer = room.getPlayer(i-1);
            room.connect(findPlayer(lastPlayer.getName()));
        }
        for (int i = 0; i < GameMode.MAX_PLAYERS; i++) {
            if (i > 0 && room.getPlayer(i-1).isHuman()) { UI.print.space(); }
            fillField(room.getPlayer(i));
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
                player = new UI(UI.input.playerName());
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
        Player attacking;
        Player defencing;
        room.start();
        do {
            attacking = room.getAttacking();
            defencing = room.getDefencing();
            int[] action = attacking.getAction(defencing);
            int answer = defencing.attackMe(action);
            attacking.retAnswer(answer);
            room.next();
        } while (room.hasNext());
        return attacking.getName();
    }
}
