package seaBattle.modes.types;

import seaBattle.modes.GameMode;
import seaBattle.network.Client;
import seaBattle.network.Socket;
import seaBattle.players.Player;
import seaBattle.players.types.UI;
import seaBattle.rooms.WebRoom;

import java.util.HashMap;

public class Multiplayer
        implements GameMode, WebRoom.RoomConnecting
{
    private final Client client = new Client();
    private String userName;

    @Override
    public void play(String userName) throws UI.input.CommandException {
        try {
            this.userName = userName;
            UI.print.line();
            System.out.println("             Welcome to ＭＵＬＴＩＰＬＡＹＥＲ　ＭＯＤＥ, Dear " + userName + "! ヽ(・∀・)ﾉ");
            UI.print.line();
            client.connect();
            client.send(userName);
            int chooseRoom;
            System .out.println("Firstly, choose, if you want to do:\n" +
                    "    0 - Connect to existing room\n" +
                    "    1 - Create new open room\n" +
                    "    2 - Create new secure room(with password)\n" +
                    ">>> Make your choice (◕‿◕)");
            while (true) {
                chooseRoom = UI.input.mode(3, "Your choice");
                client.send(chooseRoom);
                if (client.isDenied()) {
                    switch (chooseRoom) {
                        case 0 -> System.out.println("    Wow, rooms list is empty (O_O)");
                        case 1, 2 -> System.out.println("    Oh, server has not place for a new room (︶︹︺) ");
                        default -> throw new IllegalStateException("Unexpected value: " + chooseRoom);
                    }
                    System.out.println(">>> So you should to choose your action again");
                } else break;
            }
            UI.print.line();
            switch (chooseRoom) {
                case 0 -> connectRoom();
                case 1 -> createRoom(false);
                case 2 -> createRoom(true);
            }
            findPlayer();

        } catch (Socket.DisconnectException e) {
            System.out.println("There was a problem on the server. You were disconnected w(ﾟｏﾟ)w");
        } finally {
            System.out.print("What do you want to do next? ... ");
            UI.input.command();
        }
    }

    @Override
    public Player findPlayer() throws UI.input.CommandException {
        UI.print.line();
        System.out.println("Let's wait for the other players...");
        Object[] data;
        do {
            data = client.receiveArray();
            System.out.printf("Player %s %s, %s free places last in the room\n",
                    data[0], //     name (String)
                    data[1], //     connected/disconnected (String)
                    data[2] //      empty places (int)
            );
            if (data[2].equals(0)) return null;
        } while (!data[2].equals(0));
        return null;
    }


    @Override
    public void fillField(Player player) throws UI.input.CommandException {

    }

    @Override
    public Object[][] mainLoop() throws UI.input.CommandException {
        return null;
    }

    @Override
    public void connectRoom() {
        HashMap<String, Object[]> rooms = (HashMap<String, Object[]>) client.receive();
        WebRoom.print(rooms);
        while (true) {
            System.out.println("\n>>> Type the name of the room to connect");
            String roomName = UI.input.roomName();
            if (!rooms.containsKey(roomName)) System.out.println("No one room with name " + roomName);
            else {
                label: while (true) {
                    if ((boolean) rooms.get(roomName)[0]) client.send(roomName, UI.input.password());
                    else client.send(roomName);
                    switch (( int ) client.receive()) {
                        case 0:
                            System.out.println("Sorry, but this room is inaccessible (>_<)");
                            break label;
                        case 1:
                            System.out.println("Incorrect password. Please, try again. I'm sure, you can do that ( ￣▽￣)");
                            break;
                        case 2:
                            break label;
                    }
                }
                break;
            }
        }
        System.out.println("Congratulations, you successfully connected to the room (ﾉ◕ヮ◕)ﾉ*:･ﾟ✧");
    }

    @Override
    public void createRoom(boolean withPsw) {

    }
}
