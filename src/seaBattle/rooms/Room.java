package seaBattle.rooms;

import seaBattle.modes.GameMode;
import seaBattle.players.types.PC;

import java.util.Iterator;

public abstract class Room<T> implements Iterator<T[]> {
    protected final int LEN = GameMode.getMaxPlayers();
    protected int move = PC.rand.inRange(0, LEN - 1);
    protected T[] storage;
    protected T attacking;
    protected T defencing;

    public T get(int index) { return storage[index]; }
    public T getAttacking() { return attacking; }
    public T getDefencing() { return defencing; }

    @Override
    public boolean hasNext() { return attacking != defencing; }

    @Override
    public abstract T[] next();
}
