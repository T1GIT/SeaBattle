package seaBattle.rooms;

import seaBattle.players.Player;
import seaBattle.players.types.PC;

import java.util.ArrayList;


public abstract class Room<T extends Player> {
    public final static int MAX_PLAYERS = 10000;
    protected final ArrayList<T> players;
    private final ArrayList<T> result;
    private final int size;
    private int move;
    private T attacking;
    private T defencing;

    public T getPlayer(int index) { return players.get(index % size); }
    public T getAttacking() { return attacking; }
    public T getDefencing() { return defencing; }
    public int size() { return size; }

    public Room(int size) {
        if (size > MAX_PLAYERS) throw new AssertionError("Max amount of players is " + MAX_PLAYERS + ", but " + size + " given");
        players = new ArrayList<>(size);
        result = new ArrayList<>(size);
        this.size = size;
    }

    public int getPlayersIn() { return players.size(); }

    public void connect(T player) { players.add(player); }

    public void start() {
        this.move = PC.rand.inRange(0, size - 1);
        next();
    }

    public boolean isFull() { return getPlayersIn() == size; }

    public void next() {
        T player;
        int i;
        for (i = move ; i < i + size - 1; i++) {
            player = this.getPlayer(i);
            if (!player.isLose()) {
                this.attacking = player;
                break;
            }
        }
        for (i += 1; i < i + size - 1; i++) {
            player = getPlayer(i);
            if (!player.isLose()) {
                this.defencing = player;
                break;
            }
        }
        this.move++;
    }

    public boolean isRunning() { return result.size() < size - 1; }

    public void addLoser(T player) {
        players.remove(player);
        result.add(player);
    }

    public Object[][] getResult() {
        Object[] row;
        Object[][] rating = new Object[result.size()][4];
        T player;
        byte[] score;
        for (int i = 0; i < size; i++) {
            row = rating[i];
            player = result.get(size - i - 1);
            score = player.getScore();
            row[0] = player.getName();
            for (int j = 0; j < 3; j++) row[j + 1] = score[j];
        }
        return rating;
    }
}
