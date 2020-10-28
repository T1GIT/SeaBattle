package seaBattle.rooms;

import seaBattle.players.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Stack;

public class Room {
    public final static int MAX_PLAYERS = 10000;
    protected final ArrayList<Player> players;
    protected final ArrayList<Player> result;
    protected final int size;
    protected int move;
    protected Player attacking;
    protected Player defencing;

    public Player getPlayer(int index) {
        if (index >= players.size()) throw new IndexOutOfBoundsException("Index " + index + " out of room's bounds, " + players.size());
        return players.get(index);
    }
    public Player getNextPlayer(int index) { return players.get((index + 1) % size); }
    public Player getPrevPlayer(int index) { return players.get((index - 1) % size); }
    public Player getAttacking() { return attacking; }
    public Player getDefencing() { return defencing; }
    public int size() { return size; }

    public Room(int size) {
        if (size > MAX_PLAYERS) throw new AssertionError("Max amount of players is " + MAX_PLAYERS + ", but " + size + " given");
        players = new ArrayList<>(size);
        result = new ArrayList<>(size);
        this.size = size;
    }

    public int getPlayersIn() { return players.size(); }

    public void connect(Player player) { players.add(player); }

    public void start() { next(); }

    public boolean isFull() { return getPlayersIn() == size; }

    public void next() {
        int i;
        for (i = move ;i < i + size - 1; i++) {
            int num = (i) % size;
            if (!players.get(num).isLose()) {
                this.attacking = players.get(num);
                break;
            }
        }
        for (i += 1; i < i + size - 1; i++) {
            int num = (i) % size;
            if (!players.get(num).isLose()) {
                this.defencing = players.get(num);
                break;
            }
        }
        this.move++;
    }

    public boolean isFinished() { return result.size() >= size - 1; }

    public void addLoser(Player player) { result.add(player); }

    public Object[][] getResult() {
        Object[][] rating = new Object[result.size()][2];
        Player player;
        for (int i = 0; i < size; i++) {
            player = result.get(size - i - 1);
            rating[i][0] = player.getName();
            rating[i][1] = player.getScore();
        }
        return rating;
    }
}
