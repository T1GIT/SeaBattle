package seaBattle.rooms;

import seaBattle.network.Connection;
import seaBattle.network.Server;
import seaBattle.players.Player;
import seaBattle.players.types.WEB;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class WebRoom extends Room {
    public final static int MAX_NAME_LENGTH = 30;
    private byte[] psw;

    public WebRoom(WEB player, int size) {
        super(size);
        this.connect(player);
    }

    public WebRoom(WEB player, int size, byte[] psw) {
        this(player, size);
        this.psw = psw;
    }

    public Connection getConn(int index) { return ((WEB) this.getPlayer(index)).getConn(); }

    public boolean isLocked() { return this.psw != null; }

    public boolean checkPsw(byte[] inPsw) { return security.checkPsw(this.psw, inPsw); }

    public void popPlayer(WEB player) {
        for (int i = players.indexOf(player); i < players.size(); i++) {
            players.set(i, players.get(i + 1));
        }
    }

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
        }
        return res;
    }

    public static void print(HashMap<String, Object[]> pack) {
        System.out.println(" ".repeat(Player.MAX_NAME_LENGTH / 2) + "\uD835\uDE81\uD835\uDE7E\uD835\uDE7E\uD835\uDE7C\uD835\uDE82");
        Object[] data;
        int num = 0;
        for (Map.Entry<String, Object[]> entry: pack.entrySet()) {
            data = entry.getValue();
            System.out.printf("%2d %2s %-" + MAX_NAME_LENGTH + "s %3s \\ %s\n",
                    num,
                    (boolean) data[0] ? "\uD83D\uDD12" : "",
                    entry.getKey(),
                    data[1],
                    data[2]);
        }
    }

    public boolean isReady() {
        for (Player player: players) {
            if (!((WEB) player).isReady()) return false;
        }
        return true;
    }

    public void sendAll(Object ... pack) {
        for(Player player: players) {
            ((WEB) player).getConn().send(pack);
        }
    }

    public static class security {
        public final static int MAX_PSW_LEN = 100;
        public final static int MIN_PSW_LEN = 4;

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
        void createRoom();
    }
}
