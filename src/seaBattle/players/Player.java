package seaBattle.players;
import seaBattle.field.Boat;
import seaBattle.field.Field;
import seaBattle.players.types.UI;


public abstract class Player {
    public final static int MAX_NAME_LENGTH = Field.SIZE + 2;
    protected final Field field;
    private final String name;

    public Field getField() { return this.field; }
    public String getName() { return this.name; }

    /**
     * Constructs Player
     * @param name player's name
     */
    public Player(String name) {
        assert (name.length() <= MAX_NAME_LENGTH): "Given name longer then available: " + name;
        this.field = new Field();
        this.name = name;
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
     * @return True if player have no alive boat on its field
     */
    public boolean isAlive() { return this.field.isAlive(); }

    /**
     * @return True if player isn't {@code PC}
     */
    public abstract boolean isHuman();

    /**
     * @return answer for boat's coordinates request from console
     * @throws UI.input.CommandException if user typed command
     */
    public abstract Boat getBoat() throws UI.input.CommandException;

    /**
     * @param enemy another player
     * @return answer for attack's coordinates request from console
     * @throws UI.input.CommandException if user typed command
     */
    public abstract int[] getAction(Player enemy) throws UI.input.CommandException;

    /**
     * Returns answer to player
     * @param code of answer from {@code field.attack()}
     */
    public abstract void retAnswer(int code);

}
