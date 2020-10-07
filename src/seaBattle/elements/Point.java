package seaBattle.elements;

public class Point {
    private byte state;
    private Boat boat;
    final static private char[] drawDict = {' ', '□', '☒','■' ,'∙'};

    public Point() {
        this.state = 0;  // 0 - empty; 1 - alive; 2 - wounded; 3 - killed; 4 - passed
    }

    public Boat getBoat() {return this.boat;}
    public void setBoat(Boat boat) {this.boat = boat; this.state = 1;}
    public static char getStateSign(int state) {return drawDict[state];}

    public boolean isEmpty() {return this.state == 0;}
    public boolean isAlive() {return this.state == 1;}
    public boolean isWounded() {return this.state == 2;}
    public boolean isKilled() {return this.state == 3;}
    public boolean isPassed() {return this.state == 4;}
    public boolean isAvailable() {return this.state == 0 || this.state == 1;}

    public void wound() {this.state = 2; this.boat.wound();}
    public void kill() {this.state = 3;}
    public void pass() {this.state = 4;}

    public char draw() {return drawDict[this.state];}
}
