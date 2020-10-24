package seaBattle.rooms;

import seaBattle.network.Server;
import seaBattle.players.Player;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public class WebRoom extends Room {
    public final static int MAX_NAME_LENGTH = 30;
    private final String name;
    private byte[] psw;

    public String getName() { return name; }

    public WebRoom(String name, Player player) { this(name, player, 2); }

    public WebRoom(String name, Player player, int size) {
        super(size);
        assert (size <= Server.getFreeConn()): "Server have no free connections to making room";
        this.name = name;
        this.connect(player);
    }

    public WebRoom(String name, Player player, byte[] psw) { this(name, player, 2, psw); }

    public WebRoom(String name, Player player, int size, byte[] psw) {
        this(name, player, size);
        this.psw = psw;
    }

    public boolean tryConnect(Player player, byte[] inPsw) {
        if (Security.checkPsw(this.psw, inPsw)) {
            super.connect(player);
            return true;
        } else return false;
    }

    public String getPrinted() {
        String lock = Security.isLocked(this) ? "\uD83D\uDD12" : "";
        return String.format("%2s %-" + MAX_NAME_LENGTH + "s%3d \\ %d",
                lock,
                name,
                getPlayersIn(),
                size());
    }

    public static String[] getPrinted(List<WebRoom> roomList) {
        String[] res = new String[roomList.size()];
        ListIterator<WebRoom> iter = roomList.listIterator();
        while (iter.hasNext()) {
            int num = iter.nextIndex();
            WebRoom room = iter.next();
            res[num] = String.format("%2d %s", num, room.getPrinted());
        }
        return res;
    }

    public static class Security {
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

        public static boolean checkPsw(byte[] hashPsw, byte[] inPsw) { return Arrays.equals(hashPsw, inPsw); }
    }
}
