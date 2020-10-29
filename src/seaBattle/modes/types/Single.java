package seaBattle.modes.types;

import seaBattle.elements.Field;
import seaBattle.modes.GameMode;
import seaBattle.players.Player;
import seaBattle.players.types.PC;
import seaBattle.players.types.UI;
import seaBattle.rooms.types.LocalRoom;

public class Single implements GameMode {
    private LocalRoom room;

    @Override
    public void play(String userName) throws UI.input.CommandException  {
        UI.print.line();
        System.out.println("             Welcome to ＳＩＮＧＬＥ　ＭＯＤＥ, Dear " + userName + "! ヽ(・∀・)ﾉ");
        UI.print.line();
        System.out.println("How many peoples are here? (⊙▂⊙)");
        int amount = UI.input.amountOfPlayers(LocalRoom.MAX_PLAYERS);
        UI.print.line();
        room = new LocalRoom(amount);
        room.connect(
                userName.toLowerCase().equals("pc")
                        ? new PC()
                        : new UI(userName));
        for (int i = 1; i < room.size(); i++) {
            if (room.getPlayer(i - 1).isHuman()) UI.print.space();
            room.connect(findPlayer());
            UI.print.line();
        }
        for (int i = 0; i < room.size(); i++) {
            if (i > 0 && room.getPlayer(i - 1).isHuman()) { UI.print.space(); }
            fillField(room.getPlayer(i));
        }
        System.out.println("\n\n              \uD835\uDD43\uD835\uDD56\uD835\uDD65❜\uD835\uDD64 \uD835\uDD64\uD835\uDD65\uD835\uDD52\uD835\uDD52\uD835\uDD52\uD835\uDD52\uD835\uDD52\uD835\uDD52\uD835\uDD52\uD835\uDD52\uD835\uDD63\uD835\uDD65!  ►\n\n");
        UI.print.space();
        Object[][] rating = mainLoop();
        System.out.println("Ugh, game is finished. It was hard, but you've done it ╰(▔∀▔)╯ \n" +
                "And you can see the results:");
        System.out.println("\n\n" +
                "                ▒█░░▒█ ▀█▀ ▒█▄░▒█ \n" +
                "                ▒█▒█▒█ ▒█░ ▒█▒█▒█ \n" +
                "                ▒█▄▀▄█ ▄█▄ ▒█░░▀█ \n\n" +
                "       (⌐■_■)       " + rating[0][0] + "       (╮°-°)╮┳━━┳ (╯°□°)╯ ┻━━┻ ");
        UI.print.rating.table(rating);
    }

    @Override
    public Player findPlayer() throws UI.input.CommandException {
        Player player;
        System.out.println("It's time to choose your opponent, : Q (｀⌒´Q)\n" +
                "    0 - PC (This machine is stupid, but lucky)\n" +
                "    1 - Player (Like PC, but on the contrary)\n" +
                ">>> Make your choice (◕‿◕)");
        int gameMode = UI.input.variant(2, "Game mode");
        UI.print.line();
        switch (gameMode) {
            case 0 -> {
                System.out.println("Wow, good choice, our Terminator is already ready to win this game (⌐■_■)>");
                player = new PC();
            }
            case 1 -> {
                System.out.println("Oh, it's incredible, so many people in there (O.O)\n" +
                        ">>> What's your name, Stranger?");
                player = new UI(UI.input.playerName());
            }
            default -> throw new IllegalStateException("Unexpected value: " + gameMode);
        }
        return player;
    }

    @Override
    public void fillField(Player player) throws UI.input.CommandException {
        if (player.isHuman()) {
            System.out.println("\nWhat a pity! " + player.getName() + ", your field is empty, let's fill it (ﾉ◕ヮ◕)ﾉ*: ･ﾟ✧");
        }
        Field field = player.getField();
        while (field.isStorageAvailable()) {
            field.setBoat(player.getBoat());
        }
        if (player.isHuman()) {
            System.out.println("Wow, you're The God of War, " + player.getName() + "! (✯◡✯)");
            UI.print.field.table(player);
        }
    }

    @Override
    public Object[][] mainLoop() throws UI.input.CommandException {
        Player attacking, defencing;
        int[] action;
        int answer;
        room.start();
        do {
            attacking = room.getAttacking();
            defencing = room.getDefencing();
            action = attacking.getAction(
                    defencing.getName(),
                    defencing.getField().getSecured());
            answer = defencing.attackMe(action);
            attacking.retAnswer(answer);
            if (defencing.isLose()) {
                System.out.println(defencing.getName());
                room.addLoser(defencing);
                room.next();
            } else if (answer == 0) {
                room.next();
                if (defencing.isHuman()) UI.print.space();
            }
        } while (room.isRunning());
        room.addLoser(attacking);
        System.out.println(attacking.getName());
        return room.getResult();
    }
}
