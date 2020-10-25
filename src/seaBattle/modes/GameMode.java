package seaBattle.modes;

import seaBattle.players.Player;
import seaBattle.players.types.*;


public interface GameMode {

    /**
     * The main body of the game.
     * Starts the game and uses other functions
     * @throws UI.input.CommandException if user typed command
     */
    void play(String userName) throws UI.input.CommandException ;

    /**
     * Is requesting another players for the game until all places will be engaged
     * @return new {@code Player} that made by user
     * @throws UI.input.CommandException if user typed command
     */
    Player findPlayer() throws UI.input.CommandException;

    /**
     * Is requesting boats and setting it while field isn't filled
     * @param player having {@code Field} for filling
     * @throws UI.input.CommandException if user typed command
     */
    void fillField(Player player) throws UI.input.CommandException;

    /**
     * Is requesting attack and doing it while one player remains
     * @return array of two {@code Object}s arrays, with context:
     *      {@code [[name, score], ...] }
     * @throws UI.input.CommandException if user typed command
     */
    Object[][] mainLoop() throws UI.input.CommandException;
}
