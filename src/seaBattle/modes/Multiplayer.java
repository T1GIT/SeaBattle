package seaBattle.modes;

import seaBattle.players.Player;
import seaBattle.players.types.UI;

public class Multiplayer extends GameMode {
    public Multiplayer(String userName) { super(userName); }

    @Override
    public void play() throws UI.input.CommandException {

    }

    @Override
    public void fillField(Player player) throws UI.input.CommandException {

    }

    @Override
    public String mainLoop() throws UI.input.CommandException {
        return null;
    }
}
