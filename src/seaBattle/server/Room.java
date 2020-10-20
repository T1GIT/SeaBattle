package seaBattle.server;

import org.jetbrains.annotations.NotNull;
import seaBattle.modes.GameMode;
import seaBattle.players.Player;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.SortedSet;

public class Room {
    private final String name;
    private final Player[] players = new Player[GameMode.getMaxPlayers()];
    private int move = 0;
    private byte[] psw;

    public String getName() { return name; }

    public Room(String name, Player player) {
        this.name = name;
        players[0] = player;
    }

    public Room(String name, Player player, String psw) {
        this(name, player);
        this.psw = hash(psw);
    }

    public void connect(Player player) { players[1] = player; }

    public boolean isLocked() { return this.psw.length > 0; }

    public byte[] hash(String str) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(str.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean checkPsw(String psw) {
        return hash(psw) == this.psw;
    }
}
