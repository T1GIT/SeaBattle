package seaBattle.players.types;

import seaBattle.elements.*;
import seaBattle.players.Player;

import java.util.InputMismatchException;
import java.util.Scanner;

public class UI extends Player {
    final private String name;
    private boolean autoBoat = false;
    private boolean autoAction = false;

    public UI(String name) {

        super();
        if (name.equals("")) { name = "<Unknown>"; }
        this.name = name;
    }

    @Override
    public String getName() { return this.name; }

    @Override
    public boolean isHuman() { return true; }

    @Override
    public Boat getBoat() throws input.CommandException {
        if (autoBoat) { return PC.rand.boat(this.field); }
        while (true) {
            try {
                print.line();
                print.tableWithStorage(this);
                System.out.println("\n<<< Input coordinates of your new boat, Your Majesty ... ╰(*´︶`*)╯♡    (r - for random boat, random - for autofilling)");
                System.out.print("X₁  Y₁  X₂  Y₂ : ");
                int[] args;
                int x1, y1, x2, y2;
                try { args = input.command(); }
                catch (input.CommandException.RandomBoat e) {  return PC.rand.boat(this.field); }
                catch (input.CommandException.RandomField e) {
                    this.autoBoat = true;
                    return PC.rand.boat(this.field);
                }
                switch (args.length) {
                    case 1:
                        int num = args[0];
                        x1 = num / 1000; y1 = num / 100 % 10;
                        x2 = num / 10 % 10; y2 = num % 10;
                        break;
                    case 2:
                        x1 = x2 = args[0];
                        y1 = y2 = args[1];
                        break;
                    case 4:
                        x1 = args[0]; y1 = args[1];
                        x2 = args[2]; y2 = args[3];
                        break;
                    default:
                        throw new InputMismatchException();
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
        if (autoAction) { return PC.rand.action(enemyField); }
        while (true) {
            try {
                print.line();
                print.tableWithEnemy(this, enemy);
                System.out.println("\n<<< Input coordinates for attack, Your Majesty ... /(≧▽≦)/");
                System.out.print("X  Y : ");
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
                    if (point.isAvailable()) return new int[]{x, y};
                    else System.out.println("Attacking one point second time isn't good idea (๏̯͡๏)");
                }
            } catch (InputMismatchException e) { incorrectInput(); }
        }
    }

    @Override
    public void retAnswer(int code) {  // 0 - passed; 1 - wounded; 2 - killed; 3 - impossible
        switch (code) {
            case 0: { if (!autoAction) System.out.println("This place is empty (ﾉ>_<)ﾉ"); break; }
            case 1: { if (!autoAction)  System.out.println("Wow, it was an accurate shot, Sir  w (ﾟｏﾟ)w"); break; }
            case 2: { if (!autoAction)  System.out.println("Another one kill, congratulations (￣^￣)ゞ"); break; }
        }
    }

    private static void incorrectInput() { System.out.println("<<< Sorry, but your slave cannot understand you  (╥﹏╥)"); }

    public static class print {
        private static final int MARGIN = 10;
        private static final int STORAGE_SHIFT = 6;

        public static void tableWithEnemy(Player me, Player enemy) {
            final Field enemyField = enemy.getField();
            String[][] fields = new String[][]{me.getField().getPrinted(me.getName()), enemyField.getPrinted(enemy.getName(), true)};
            String mar_str = String.valueOf(' ').repeat(MARGIN);
            int[] storage = me.getField().getAlives();
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
            for (String s : field) {
                System.out.println(mar_str + s);
            }
        }

        public static void space() {
            final int SPACE=10;
            System.out.print(String.valueOf("\n").repeat(SPACE));
        }

        public static void line() {
            for (int i = 0; i < 100; i++) {
                System.out.print('⎯');
            }
            System.out.println();
        }
    }

    public static class input {
        public static class CommandException extends Exception {
            public static class Exit extends CommandException {}
            public static class Reset extends CommandException {}
            public static class RandomBoat extends CommandException {}
            public static class RandomField extends CommandException {}
        }

        public static int mode(int maxVal) throws CommandException {
            while (true) {
                try {
                    System.out.print("Mode: ");
                    int res = input.command()[0];
                    if (res > maxVal || res < 0) {
                        incorrectInput();
                    } else {
                        return res;
                    }
                } catch (InputMismatchException e) { incorrectInput(); }
            }
        }

        public static String name() throws CommandException {
            final int MAX_NAME = Field.SIZE + 2;
            while (true) {
                System.out.print("Name: ");
                String res = new Scanner(System.in).nextLine();
                if (res.length() > MAX_NAME) {
                    System.out.println("          Unfortunately, this name is too long (maximum length is " + MAX_NAME + " symbols)");
                    print.line();
                } else {
                    return res;
                }
            }
        }

        public static int[] command() throws InputMismatchException, CommandException {
            String[] cmd = new Scanner(System.in).nextLine().toLowerCase().strip().split("\\s+");
            switch (cmd[0]) {
                case "reset": throw new CommandException.Reset();
                case "exit": throw new CommandException.Exit();
                case "r": throw new CommandException.RandomBoat();
                case "random": throw new CommandException.RandomField();
                default: {
                    int[] res = new int[cmd.length];
                    try { for (int i = 0; i < cmd.length; i++) res[i] = Integer.parseInt(cmd[i]); }
                    catch (NumberFormatException e) { throw new InputMismatchException(); }
                    return res;
                }
            }
        }
    }




}
