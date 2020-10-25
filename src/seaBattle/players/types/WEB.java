package seaBattle.players.types;

import seaBattle.field.Boat;
import seaBattle.network.Connection;
import seaBattle.players.Player;
import seaBattle.rooms.WebRoom;

import java.io.IOException;


public class WEB extends Player {
    private final Connection conn;

    public Connection getConn() { return conn; }

    public WEB(String name, Connection connection) {
        super(name);
        this.conn = connection;
    }

    @Override
    public boolean isHuman() {
        return true;
    }

    @Override
    public Boat getBoat() {
        Object[] raw = (Object[]) conn.receive();
        int[] coors = new int[4];
        for (int i = 0; i < raw.length; i++) coors[i] = (int) raw[i];
        return new Boat(coors);
    }

    @Override
    public int[] getAction(Player enemy) {
        Object[] raw = (Object[]) conn.receive();
        int[] coor = new int[2];
        for (int i = 0; i < raw.length; i++) coor[i] = (int) raw[i];
        return coor;
    }

    @Override
    public void retAnswer(int code) {
        conn.send(code);
    }
}
