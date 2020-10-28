package seaBattle.network;

import seaBattle.elements.Boat;
import seaBattle.elements.Field;
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
    private final Server server;
    private final Socket socket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private WEB player;
    private WebRoom room;

    public WEB getPlayer() { return player; }
    public WebRoom getRoom() { return room; }

    public Connection(Server server, Socket socket) throws IOException {
        this.server = server;
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
            if (room.isFull()) player.lose();
            else {
                room.popPlayer(player);
                room.sendAll(player.getName(), "disconnected", room.size() - room.getPlayersIn());
            }
            socket.close();
            server.connects.remove(this);
            System.gc();
        } catch (IOException e) { throw new DisconnectException(); }
    }

    @Override
    public void play(String userName) throws UI.input.CommandException {
        this.player = new WEB(userName, this);
        int chooseRoom;
        while (true){
            chooseRoom = (int) receive();
            boolean impossible = switch (chooseRoom) {
                case 0 -> server.freeRooms.size() == 0;
                case 1 -> server.freeRooms.size() >= Server.MAX_ROOMS;
                default -> throw new IllegalStateException("Unexpected value: " + chooseRoom);
            };
            if (impossible) deny();
            else break;
        }
        allow();
        switch (chooseRoom) {
            case 0 -> connectRoom();
            case 1 -> createRoom();
        }
        findPlayer();
        fillField(player);
        Object[][] rating;
        if (room.isReady()) {
            rating = mainLoop();
            room.sendAll((Object) rating);
        }
    }

    @Override
    public Player findPlayer() throws UI.input.CommandException {
        room.sendAll(player.getName(), "connected", room.size() - room.getPlayersIn());
        return null;
    }

    @Override
    public void fillField(Player player) throws UI.input.CommandException {
        Boat answer;
        Field field = player.getField();
        while (true) {
            answer = (Boat) receive();
            field.setBoat(answer);
            if (field.isStorageAvailable()) deny();
            else break;
        }
        allow();
        ((WEB) player).ready();
    }

    public void fillField() throws UI.input.CommandException { fillField(this.player); }

    @Override
    public Object[][] mainLoop() throws UI.input.CommandException {
        return new Object[0][];
    }


    @Override
    public void createRoom() {
        Object[] answer;
        // Sending available places
        send(server.getAvailablePlaces());
        // Configuring
        while (true) {
            answer = receiveArray();
            boolean withPsw = (3 == answer.length);
            if (server.freeRooms.containsKey(( String ) answer[0])) deny();
            else {
                room = withPsw
                        ? new WebRoom(player, ( int ) answer[1], ( byte[] ) answer[2])
                        : new WebRoom(player, ( int ) answer[1]);
                break;
            }
        }
        allow();
        server.freeRooms.put(( String ) answer[0], room);
    }

    @Override
    public void connectRoom() {
        WebRoom room;
        Object[] answer;
        // Sending available rooms
        send(WebRoom.pack(server.freeRooms));
        // Choosing and connecting
        while (true) {
            answer = receiveArray();
            room = server.freeRooms.get((String) answer[0]);
            if (room.isFull()) {
                send(0);
            } else if (room.isLocked() && !room.checkPsw((byte[]) answer[1])) {
                send(1);
            } else {
                send(2);
                room.connect(this.player);
                this.room = room;
                break;
            }
        }
    }

}