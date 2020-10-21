package seaBattle.rooms.types;

import seaBattle.players.Player;
import seaBattle.rooms.Room;

import java.util.Iterator;

public class LocalRoom
        extends Room<Player>
        implements Iterator<Player[]>
{
    private final Player[] local = new Player[LEN];

    public LocalRoom(Player player) {
        local[0] = player;
        next();
    }

    @Override
    public Player[] next() {
        for (int i = 0; i < LEN - 1; i++) {
            int num = (move + i) % LEN;
            if (!local[num].isLose()) attacking = local[num];
        }
        for (int i = 1; i < LEN; i++) {
            int num = (move + i) % LEN;
            if (!local[num].isLose()) defencing = local[num];
        }
        return new Player[]{attacking, defencing};
    }
}
