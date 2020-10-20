package seaBattle.players;
import seaBattle.elements.Boat;
import seaBattle.elements.Field;
import seaBattle.players.types.UI;


public abstract class Player {
    final protected Field field;
    final private String name;

    public Field getField() { return this.field; }
    public String getName() { return this.name; }

    public Player(String name) {
        this.field = new Field();
        this.name = name;
    }

    public int attackMe(int[] coor) { return this.field.attack(coor[0], coor[1]); }

    public boolean isLose() { return this.field.isLose(); }

    public abstract boolean isHuman();

    public abstract Boat getBoat() throws UI.input.CommandException;

    public abstract int[] getAction(Player enemy) throws UI.input.CommandException;

    public abstract void retAnswer(int code);

}
