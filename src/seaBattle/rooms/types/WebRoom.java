package seaBattle.rooms.types;

import seaBattle.players.Player;
import seaBattle.players.types.WEB;
import seaBattle.rooms.Room;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class WebRoom extends Room<WEB> {
    public final static int MAX_NAME_LENGTH = 30;
    private final byte[] psw;

    public WebRoom(WEB player, int size) { this(player, size, null); }

    public WebRoom(WEB player, int size, byte[] psw) {
        super(size);
        this.psw = psw;
        this.connect(player);
    }

    public boolean isLocked() { return this.psw != null; }

    public boolean checkPsw(byte[] inPsw) { return security.checkPsw(this.psw, inPsw); }

    public static HashMap<String, Object[]> pack(HashMap<String, WebRoom> rooms) {
        HashMap<String, Object[]> res = new HashMap<>(rooms.size());
        WebRoom room;
        for (Map.Entry<String, WebRoom> entry: rooms.entrySet()) {
            room = entry.getValue();
            res.put(entry.getKey(), new Object[]{
                    room.isLocked(),
                    room.getPlayersIn(),
                    room.size()
            });
            System.out.println(entry.getKey() + " " + room);
        }
        return res;
    }

    public static void print(HashMap<String, Object[]> pack) {
        System.out.println(" ".repeat(Player.MAX_NAME_LENGTH / 2) + "\uD835\uDE81\uD835\uDE7E\uD835\uDE7E\uD835\uDE7C\uD835\uDE82");
        Object[] data;
        for (Map.Entry<String, Object[]> entry: pack.entrySet()) {
            data = entry.getValue();
            System.out.printf("%2s %-" + MAX_NAME_LENGTH + "s %3s \\ %s\n",
                    (boolean) data[0] ? "\uD83D\uDD12" : "",
                    entry.getKey(),
                    data[1],
                    data[2]);
        }
    }

    public boolean isReady() {
        for (WEB player: players) if (!player.isReady()) return false;
        return true;
    }

    public boolean humanIsHere() {
        for (WEB player: players) if (player.isHuman()) return true;
        return false;
    }

    @Override
    public void addLoser(WEB player) {
        player.getConn().send(2, null);
        super.addLoser(player);
    }

    public void sendAll(Object obj) {
        for(WEB player: players) {
            if (player.isHuman()) player.getConn().send(obj);
        }
    }

    public void sendAll(Object ... pack) { sendAll((Object) pack); }

    public static class security {
        public final static int MAX_PSW_LEN = 100;
        public final static int MIN_PSW_LEN = 4;

        public static byte[] hash(String str) {
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                return digest.digest(str.getBytes(StandardCharsets.UTF_8));
            } catch (NoSuchAlgorithmException e) { e.printStackTrace(); }
            return null;
        }

        public static boolean checkPsw(byte[] hashPsw, byte[] inPsw) { return Arrays.equals(hashPsw, inPsw); }
    }

    public interface RoomConnecting{
        void connectRoom();
        void createRoom();
    }
}
