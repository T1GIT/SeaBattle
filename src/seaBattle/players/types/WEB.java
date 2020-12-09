package seaBattle.players.types;

import seaBattle.elements.Boat;
import seaBattle.elements.Field;
import seaBattle.network.Socket;
import seaBattle.network.server.Connection;
import seaBattle.players.Player;


public class WEB
        extends Player
{
    private final Connection conn;
    private PC bot = null;
    private boolean ready = false;

    public Connection getConn() { return conn; }

    public WEB(String name, Connection connection) {
        super(name);
        this.conn = connection;
    }

    @Override
    public boolean isHuman() {
        return bot == null;
    }

    @Override
    public Boat getBoat() {
        Boat boat;
        try {
            if (isHuman()) {
                boat = (Boat) conn.receive();
            } else {
                boat = bot.getBoat();
            }
        } catch (Socket.DisconnectException e) {
            toBot();
            boat = getBoat();
        }
        return boat;
    }

    @Override
    public int[] getAction(String enemyName, Field enemyField) {
        int[] action;
        try {
            if (isHuman()) {
                conn.send(0, enemyName, enemyField);
                action = (int[]) conn.receive();
            } else {
                action = bot.getAction(enemyName, enemyField);
            }
        } catch (Socket.DisconnectException e) {
            toBot();
            action = getAction(enemyName, enemyField);
        }
        return  action;
    }

    @Override
    public int attackMe(int[] coor) {
        if (isHuman()) conn.send(1, coor);
        return super.attackMe(coor);
    }

    @Override
    public void retAnswer(int code) {
        super.addScore(code);
        if (isHuman()) conn.send(code);
        else bot.retAnswer(code);
    }

    public void toBot() { this.bot = new PC(field); }

    public void ready() { this.ready = true; }

    public boolean isReady() { return ready; }
}
