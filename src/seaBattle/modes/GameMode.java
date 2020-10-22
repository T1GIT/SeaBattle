package seaBattle.modes;

import seaBattle.players.Player;
import seaBattle.players.types.*;

public abstract class GameMode {
    public static final byte MAX_PLAYERS = 2;

    /**
     * The main body of the game.
     * Starts the game and uses other functions
     * @throws UI.input.CommandException if user typed command
     */
    public abstract void play() throws UI.input.CommandException ;

    /**
     * Is requesting another players for the game until all places will be engaged
     * @param lastPlayerName the name of user, choosing opponent
     * @return new {@code Player} that made by user
     * @throws UI.input.CommandException if user typed command
     */
    public abstract Player findPlayer(String lastPlayerName) throws UI.input.CommandException;

    /**
     * Is requesting boats and setting it while field isn't filled
     * @param player having {@code Field} for filling
     * @throws UI.input.CommandException if user typed command
     */
    public abstract void fillField(Player player) throws UI.input.CommandException;

    /**
     * Is requesting attack and doing it while one player remains
     * @return name of winner
     * @throws UI.input.CommandException if user typed command
     */
    public abstract String mainLoop() throws UI.input.CommandException;
}
