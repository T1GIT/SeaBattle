package seaBattle.rooms;

import seaBattle.modes.GameMode;
import seaBattle.network.Connection;
import seaBattle.players.Player;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class WebRoom
        extends Room
        implements Iterator<Player[]>
{
    public final static int MAX_NAME_LENGTH = 30;
    private final String name;
    private byte[] psw;

    public String getName() { return name; }

    public WebRoom(String name, Player player) {
        this.name = name;
        this.connect(player);
    }

    public WebRoom(String name, Player player, String psw) {
        this(name, player);
        this.psw = Security.hash(psw);
    }

    public boolean tryConnect(Player player, String inPsw) {
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
                getSize());
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

        public static boolean checkPsw(byte[] hashPsw, String psw) { return Arrays.equals(hashPsw, hash(psw)); }
    }
}
