package seaBattle.modes;

import seaBattle.players.Player;
import seaBattle.players.types.UI;


/**
 * Class, implementing {@code GameMode}
 * include all the steps of the game, with their
 * own realisations.
 *  Order:
 * 1) play - for running game
 * 2) findPlayer - for filling room, finding all the opponents
 * 3) fillField - filling players' tables
 * 4) mainloop - running process of game
 */
public interface GameMode {

    /**
     * The main body of the game.
     * Starts the game and uses other functions
     * @throws UI.input.CommandException if user typed command
     */
    void play(String userName) throws UI.input.CommandException;

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
     * @return array of 4 {@code Object}s arrays, with context:
     *      {@code [[name, kills, wounds, passes], ...] }
     * @throws UI.input.CommandException if user typed command
     */
    Object[][] mainLoop() throws UI.input.CommandException;
}
