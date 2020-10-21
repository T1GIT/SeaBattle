package seaBattle.rooms.types;

import seaBattle.modes.GameMode;
import seaBattle.rooms.Room;
import seaBattle.server.Connection;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class WebRoom
        extends Room<Connection>
        implements Iterator<Connection[]>
{
    private final Connection[] network = new Connection[GameMode.getMaxPlayers()];
    private final String name;
    private byte[] psw;

    public WebRoom(String name, Connection connection) {
        this.name = name;
        network[0] = connection;
    }

    public WebRoom(String name, Connection connection, String psw) {
        this(name, connection);
        this.psw = Security.hash(psw);
    }

    public String getName() { return name; }



    public static String[] getPrinted(List<WebRoom> roomList) {
        String[] res = new String[roomList.size()];
        ListIterator<WebRoom> iter = roomList.listIterator();
        while (iter.hasNext()) {
            int num = iter.nextIndex();
            WebRoom room = iter.next();
            String lock = WebRoom.Security.isLocked(room) ? "\uD83D\uDD12" : "";
            res[num] = String.format("%2d %2s %s", num, lock, room.name);
        }
        return res;
    }

    @Override
    public Connection[] next() {
        return new Connection[0];
    }

    private static class Security {
        public static boolean isLocked(WebRoom room) { return room.psw != null; }

        public static byte[] hash(String str) {
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                return digest.digest(str.getBytes(StandardCharsets.UTF_8));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return null;
        }

        public static boolean checkPsw(WebRoom room, String psw) {
            return hash(psw) == room.psw;
        }
    }
}
