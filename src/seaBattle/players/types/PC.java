package seaBattle.players.types;

import seaBattle.elements.Boat;
import seaBattle.elements.Field;
import seaBattle.elements.Point;
import seaBattle.players.Player;

import java.util.Random;


public class PC
        extends Player
{
    private int woundedBefore = 0;
    private int[] lastWoundCoor;
    private int[] lastHitCoor;

    public PC() { super("PC"); }

    @Override
    public boolean isHuman() { return false; }

    @Override
    public Boat getBoat() { return rand.boat(this.field); }

    @Override
    public  int[] getAction(String enemyName, Field enemyField) {
        int[] coor;
        if (woundedBefore > 0) {
            int x = this.lastWoundCoor[0]; int y = this.lastWoundCoor[1];
            if (woundedBefore > 1) {  // Have >= 2 wounded points of boat
                Point point;
                int direction;
                try {
                    direction = findWoundDirection(enemyField);
                } catch (AssertionError assertionError) {
                    woundedBefore = 0;
                    return getAction(enemyName, enemyField);
                }
                boolean hor_axis = direction % 2 == 0;
                int step = - direction + (hor_axis ? 1 : 2);
                while (true){
                    x = hor_axis ? x : x + step;
                    y = hor_axis ? y + step : y;
                    if (Field.isOver(x, y)) {
                        step = -step;
                        x = lastWoundCoor[0]; y = lastWoundCoor[1];
                    }
                    point = enemyField.getPoint(x, y);
                    if (point.isPassed()) {
                        step = -step;
                        x = lastWoundCoor[0]; y = lastWoundCoor[1];
                    } else if (point.isAttackable()) {
                        coor = new int[]{x, y};
                        break;
                    }
                }
            } else {  // Have only 1 wounded point
                while (true) {
                    coor = switch (rand.inRange(0, 3)) {
                        case 0 -> new int[]{x, y + 1};
                        case 1 -> new int[]{x + 1, y};
                        case 2 -> new int[]{x, y - 1};
                        case 3 -> new int[]{x - 1, y};
                        default -> new int[]{x, y};
                    };
                    if (Field.isOver(coor[0], coor[1])) continue;
                    if (enemyField.getPoint(coor).isAttackable()) {break;}
                }
            }
        } else {  // No one wounded point
            coor = rand.action(enemyField);
        }
        this.lastHitCoor = coor;
        return coor;
    }

    @Override
    public void retAnswer(int code) {  // 0 - passed; 1 - wounded; 2 - killed
        score[code]++;
        switch (code) {
            case 0 -> {}
            case 1 -> {
                woundedBefore++;
                lastWoundCoor = lastHitCoor;
            }
            case 2 -> {
                woundedBefore = 0;
                lastWoundCoor = null;
            }
            default -> throw new IllegalStateException("Unexpected answer code: " + code);
        }
    }

    private int findWoundDirection(Field field) throws AssertionError {
        int x = this.lastWoundCoor[0]; int y = this.lastWoundCoor[1];
        if (Field.inBounds(y+1) && field.getPoint(x, y+1).isWounded()) { return 0; }  // Up
        else if (Field.inBounds(x+1) && field.getPoint(x+1, y).isWounded()) { return 1; }  // Right
        else if (Field.inBounds(y-1) && field.getPoint(x, y-1).isWounded()) { return 2; }  // Down
        else if (Field.inBounds(x-1) && field.getPoint(x-1, y).isWounded()) { return 3; }  // Left
        else {throw new AssertionError("It isn't wounded points around"); }
    }

    public static class rand {
        public static int inRange(int minVal, int maxVal) {
            return minVal + new Random().nextInt(maxVal - minVal + 1);
        }

        public static boolean bool() {
            return Math.random() > 0.5;
        }

        public static int[] coor() {
            int x = inRange(0, Field.SIZE - 1);
            int y = inRange(0, Field.SIZE - 1);
            return new int[]{x, y};
        }

        public static int[] coor(int length) {
            int x = inRange(0, Field.SIZE - length);
            int y = inRange(0, Field.SIZE - length);
            return new int[]{x, y};
        }

        public static Boat boat(Field field) {
            int len = Field.MAX_BOAT_LENGTH;
            for (; len > 0; len--) if (field.hasInStorage(len)) break;
            while (true) {
                boolean rotation = rand.bool();
                int[] point1 = rand.coor(len);
                int[] point2 = new int[]{rotation ? point1[0] + len - 1 : point1[0], !rotation ? point1[1] + len - 1 : point1[1]};
                if  (field.placeIsEmpty(point1, point2)) {
                    return new Boat(point1, point2);
                }
            }
        }

        public static int[] action(Field field) { // Without logic!
            while (true) {
                int[] coor = coor();
                if (field.getPoint(coor).isAttackable()) return coor;
            }
        }
    }

}
