package seaBattle.server;

import seaBattle.modes.GameMode;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.ListIterator;

public class Room {
    private final String name;
    private final Connection[] conns = new Connection[GameMode.getMaxPlayers()];
    private int move = 0;
    private byte[] psw;

    public String getName() { return name; }

    public Room(String name, Connection connection) {
        this.name = name;
        conns[0] = connection;
    }

    public Room(String name, Connection connection, String psw) {
        this(name, connection);
        this.psw = hash(psw);
    }

    public void connect(Connection connection) { conns[1] = connection; }

    public boolean isLocked() { return this.psw != null; }

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

    public static String[] getPrinted(List<Room> roomList) {
        String[] res = new String[roomList.size()];
        ListIterator<Room> iter = roomList.listIterator();
        while (iter.hasNext()) {
            int num = iter.nextIndex();
            Room room = iter.next();
            String lock = room.isLocked() ? "\uD83D\uDD12" : "";
            res[num] = String.format("%2d %2s %s", num, lock, room.name);
        }
        return res;
    }
}
