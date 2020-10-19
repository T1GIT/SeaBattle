package seaBattle.modes;

import seaBattle.players.Player;
import seaBattle.players.types.*;

public abstract class GameMode {
    private final byte MAX_PLAYERS = 2;
    protected final Player[] players = new Player[MAX_PLAYERS];

    public GameMode(String userName) {
        this.players[0] = userName.toLowerCase().equals("pc") ? new PC() : new UI(userName);
    }

    public abstract void play() throws UI.input.CommandException ;
    public abstract void fillField(Player player) throws UI.input.CommandException;
    public abstract String mainLoop() throws UI.input.CommandException;
}
