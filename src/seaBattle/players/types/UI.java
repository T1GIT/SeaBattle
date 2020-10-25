package seaBattle.players.types;

import seaBattle.field.Boat;
import seaBattle.field.Field;
import seaBattle.field.Point;
import seaBattle.players.Player;
import seaBattle.rooms.Room;
import seaBattle.rooms.WebRoom;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class UI extends Player {
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
                print.tableWithStorage(this);
                System.out.println("\n>>> Input coordinates of your new boat, Your Majesty ... ╰(*´︶`*)╯♡    (r - for random boat, random - for autofilling)");
                System.out.print("X₁  Y₁  X₂  Y₂ : ");
                if (autoBoat) {
                    Boat boat = PC.rand.boat(this.field);
                    byte [] xDist = boat.getxPos();
                    byte [] yDist = boat.getyPos();
                    System.out.println(xDist[0] + " " + yDist[0] + " " + xDist[1] + " " + yDist[1]);
                    return boat;
                }
                int[] args;
                int x1, y1, x2, y2;
                try { args = input.command(4); }
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
                    if ((x2 != x1 && y2 != y1) || (boatLength > Field.MAX_BOAT_LENGTH))
                        System.out.println("Sorry, it isn't correct coordinates (╯︵╰,)");
                    else if (!this.field.hasInStorage(boatLength))
                        System.out.println("Oh, you have not more boat of this with length " + boatLength + " (╯︵╰,)");
                    else if (!this.field.placeIsEmpty(x1, y1, x2, y2))
                        System.out.println("This place is already engaged ¯＼_(ツ)_/¯");
                    else return new Boat(x1, y1, x2, y2);
                }
            }
            catch (InputMismatchException | NumberFormatException e) { incorrectInput(); }
        }
    }

    @Override
    public int[] getAction(Player enemy) throws input.CommandException {
        final Field enemyField = enemy.getField();
        while (true) {
            try {
                print.line();
                print.tableWithEnemy(this, enemy);
                System.out.println("\n>>> Input coordinates for attack, Your Majesty ... /(≧▽≦)/");
                System.out.print("X  Y : ");
                if (autoAction) {
                    int[] coor = PC.rand.action(enemyField);
                    System.out.println(coor[0] + " " + coor[1]);
                    return coor;
                }
                int[] args;
                int x, y;
                try { args = input.command(2); }
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
            } catch (InputMismatchException e) { incorrectInput(); }
        }
    }

    @Override
    public void retAnswer(int code) {  // 0 - passed; 1 - wounded; 2 - killed
        switch (code) {
            case 0 -> System.out.println("This place is empty (ﾉ>_<)ﾉ");
            case 1 -> System.out.println("Wow, it was an accurate shot, Sir  w (ﾟｏﾟ)w");
            case 2 -> System.out.println("Another one kill, congratulations (￣^￣)ゞ");
            default -> throw new IllegalStateException("Unexpected answer code: " + code);
        }
        score += POINTS_FOR_STATE[code];
    }

    private static void incorrectInput() { System.out.println("<<< Sorry, but your slave cannot understand you  (╥﹏╥)"); }

    public static class print {
        private static final int MARGIN = 10;
        private static final int STORAGE_SHIFT = 6;

        public static void tableWithEnemy(Player me, Player enemy) {
            final Field enemyField = enemy.getField();
            String[][] fields = new String[][]{me.getField().getPrinted(me.getName()), enemyField.getPrinted(enemy.getName(), true)};
            String mar_str = " ".repeat(MARGIN);
            int[] storage = enemy.getField().getAlives();
            fields[1][STORAGE_SHIFT] += mar_str + "Alive boats:";
            for (int i = 0; i < storage.length; i++) {
                fields[1][i + 2 + STORAGE_SHIFT] += mar_str + storage[i] + " x " + String.valueOf(Point.getStateSign(1)).repeat(i + 1);
            }
            for (int i = 0; i < fields[0].length; i++) {
                System.out.println(mar_str + fields[0][i] + mar_str + fields[1][i]);
            }
        }

        public static void tableWithStorage(Player me) {
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

        public static void table(Player me) {
            String[] field = me.getField().getPrinted(me.getName());
            String mar_str = String.valueOf(' ').repeat(MARGIN);
            for (String s : field) System.out.println(mar_str + s);
        }

        public static void space() {
            final int SPACE = 20;
            System.out.print("\n".repeat(SPACE));
        }

        public static void line() {
            final int LEN = 130;
            System.out.println("⎯".repeat(LEN));
        }

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

            public static void table(Object[][] rating) {
                int margin = (Player.MAX_NAME_LENGTH) / 2;
                System.out.println("\n" + " ".repeat(margin - 2) + "ＳＣＯＲＥ ＢＯＡＲＤ\n");
                System.out.println("PLACE" + " ".repeat(margin) + "NAME" + " ".repeat(margin) + "POINTS");
                int points; String name;
                for (int i = 0, place = 1; i < rating.length; i++, place++) {
                    name = (String) rating[i][0];
                    points = (int) rating[i][1];
                    System.out.printf("%5d  %-" + (Player.MAX_NAME_LENGTH) + "s  %-6d\n",
                            place, name, points);
                }
                System.out.println();
            }
        }
    }

    public static class input {

        public static class CommandException extends Exception {
            CommandException() {}
            CommandException(String msg) { super(msg); }

            public static class Exit extends CommandException {}
            public static class Reset extends CommandException {}
            public static class RandomBoat extends CommandException {}
            public static class RandomField extends CommandException {}
            public static class AddRoom extends CommandException {}
            public static class Chat extends CommandException {public Chat(String msg) {super(msg);}}
        }

        public static int mode(int amountOfVariants, String word) throws CommandException {
            while (true) {
                try {
                    System.out.print(word + ": ");
                    int res = input.command(1)[0];
                    if (res >= amountOfVariants || res < 0) incorrectInput();
                    else return res;
                } catch (InputMismatchException e) { incorrectInput(); }
            }
        }

        public static int mode(int amountOfVariants) throws CommandException { return mode(amountOfVariants, "Mode"); }

        public static int amountOfPlayers(int maxPlayers) throws CommandException {
            while (true) {
                try {
                    System.out.print("Amount: ");
                    int res = input.command(1)[0];
                    if (res > maxPlayers || res < 2) incorrectInput();
                    else return res;
                } catch (InputMismatchException e) { incorrectInput(); }
            }
        }

        public static int amountOfPlayers() throws CommandException {
            return amountOfPlayers(Room.MAX_PLAYERS);
        }

        public static String roomName() {
            while (true) {
                System.out.print("Room name: ");
                String res = new Scanner(System.in).nextLine();
                if (res.length() > WebRoom.MAX_NAME_LENGTH) {
                    System.out.println("          Unfortunately, this name is too long (maximum length is " + WebRoom.MAX_NAME_LENGTH + " symbols)");
                    print.line();
                } else return res;
            }
        }

        public static String playerName() {
            while (true) {
                System.out.print("Name: ");
                String res = new Scanner(System.in).nextLine();
                if (res.length() > Player.MAX_NAME_LENGTH) {
                    System.out.println("          Unfortunately, this name is too long (maximum length is " + Player.MAX_NAME_LENGTH + " symbols)");
                    print.line();
                } else return res;
            }
        }

        public static byte[] password() {
            System.out.print("Password: ");
            return WebRoom.security.hash(new Scanner(System.in).nextLine());
        }

        public static void command() throws CommandException { command(0); }

        public static int[] command(int argsAmount) throws CommandException, InputMismatchException {
            while (true) {
                String[] cmd = new Scanner(System.in).nextLine().toLowerCase().strip().split("\\s+");
                switch (cmd[0]) {
                    case "reset" -> throw new CommandException.Reset();
                    case "exit" -> throw new CommandException.Exit();
                    case "r" -> throw new CommandException.RandomBoat();
                    case "random" -> throw new CommandException.RandomField();
                    case "chat" -> throw new CommandException.Chat(String.join(" ", Arrays.copyOfRange(cmd, 1, cmd.length)));
                    default -> {
                        if (cmd.length != argsAmount) throw new InputMismatchException();
                        int[] args = new int[argsAmount];
                        try {
                            for (int i = 0; i < argsAmount; i++) args[i] = Integer.parseInt(cmd[i]);
                        } catch (NumberFormatException e) { throw new InputMismatchException(); }
                        return args;
                    }
                }
            }
        }
    }




}
