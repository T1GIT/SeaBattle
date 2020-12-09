package seaBattle.network.server;

import seaBattle.elements.Boat;
import seaBattle.elements.Field;
import seaBattle.modes.GameMode;
import seaBattle.players.Player;
import seaBattle.players.types.UI;
import seaBattle.players.types.WEB;
import seaBattle.rooms.Room;
import seaBattle.rooms.types.WebRoom;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

public class Connection
        extends Thread
        implements seaBattle.network.Socket, GameMode, WebRoom.RoomConnecting
{
    private final Server server;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private WEB player;
    private WebRoom room;
    private String roomName;

    public Connection(Server server, Socket socket) throws IOException {
        this.server = server;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        super.start();
    }

    @Override
    public void run() {
        try {
            String playerName = (String) receive();
            play(playerName);
        } catch (DisconnectException e) { disconnect(); }
    }

    @Override
    public void send(Object data) throws DisconnectException {
        try {
            out.writeObject(data);
        } catch (IOException e) { throw new DisconnectException(); }
    }

    @Override
    public Object receive() throws DisconnectException {
        Object data = null;
        try {
            data = in.readObject();
            try {
                if (((Object[]) data)[0].equals("chat")) {
                    if (room.isReady()) room.sendAll(3, ((Object[]) data)[1]);
                    return receive();
                }
            } catch (ClassCastException ignored) { }
        } catch (ClassNotFoundException e) { e.printStackTrace();
        } catch (IOException e) { throw new DisconnectException(); }
        return data;
    }

    @Override
    public void disconnect() {
        System.out.println("    Client disconnected: " + this.getName());
        if (room != null) {
            if (room.humanIsHere()) {
                if (room.isFull()) {
                    player.toBot();
                    room.sendAll("sys", player.getName() + " disconnected and changed to bot");
                } else {
                    room.sendAll("sys",
                            player.getName(),
                            "disconnected",
                            room.size() - room.getPlayersIn());
                }
            } else server.freeRooms.remove(roomName, room);
        }
        server.connects.remove(this);
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
        try {
            fillField(player);
        } catch (DisconnectException e) {
            player.toBot();
            fillField(player);
        }
        player.ready();
        if (room.isReady()) {
            room.sendAll(0);
            Object[][] rating = mainLoop();
            room.sendAll(( Object ) rating);
        }
    }

    @Override
    public Player findPlayer() throws UI.input.CommandException {
        room.sendAll(player.getName(), "connected", room.size() - room.getPlayersIn());
        return null;
    }

    @Override
    public void fillField(Player player) throws UI.input.CommandException {
        Boat boat;
        Field field = player.getField();
        while (true) {
            boat = player.getBoat();
            field.setBoat(boat);
            if (field.isStorageAvailable()) {
                if (player.isHuman()) deny();
            } else break;
        }
        if (player.isHuman()) allow();
    }

    @Override
    public Object[][] mainLoop() throws UI.input.CommandException {
        int[] action;
        int answer;
        room.start();
        WEB attacking = room.getAttacking();
        WEB defencing = room.getDefencing();
        do {
            action = attacking.getAction(
                    defencing.getName(),
                    defencing.getField().getSecured());
            answer = defencing.attackMe(action);
            attacking.retAnswer(answer);
            if (defencing.isLose()) {
                room.addLoser(defencing);
                room.next();
            } else if (answer == 0) {
                room.next();
                attacking = room.getAttacking();
                defencing = room.getDefencing();
            }
        } while (room.isRunning());
        room.addLoser(attacking);
        return room.getResult();
    }


    @Override
    public void createRoom() {
        Object[] answer;
        String roomName;
        // Sending available places
        send(server.getAvailablePlaces());
        // Configuring
        while (true) {
            answer = receiveArray();
            roomName = (String) answer[0];
            boolean withPsw = (3 == answer.length);
            if (server.freeRooms.containsKey(roomName)) deny();
            else {
                room = withPsw
                        ? new WebRoom(player, ( int ) answer[1], ( byte[] ) answer[2])
                        : new WebRoom(player, ( int ) answer[1]);
                break;
            }
        }
        allow();
        this.roomName = roomName;
        server.freeRooms.put(roomName, room);
    }

    @Override
    public void connectRoom() {
        WebRoom room;
        Object[] answer;
        String roomName;
        // Sending available rooms
        send(WebRoom.pack(server.freeRooms));
        // Choosing and connecting
        while (true) {
            answer = receiveArray();
            roomName = (String) answer[0];
            room = server.freeRooms.get(roomName);
            if (room.isFull()) {
                send(0);
            } else if (room.isLocked() && !room.checkPsw((byte[]) answer[1])) {
                send(1);
            } else {
                send(2);
                room.connect(this.player);
                this.room = room;
                if (room.isFull()) server.freeRooms.remove(roomName);
                break;
            }
        }
    }

}