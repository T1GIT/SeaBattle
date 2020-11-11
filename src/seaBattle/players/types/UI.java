package seaBattle.players.types;

import seaBattle.elements.Boat;
import seaBattle.elements.Field;
import seaBattle.elements.Point;
import seaBattle.players.Player;
import seaBattle.rooms.types.WebRoom;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class UI
        extends Player
{
    private boolean autoBoat = false;
    private boolean autoAction = false;

    public UI(String name) {
        super(name.equals("") ? "<Unknown>" : name);
    }

    @Override
    public boolean isHuman() { return true; }

    @Override
    public Boat getBoat() throws input.CommandException {
        while (true) {
            try {
                print.line();
                print.field.withStorage(this);
                System.out.println("\n>>> Input coordinates of your new boat, Your Majesty ... ╰(*´︶`*)╯♡    (r - for random boat, random - for auto filling)");
                System.out.print("X₁  Y₁  X₂  Y₂ : ");
                if (autoBoat) {
                    Boat boat = PC.rand.boat(this.field);
                    int [] xDist = boat.getxPos();
                    int [] yDist = boat.getyPos();
                    System.out.println(xDist[0] + " " + yDist[0] + " " + xDist[1] + " " + yDist[1]);
                    return boat;
                }
                int[] args;
                int x1, y1, x2, y2;
                try { args = input.command(); }
                catch (input.CommandException.RandomBoat e) {  return PC.rand.boat(this.field); }
                catch (input.CommandException.RandomField e) {
                    this.autoBoat = true;
                    return PC.rand.boat(this.field);
                }
                switch (args.length) {
                    case 1 -> {
                        int num = args[0];
                        x1 = num / 1000;
                        y1 = num / 100 % 10;
                        x2 = num / 10 % 10;
                        y2 = num % 10;
                    }
                    case 2 -> {
                        x1 = x2 = args[0];
                        y1 = y2 = args[1];
                    }
                    case 4 -> {
                        x1 = args[0];
                        y1 = args[1];
                        x2 = args[2];
                        y2 = args[3];
                    }
                    default -> throw new InputMismatchException();
                }
                if (Field.isOver(x1, y1) || Field.isOver(x2, y2)) {
                    System.out.println("I am sorry, but boat's coordinates isn't inbound (μ_μ)");
                } else {
                    int boatLength = Math.max(x2 - x1, y2 - y1) + 1;
                    if ((x2 != x1 && y2 != y1) || (boatLength > Boat.MAX_BOAT_LENGTH))
                        System.out.println("Sorry, it isn't correct coordinates (╯︵╰,)");
                    else if (!this.field.hasInStorage(boatLength))
                        System.out.println("Oh, you have not more boat of this with length " + boatLength + " (╯︵╰,)");
                    else if (!this.field.placeIsEmpty(x1, y1, x2, y2))
                        System.out.println("This place is already engaged ¯＼_(ツ)_/¯");
                    else return new Boat(x1, y1, x2, y2);
                }
            }
            catch (InputMismatchException | NumberFormatException e) { print.incorrectInput(); }
        }
    }

    @Override
    public int[] getAction(String enemyName, Field enemyField) throws input.CommandException {
        while (true) {
            try {
                print.line();
                print.field.withEnemy(this, enemyName, enemyField);
                System.out.println("\n>>> Input coordinates for attack, Your Majesty ... /(≧▽≦)/");
                System.out.print("X  Y : ");
                if (autoAction) {
                    int[] coor = PC.rand.action(enemyField);
                    System.out.println(coor[0] + " " + coor[1]);
                    return coor;
                }
                int[] args;
                int x, y;
                try { args = input.command(); }
                catch (input.CommandException.RandomBoat e) {  return PC.rand.action(enemyField); }
                catch (input.CommandException.RandomField e) {
                    this.autoAction = true;
                    return PC.rand.action(enemyField);
                }
                if (args.length == 1) { x = args[0] / 10; y = args[0] % 10; }
                else if (args.length == 2) { x = args[0]; y = args[1]; }
                else throw new InputMismatchException();
                if (Field.isOver(x, y)) {
                    System.out.println("Attacking one point second time isn't good idea (๏̯͡๏)");
                } else {
                    Point point = enemyField.getPoint(x, y);
                    if (point.isAttackable()) return new int[]{x, y};
                    else System.out.println("Attacking one point second time isn't good idea (๏̯͡๏)");
                }
            } catch (InputMismatchException e) { print.incorrectInput(); }
        }
    }

    @Override
    public void retAnswer(int code) {  // 0 - passed; 1 - wounded; 2 - killed
        score[code]++;
        System.out.println(switch (code) {
            case 0 -> "This place is empty (ﾉ>_<)ﾉ";
            case 1 -> "Wow, it was an accurate shot, Sir  w (ﾟｏﾟ)w";
            case 2 -> "Another one kill, congratulations (￣^￣)ゞ";
            default -> throw new IllegalStateException("Unexpected answer code: " + code);
        });
    }

    public static class print {
        private static final int MARGIN = 10;
        private static final int STORAGE_SHIFT = 6;

        public static  class field {
            public static void table(Player me) {
                String[] field = me.getField().getPrinted(me.getName());
                String mar_str = String.valueOf(' ').repeat(MARGIN);
                for (String s : field) System.out.println(mar_str + s);
            }

            public static void withEnemy(Player me, String enemyName, Field enemyField) {
                String[][] fields = new String[][]{
                        me.getField().getPrinted(me.getName()),
                        enemyField.getPrinted(enemyName)};
                String mar_str = " ".repeat(MARGIN);
                int[] storage = enemyField.getAlives();
                fields[1][STORAGE_SHIFT] += mar_str + "Alive boats:";
                for (int i = 0; i < storage.length; i++) {
                    fields[1][i + 2 + STORAGE_SHIFT] += mar_str + storage[i] + " x " + String.valueOf(Point.getStateSign(1)).repeat(i + 1);
                }
                for (int i = 0; i < fields[0].length; i++) {
                    System.out.println(mar_str + fields[0][i] + mar_str + fields[1][i]);
                }
            }

            public static void withStorage(Player me) {
                String[] field = me.getField().getPrinted(me.getName());
                int[] storage = me.getField().getStorage();
                String mar_str = String.valueOf(' ').repeat(MARGIN);
                field[STORAGE_SHIFT] += mar_str + "Available boats:";
                for (int i = 0; i < storage.length; i++) {
                    field[i + 2 + STORAGE_SHIFT] += mar_str + storage[i] + " x " + String.valueOf(Point.getStateSign(1)).repeat(i + 1);
                }
                for (String s : field) {
                    System.out.println(mar_str + s);
                }
            }
        }

        public static void space() {
            final int SPACE = 20;
            System.out.print("\n".repeat(SPACE));
        }

        public static void line() {
            final int LEN = 130;
            System.out.println("⎯".repeat(LEN));
        }

        public static void incorrectInput() { System.out.println("<<< Sorry, but your slave cannot understand you  (╥﹏╥)"); }

        public static class rating {
            public static void ladder(Object[][] rating) {
                final int WIDTH = Player.MAX_NAME_LENGTH;
                final int HEIGHT = 2;
                String name; int score, left_margin;
                System.out.println();
                for (int i = 0; i < rating.length; i++) {
                    name = (String) rating[i][0];
                    score = (int) rating[i][1];
                    left_margin = (Player.MAX_NAME_LENGTH - name.length()) / 2;
                    System.out.println(" ".repeat(left_margin) + name);
                    System.out.print(" ".repeat(WIDTH * i) + "-".repeat(WIDTH));
                    System.out.printf("\n" + " ".repeat(WIDTH * (i)) + "%-" + WIDTH + "d" + "|", score);
                    for (int j = 0; j < HEIGHT - 1; j++) System.out.print("\n" + " ".repeat(WIDTH * (i + 1)) + "|");
                }
                System.out.println();
            }

            public static void table(Object[][] rating) { // TODO: remove score. Add kills, wounds, passes
                Object[] row;
                final boolean WITH_HAT = true;
                int margin = (Player.MAX_NAME_LENGTH) / 2;
                System.out.println("\n" + " ".repeat(margin - 2) + "ＳＣＯＲＥ ＢＯＡＲＤ\n");
                if (WITH_HAT) System.out.println("PLACE" + " ".repeat(margin) + "NAME" + " ".repeat(margin) + "KILLS  WOUNDS  PASSES");
                for (int i = 0, place = 1; i < rating.length; i++, place++) {
                    row = rating[i];
                    System.out.printf("%5d  %-" + (Player.MAX_NAME_LENGTH) + "s  %-6s %-7s %s\n",
                            place, row[0], row[1], row[2], row[3]);
                }
                System.out.println();
            }
        }

    }

    public static class input {

        public static class CommandException extends RuntimeException {
            CommandException() {}
            CommandException(String msg) { super(msg); }

            public final static class Exit extends CommandException {}
            public final static class Reset extends CommandException {}
            public final static class RandomBoat extends CommandException {}
            public final static class RandomField extends CommandException {}
            public final static class Chat extends CommandException {public Chat(String msg) {super(msg);}}
        }

        public static int variant(int amountOfVariants, String phrase) throws CommandException {
            while (true) {
                try {
                    System.out.print(phrase + ": ");
                    int res = input.command()[0];
                    if (res >= amountOfVariants || res < 0) System.out.println("          Can't find this answer");
                    else return res;
                } catch (InputMismatchException e) { print.incorrectInput(); }
            }
        }

        public static int amountOfPlayers(int maxPlayers) throws CommandException {
            while (true) {
                try {
                    System.out.print("Amount: ");
                    int res = input.command()[0];
                    if (res > maxPlayers) System.out.println("          Players amount can't be more then " + maxPlayers);
                    else if (res < 2) System.out.println("          At least 2 players should participate");
                    else return res;
                } catch (InputMismatchException e) { print.incorrectInput(); }
            }
        }

        public static String roomName() {
            while (true) {
                System.out.print("Room name: ");
                String res = new Scanner(System.in).nextLine().strip();
                if (res.length() > WebRoom.MAX_NAME_LENGTH) {
                    System.out.println("          Unfortunately, this name is too long (maximum length is " + WebRoom.MAX_NAME_LENGTH + " symbols)");
                } else if (res.length() == 0) {
                    System.out.println("          Name should include at least 1 symbol");
                } else return res;
            }
        }

        public static String playerName() {
            while (true) {
                System.out.print("Name: ");
                String res = new Scanner(System.in).nextLine().strip();
                if (res.length() > Player.MAX_NAME_LENGTH) {
                    System.out.println("          Unfortunately, this name is too long (maximum length is " + Player.MAX_NAME_LENGTH + " symbols)");
                } else if (res.length() == 0) {
                    System.out.println("          Name should include at least 1 symbol");
                } else return res;
            }
        }

        public static byte[] password() {
            String psw;
            while (true) {
                System.out.print("Password: ");
                psw = new Scanner(System.in).nextLine().strip();
                if (psw.length() > WebRoom.security.MAX_PSW_LEN) {
                    System.out.println("          Password can't be longer then " + WebRoom.security.MAX_PSW_LEN);
                } else if (psw.length() < WebRoom.security.MIN_PSW_LEN) {
                    System.out.println("Password can't be shorter then " + WebRoom.security.MIN_PSW_LEN);
                } else break;
            }
            return WebRoom.security.hash(psw);
        }

        public static int[] command() throws CommandException {
            String[] cmd = new Scanner(System.in).nextLine().toLowerCase().strip().split("\\s+");
            switch (cmd[0]) {
                case "reset" -> throw new CommandException.Reset();
                case "exit" -> throw new CommandException.Exit();
                case "r" -> throw new CommandException.RandomBoat();
                case "random" -> throw new CommandException.RandomField();
                case "chat" -> throw new CommandException.Chat(String.join(" ", Arrays.copyOfRange(cmd, 1, cmd.length)));
                default -> {
                    int[] args = new int[cmd.length];
                    try {
                        for (int i = 0; i < cmd.length; i++) args[i] = Integer.parseInt(cmd[i]);
                    } catch (NumberFormatException e) { throw new InputMismatchException(); }
                    return args;
                }
            }
        }
    }




}
