package seaBattle.elements;

import java.io.Serializable;

public class Point
        implements Serializable
{
    private Boat boat;
    private byte state;
    final static private char[] drawDict = {
            ' ', // empty       0
            '□', // alive       1
            '☒', // wounded     2
            '■', // killed      3
            '∙', // passed      4
    };

    public Point() { this(0); }
    public Point(int state) { this.state = (byte) state; }

    public byte getState() { return state; }
    public Boat getBoat() {return this.boat;}
    public void setBoat(Boat boat) {this.boat = boat; this.state = 1;}
    public static char getStateSign(int state) {return drawDict[state];}

    public boolean isEmpty() {return this.state == 0;}
    public boolean isWounded() {return this.state == 2;}
    public boolean isPassed() {return this.state == 4;}
    public boolean isAttackable() {return this.state == 0 || this.state == 1;}

    public void wound() {this.state = 2; this.boat.wound();}
    public void kill() {this.state = 3;}
    public void pass() {this.state = 4;}

    /**
     * @return {@code char} matching {@code Point}'s state
     * from {@code drawDict}
     */
    public char draw() {return drawDict[this.state];}
}
