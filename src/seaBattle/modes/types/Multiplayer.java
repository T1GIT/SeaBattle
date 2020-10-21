package seaBattle.modes.types;

import seaBattle.modes.GameMode;
import seaBattle.players.Player;
import seaBattle.players.types.UI;

public class Multiplayer extends GameMode {
    public Multiplayer(String userName) { super(userName); }

    @Override
    public void play() throws UI.input.CommandException {
        UI.print.line();
        System.out.println("             Welcome to ＭＵＬＴＩＰＬＡＹＥＲ　ＭＯＤＥ, Dear " + players[0].getName() + "! ヽ(・∀・)ﾉ");
        UI.print.line();
    }

    @Override
    public Player findPlayer(String lastPlayerName) throws UI.input.CommandException {
        return null;
    }


    @Override
    public void fillField(Player player) throws UI.input.CommandException {

    }

    @Override
    public String mainLoop() throws UI.input.CommandException {
        return null;
    }
}
