package seaBattle.rooms;

import seaBattle.players.Player;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class WebRoom extends Room {
    public final static int MAX_NAME_LENGTH = 30;
    private byte[] psw;

    public WebRoom(Player player, int size) {
        super(size);
        this.connect(player);
    }

    public WebRoom(Player player, int size, byte[] psw) {
        this(player, size);
        this.psw = psw;
    }

    public boolean isLocked() { return this.psw != null; }

    public boolean checkPsw(byte[] inPsw) { return security.checkPsw(this.psw, inPsw); }

    public static HashMap<String, Object[]> pack(HashMap<String, WebRoom> rooms) {
        HashMap<String, Object[]> res = new HashMap<>(rooms.size());
        for (Map.Entry<String, WebRoom> entry: rooms.entrySet()) {
            WebRoom room = entry.getValue();
            res.put(entry.getKey(), new Object[]{
                    room.isLocked(),
                    room.getPlayersIn(),
                    room.size()
            });
        }
        return res;
    }

    public static void print(HashMap<String, Object[]> pack) {
        System.out.println(" ".repeat(Player.MAX_NAME_LENGTH / 2) + "\uD835\uDE81\uD835\uDE7E\uD835\uDE7E\uD835\uDE7C\uD835\uDE82");
        int num = 0;
        for (Map.Entry<String, Object[]> entry: pack.entrySet()) {
            Object[] data = entry.getValue();
            System.out.printf("%2d %2s %-" + MAX_NAME_LENGTH + "s %3s \\ %s\n",
                    num,
                    (boolean) data[0] ? "\uD83D\uDD12" : "",
                    entry.getKey(),
                    data[1],
                    data[2]);
            num++;
        }
    }

    public static class security {
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

    public interface RoomConnecting{
        void connectRoom();
        void createRoom(boolean withPsw);
    }
}
