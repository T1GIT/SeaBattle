package seaBattle.rooms.types;

import seaBattle.players.Player;
import seaBattle.rooms.Room;

import java.util.ArrayList;
import java.util.Iterator;

public class LocalRoom
        extends Room<Player>
        implements Iterator<Player[]>
{
    public LocalRoom() { }

    @Override
    public Player[] next() {
        for (int i = 0; i < SIZE - 1; i++) {
            int num = (move + i) % SIZE;
            if (!storage.get(num).isLose()) {
                this.attacking = storage.get(num);
                break;
            }
        }
        for (int i = 1; i < SIZE; i++) {
            int num = (move + i) % SIZE;
            if (!storage.get(num).isLose()) {
                this.defencing = storage.get(num);
                break;
            }
        }
        this.move++;
        return new Player[]{attacking, defencing};
    }
}
