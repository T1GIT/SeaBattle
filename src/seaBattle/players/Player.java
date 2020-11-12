package seaBattle.players;
import seaBattle.elements.Boat;
import seaBattle.elements.Field;
import seaBattle.players.types.UI;


/**
 * Abstract class for game's players.
 * Includes all of the methods for interaction between
 * game and player.
 * Holding player's field and name.
 */
public abstract class Player
{
    private final int maxNameLength;
    private final String name;
    protected final Field field;
    /**
     * {@code score} contains player's statistics:
     *      0 - passes
     *      1 - wounds
     *      2 - kills
     */
    protected final byte[] score;

    public int getMaxNameLength() {  return maxNameLength; }
    public Field getField() { return field; }
    public String getName() { return name; }
    public byte[] getScore() { return score; }

    /**
     * Constructs Player.
     * @param name player's name.
     */
    public Player(String name, int size) {
        this.maxNameLength = size * 3 - 8;
        assert (name.length() <= maxNameLength): "Given name longer then available: " + name;
        this.field = new Field(size);
        this.name = name;
        this.score = new byte[]{0, 0, 0};
    }

    /**
     * @param coor {@code [x, y]} coordinates to attack this player
     * @return result of attack:
     *      0 - passed
     *      1 - wounded
     *      2 - killed
     */
    public int attackMe(int[] coor) { return this.field.attack(coor[0], coor[1]); }

    /**
     * @return True if player have no alive boat on its field.
     */
    public boolean isLose() { return field.isDead(); }

    /**
     * @return True if player isn't {@code PC}.
     */
    public abstract boolean isHuman();

    /**
     * @return answer for boat's coordinates.
     * @throws UI.input.CommandException if user typed command.
     */
    public abstract Boat getBoat() throws UI.input.CommandException;

    /**
     * @param enemyName defencing player's name.
     * @param enemyField defencing player's field.
     * @return answer for attack's coordinates.
     * @throws UI.input.CommandException if user typed command.
     */
    public abstract int[] getAction(String enemyName, Field enemyField) throws UI.input.CommandException;

    /**
     * Returns answer to player.
     * Writes into {@code score} result.
     * MUST TO BE OVERRIDING
     * @param code of answer from {@code field.attack()}
     *      0 - passed
     *      1 - wounded
     *      2 - killed
     */
    public abstract void retAnswer(int code);

}
