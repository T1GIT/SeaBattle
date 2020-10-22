package seaBattle.field;

import java.util.ArrayList;


public class Field {
    final public static byte SIZE = 10;
    final public static byte MAX_BOAT_LENGTH = 4;
    final private Point[][] table;
    final private int[] unusedBoats;
    final private int[] aliveBoats;

    public Point getPoint(int[] coor) {
        return table[coor[1]][coor[0]];
    }

    public Point getPoint(int x, int y) {
        return table[y][x];
    }

    public int[] getStorage() {
        return unusedBoats;
    }

    public int[] getAlives() {
        return aliveBoats;
    }

    public static boolean inBounds(int coor) {
        return coor >= 0 && coor < SIZE;
    }

    public static boolean isOver(int x, int y) {
        return !inBounds(x) || !inBounds(y);
    }

    public Field() {
        this.table = new Point[SIZE][SIZE];
        this.unusedBoats = new int[MAX_BOAT_LENGTH];
        this.aliveBoats = new int[MAX_BOAT_LENGTH];
        for (int i = 0; i < MAX_BOAT_LENGTH; i++) {
            this.unusedBoats[i] = MAX_BOAT_LENGTH - i;
        }
        for (int i = 0; i < MAX_BOAT_LENGTH; i++) {
            this.aliveBoats[i] = 0;
        }
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                this.table[y][x] = new Point();
            }
        }
    }

    public boolean placeIsEmpty(int x1, int y1, int x2, int y2) {
        x1 = Math.max(0, x1 - 1);
        x2 = Math.min(x2 + 1, SIZE - 1);
        y1 = Math.max(0, y1 - 1);
        y2 = Math.min(y2 + 1, SIZE - 1);
        for (int _x = x1; _x <= x2; _x++) {
            for (int _y = y1; _y <= y2; _y++) {
                if (!this.table[_y][_x].isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean placeIsEmpty(int[] p1, int[] p2) {
        return placeIsEmpty(p1[0], p1[1], p2[0], p2[1]);
    }

    public boolean hasInStorage(int length) {
        return unusedBoats[length - 1] > 0;
    }

    public boolean isStorageEmpty() {
        for (int am : unusedBoats) {
            if (am != 0) {
                return false;
            }
        }
        return true;
    }

    public boolean isAlive() {
        for (int am : aliveBoats) {
            if (am != 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param x coordinate for attack this {@code field}
     * @param y coordinate for attack this {@code field}
     * @return result of attack {@code state} :
     * 0 - passed
     * 1 - wounded
     * 2 - killed
     */
    public int attack(int x, int y) {
        Point p = table[y][x];
        int state;
        if (p.isEmpty()) {
            p.pass();
            state = 0;
        } else {
            p.wound();
            Boat boat = p.getBoat();
            if (boat.isDead()) {
                for (Point point : this.getBoatPoints(boat)) {
                    point.kill();
                }
                for (Point point : this.getEnv(boat)) {
                    point.pass();
                }
                --aliveBoats[boat.length() - 1];
                state = 2;
            } else {
                state = 1;
            }
        }
        return state;
    }

    /**
     * Places boat in the field without any checks
     *
     * @param boat for placing
     */
    public void setBoat(Boat boat) {
        byte[] xDist = boat.getxPos();
        byte[] yDist = boat.getyPos();
        byte length = boat.length();
        for (byte _x = xDist[0]; _x <= xDist[1]; _x++) {
            for (byte _y = yDist[0]; _y <= yDist[1]; _y++) {
                this.table[_y][_x].setBoat(boat);
            }
        }
        unusedBoats[length - 1]--;
        aliveBoats[length - 1]++;
    }

    /**
     * @param boat for checking
     * @return {@code Point[]}, including all {@code Point}s around
     * the {@code boat}
     */
    private Point[] getEnv(Boat boat) {
        byte[] xPos = boat.getxPos();
        byte[] yPos = boat.getyPos();
        ArrayList<Point> env = new ArrayList<>();
        int x1 = Math.max(0, xPos[0] - 1);
        int x2 = Math.min(xPos[1] + 1, SIZE - 1);
        int y1 = Math.max(0, yPos[0] - 1);
        int y2 = Math.min(yPos[1] + 1, SIZE - 1);
        for (int _x = x1; _x <= x2; _x++) {
            for (int _y = y1; _y <= y2; _y++) {
                if (_x < xPos[0] || _x > xPos[1] || _y < yPos[0] || _y > yPos[1]) {
                    env.add(this.table[_y][_x]);
                }
            }
        }
        return env.toArray(Point[]::new);
    }

    /**
     * @param boat for checking
     * @return {@code Point[]}, including all {@code Point}s
     * that {@code boat} engages
     */
    private Point[] getBoatPoints(Boat boat) {
        byte[] xPos = boat.getxPos();
        byte[] yPos = boat.getyPos();
        ArrayList<Point> boatPoints = new ArrayList<>(boat.length());
        for (byte _x = xPos[0]; _x <= xPos[1]; _x++) {
            for (byte _y = yPos[0]; _y <= yPos[1]; _y++) {
                boatPoints.add(this.table[_y][_x]);
            }
        }
        return boatPoints.toArray(Point[]::new);
    }

    public String[] getPrinted(String name, Boolean hidden) {
        final int LINE_LEN = SIZE * 3;
        int leftAlign = (LINE_LEN - name.length()) / 2;
        int rightAlign = LINE_LEN - leftAlign - name.length();
        final String[] res = new String[SIZE + 6];
        res[0] = "┏ " + "━━ ".repeat(LINE_LEN / 3 + 2) + "━┓";
        res[1] = "┃" + String.valueOf(' ').repeat(leftAlign + 4) + name + String.valueOf(' ').repeat(rightAlign + 4) + "┃";
        res[2] = "┣ ━━━ ┳ " + "━━ ".repeat(LINE_LEN / 3) + "━┫";
        StringBuilder numbers = new StringBuilder();
        numbers.append("┃     ┃ ");
        for (int i = 0; i < SIZE; i++) {
            numbers.append(" ").append(i).append(" ");
        }
        numbers.append(" ┃");
        res[3] = numbers.toString();
        res[4] = "┣ ━━━ ╋ " + "━━ ".repeat(LINE_LEN / 3) + "━┫";
        for (int row = 0; row < SIZE; row++) {
            StringBuilder str = new StringBuilder();
            str.append("┃  ").append(row).append("  ┃ ");
            for (int col = 0; col < SIZE; col++) {
                char symbol = table[row][col].draw();
                if (table[row][col].isAlive() && hidden) {
                    symbol = Point.getStateSign(0);
                }
                str.append(" ").append(symbol).append(" ");
            }
            str.append(" ┃");
            res[5 + row] = str.toString();
        }
        res[SIZE + 5] = "┗ ━━━ ┻ " + "━━ ".repeat(LINE_LEN / 3) + "━┛";
        return res;
    }

    public String[] getPrinted(String name) {
        return getPrinted(name, false);
    }
}
