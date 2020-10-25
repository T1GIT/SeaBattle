package seaBattle.network;

import seaBattle.modes.GameMode;
import seaBattle.players.Player;
import seaBattle.players.types.UI;
import seaBattle.players.types.WEB;
import seaBattle.rooms.WebRoom;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connection
        extends Thread
        implements seaBattle.network.Socket, GameMode, WebRoom.RoomConnecting
{
    private final Socket socket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private Player player;
    private WebRoom room;

    public Player getPlayer() { return player; }

    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(this.socket.getOutputStream());
        this.in = new ObjectInputStream(this.socket.getInputStream());
        super.start();
    }

    @Override
    public void run() {
        try {
            String playerName = (String) receive();
            play(playerName);
        } catch (DisconnectException | UI.input.CommandException e) { disconnect(); }
    }

    @Override
    public void send(Object data) throws DisconnectException {
        try {
            out.writeObject((Object) data);
        } catch (IOException e) { throw new DisconnectException(); }
    }

    @Override
    public Object receive() throws DisconnectException {
        Object res = null;
        try {
            do res = in.readObject(); while (res == null);
        } catch (ClassNotFoundException e) { e.printStackTrace();
        } catch (IOException e) { throw new DisconnectException(); }
        return res;
    }

    @Override
    public void disconnect() {
        try {
            System.out.println("    Client disconnected: " + this.getName());
            socket.close();
            Server.connects.remove(this);
            interrupt();
        } catch (IOException e) { throw new DisconnectException(); }
    }

    @Override
    public void createRoom(boolean withPsw) {

    }

    @Override
    public void connectRoom() {
        send(WebRoom.pack(Server.freeRooms));
        while (true) {
            Object[] answer = receiveArray();
            WebRoom room = Server.freeRooms.getOrDefault(answer[0], null);
            if (room == null || room.isFull()) send(0);
            else if (!room.checkPsw((byte[]) answer[1])) send(1);
            else {
                room.connect(this.player);
                this.room = room;
                send(2);
                break;
            }
        }
    }

    @Override
    public void play(String userName) throws UI.input.CommandException {
        this.player = new WEB(userName, this);
        int chooseRoom;
        while (true){
            chooseRoom = (int) receive();
            boolean impossible = switch (chooseRoom) {
                case 0 -> Server.freeRooms.size() == 0;
                case 1, 2 -> Server.freeRooms.size() >= Server.MAX_ROOMS;
                default -> throw new IllegalStateException("Unexpected value: " + chooseRoom);
            };
            if (impossible) deny();
            else break;
        }
        allow();
        switch (chooseRoom) {
            case 0 -> connectRoom();
            case 1 -> createRoom(false);
            case 2 -> createRoom(true);
        }
        findPlayer();
    }

    @Override
    public Player findPlayer() throws UI.input.CommandException {
        for (int i = 0; i < room.getPlayersIn(); i++) {
            room.getConn(i).send(
                    player.getName(),
                    "connected",
                    room.size() - room.getPlayersIn());
        }
        return null;
    }

    @Override
    public void fillField(Player player) throws UI.input.CommandException {

    }

    @Override
    public Object[][] mainLoop() throws UI.input.CommandException {
        return new Object[0][];
    }
}