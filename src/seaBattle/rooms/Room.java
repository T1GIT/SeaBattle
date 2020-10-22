package seaBattle.rooms;

import seaBattle.modes.GameMode;
import seaBattle.players.Player;
import seaBattle.players.types.PC;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;

public class Room implements Iterator<Player[]> {
    private final int size = GameMode.MAX_PLAYERS;
    private final ArrayList<Player> players = new ArrayList<>(size);
    private int playersIn = 0;
    private int move = PC.rand.inRange(0, size - 1);
    private Player attacking;
    private Player defencing;

    public Player getPlayer(int index) { return players.get(index); }
    public Player getAttacking() { return attacking; }
    public Player getDefencing() { return defencing; }
    public int getSize() { return size; }

    public int getPlayersIn() { return playersIn; }

    public void connect(Player player) {
        assert (!isFull()) : "Room " + this.toString() + " is full";
        players.add(player);
        playersIn++;
    }

    public void start() {
        assert (isFull()) : "Room " + this.toString() + " has " + playersIn + " out of " + size;
        next();
    }

    public boolean isFull() { return !(playersIn < size); }

    @Override
    public boolean hasNext() { return attacking != defencing; }

    @Override
    public Player[] next() {
        for (int i = 0; i < size - 1; i++) {
            int num = (move + i) % size;
            if (players.get(num).isAlive()) {
                this.attacking = players.get(num);
                break;
            }
        }
        for (int i = 1; i < size; i++) {
            int num = (move + i) % size;
            if (players.get(num).isAlive()) {
                this.defencing = players.get(num);
                break;
            }
        }
        this.move++;
        return new Player[]{attacking, defencing};
    }

    @Override
    public void forEachRemaining(Consumer<? super Player[]> action) {
        Objects.requireNonNull(action);
        for (Player player: players) action.accept(new Player[]{player});
    }
}
