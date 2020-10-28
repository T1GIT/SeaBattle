package seaBattle.players.types;

import seaBattle.elements.*;
import seaBattle.players.Player;

import java.util.Random;


public class PC extends Player {
    private boolean lastWounded = false;
    private int[] lastWoundCoor;
    private int[] lastHitCoor;

    public PC() { super("PC"); }

    @Override
    public boolean isHuman() { return false; }

    @Override
    public Boat getBoat() { return rand.boat(this.field); }

    @Override
    public  int[] getAction(Player enemy) {
        int[] coor;
        Field enemyField = enemy.getField();
        if (lastWounded) {
            Boat woundedBoat = enemyField.getPoint(this.lastWoundCoor).getBoat();
            int x = this.lastWoundCoor[0]; int y = this.lastWoundCoor[1];
            if (woundedBoat.getWounds() > 1) {  // Have >= 2 wounded points of boat
                Point point;
                int direction;
                try {
                    direction = findWoundDirection(enemyField);
                } catch (AssertionError assertionError) {
                    this.lastWounded = false;
                    return getAction(enemy);
                }
                boolean hor_axis = direction % 2 == 0;
                int step = - direction + (hor_axis ? 1 : 2);
                while (true){
                    x = hor_axis ? x : x + step;
                    y = hor_axis ? y + step : y;
                    if (Field.isOver(x, y)) {
                        step = -step;
                        x = this.lastWoundCoor[0]; y = this.lastWoundCoor[1];
                    }
                    point = enemyField.getPoint(x, y);
                    if (point.isPassed()) {
                        step = -step;
                        x = this.lastWoundCoor[0]; y = this.lastWoundCoor[1];
                    } else if (!point.isWounded()) {
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
        switch (code) {
            case 1 -> {
                lastWounded = true;
                lastWoundCoor = lastHitCoor;
            }
            case 0, 2 -> lastWounded = false;
            default -> throw new IllegalStateException("Unexpected answer code: " + code);
        }
        score += POINTS_FOR_STATE[code];
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

        public static int choice(int one, int two) {
            return bool() ? one : two;
        }

        public static Boat boat(Field field) {
            int len = 0;
            for (int i = Field.MAX_BOAT_LENGTH; i > 0; i--)
                if (field.hasInStorage(i)) {
                    len = i;
                    break;
                }
            assert (len != 0) : "Storage is empty";
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
