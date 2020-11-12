package seaBattle.rooms;

import seaBattle.players.Player;
import seaBattle.players.types.PC;

import java.util.ArrayList;


/**
 * Abstract class of Room.
 * It provides interaction between game and {@code Player}s.
 * Chooses who is doing way.
 * Collects losers.
 * Gives set of the players for attack, for defencing
 * iterating cyclic way.
 * @param <T> type of holding {@code Player}s
 */
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

    /**
     * Constructs room with size = {@code size}
     * @param size amount of maximum available amount of players
     */
    public Room(int size) {
        if (size > MAX_PLAYERS) throw new AssertionError("Max amount of players is " + MAX_PLAYERS + ", but " + size + " given");
        players = new ArrayList<>(size);
        result = new ArrayList<>(size);
        this.size = size;
    }

    /**
     * Returns amount of players, that is already in room
     * @return amount
     */
    public int getPlayersIn() { return players.size(); }

    /**
     * Adds {@code player} into room
     * @param player for adding
     */
    public void connect(T player) { players.add(player); }

    /**
     * Choose random player for the first way.
     * Starts going through the players.
     */
    public void start() {
        this.move = PC.rand.inRange(0, size - 1);
        next();
    }

    /**
     * Checks if all places in the room are engaged
     * @return True if the room has not empty places
     */
    public boolean isFull() { return getPlayersIn() == size; }

    /**
     * Iterates players list.
     * Writes new attacking and defencing players.
     */
    public void next() {
        System.out.println(players.size());
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

    /**
     * Checks if game is finished
     * @return True if {@code Room} has more the 1 alive players
     */
    public boolean isRunning() { return result.size() < size - 1; }

    /**
     * Adds {@code Player} to result list
     * @param player for adding
     */
    public void addLoser(T player) {
        result.add(player);
    }

    /**
     * Returns list of players in the end of the game in
     * reverse order of losing
     * @return array of 4 {@code Object}s arrays, with context:
     *      {@code [[name, kills, wounds, passes], ...] }
     */
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
