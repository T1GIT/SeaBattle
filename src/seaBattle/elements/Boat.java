package seaBattle.elements;

public class Boat {
    final private byte[] xPos;
    final private byte[] yPos;
    private byte wounds = 0;

    public Boat(int[] point1, int[] point2) {
        this(point1[0], point1[1], point2[0], point2[1]);
    }

    public Boat(int x1, int y1, int x2, int y2) {
        if (x1 > x2) { int temp = x2; x2 = x1; x1 = temp; }
        if (y1 > y2) { int temp = y2; y2 = y1; y1 = temp; }
        this.xPos = new byte[]{(byte) x1, (byte) x2};
        this.yPos = new byte[]{(byte) y1, (byte) y2};
    }

    public byte[] getxPos() {return this.xPos;}

    public byte[] getyPos() {return this.yPos;}

    public byte getWounds() {return this.wounds;}

    public byte length() {return (byte) (Math.max(xPos[1] - xPos[0], yPos[1] - yPos[0]) + 1);}

    public boolean isDead() {return this.wounds == length();}

    public void wound() { this.wounds++; }
}
