package seaBattle.rooms;

import seaBattle.players.Player;

import java.util.ArrayList;

public class Room {
    public final static int MAX_PLAYERS = 10000;
    private final ArrayList<Player> players;
    private final int size;
    private int playersIn = 0;
    private int move;
    private Player attacking;
    private Player defencing;

    public Player getPlayer(int index) {
        if (index >= players.size()) throw new IndexOutOfBoundsException("Index " + index + " out of room's bounds, " + players.size());
        return players.get(index);
    }
    public Player getNextPlayer(int index) { return players.get((index + 1) % size); }
    public Player getPrevPlayer(int index) { return players.get((index - 1) % size); }
    public Player getAttacking() { return attacking; }
    public Player getDefencing() { return defencing; }
    public int size() { return size; }

    public Room() { this(2); }

    public Room(int size) {
        if (size > MAX_PLAYERS) throw new AssertionError("Max amount of players is " + MAX_PLAYERS + ", but " + size + " given");
        players = new ArrayList<>(size);
        this.size = size;
    }

    public int getPlayersIn() { return playersIn; }

    public void connect(Player player) {
        players.add(player);
        playersIn++;
    }

    public void start() {
        assert (isFull()) : "Room " + this.toString() + " has " + playersIn + " out of " + size;
        next();
    }

    public boolean isFull() { return !(playersIn < size); }

    public void next() {
        int i;
        for (i = move ;i < i + size - 1; i++) {
            int num = (i) % size;
            if (players.get(num).isAlive()) {
                this.attacking = players.get(num);
                break;
            }
        }
        for (i += 1; i < i + size - 1; i++) {
            int num = (i) % size;
            if (players.get(num).isAlive()) {
                this.defencing = players.get(num);
                break;
            }
        }
        this.move++;
    }
}
