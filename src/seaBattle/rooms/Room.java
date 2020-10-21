package seaBattle.rooms;

import seaBattle.modes.GameMode;
import seaBattle.players.types.PC;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class Room<T> implements Iterator<T[]> {
    protected final static int SIZE = GameMode.getMaxPlayers();
    protected int move = PC.rand.inRange(0, SIZE - 1);
    protected ArrayList<T> storage = new ArrayList<>(SIZE);
    protected T attacking;
    protected T defencing;

    public T get(int index) { return storage.get(index); }
    public T getAttacking() { return attacking; }
    public T getDefencing() { return defencing; }

    @Override
    public boolean hasNext() { return attacking != defencing; }

    @Override
    public abstract T[] next();
}
