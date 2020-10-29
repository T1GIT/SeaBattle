package seaBattle.modes.types;

import seaBattle.elements.Boat;
import seaBattle.elements.Field;
import seaBattle.modes.GameMode;
import seaBattle.network.Socket;
import seaBattle.network.client.Client;
import seaBattle.players.Player;
import seaBattle.players.types.PC;
import seaBattle.players.types.UI;
import seaBattle.rooms.types.WebRoom;

import java.util.HashMap;

public class Multiplayer
        implements GameMode, WebRoom.RoomConnecting
{
    private final Client client = new Client();
    private Player player;

    @Override
    public void play(String userName) throws UI.input.CommandException {
        try {
            this.player = userName.toLowerCase().equals("pc")
                    ? new PC()
                    : new UI(userName);
            UI.print.line();
            System.out.println("             Welcome to ＭＵＬＴＩＰＬＡＹＥＲ　ＭＯＤＥ, Dear " + player.getName() + "! ヽ(・∀・)ﾉ");
            UI.print.line();
            client.connect();
            client.send(userName);
            int chooseRoom;
            System .out.println("Firstly, choose, if you want to do:\n" +
                    "    0 - Connect to existing room\n" +
                    "    1 - Create new room\n" +
                    ">>> Make your choice (◕‿◕)");
            while (true) {
                chooseRoom = UI.input.variant(2, "Your choice");
                client.send(chooseRoom);
                if (client.isDenied()) {
                    System.out.println(switch (chooseRoom) {
                        case 0 -> "    Wow, rooms list is empty (O_O)";
                        case 1 -> "    Oh, server has not place for a new room (︶︹︺) ";
                        default -> throw new IllegalStateException("Unexpected value: " + chooseRoom);
                    });
                    System.out.println(">>> So you should to choose your action again");
                } else break;
            }
            UI.print.line();
            switch (chooseRoom) {
                case 0 -> connectRoom();
                case 1 -> createRoom();
            }
            findPlayer();
            System.out.println("Yahoo, all of the members are connected. Let's get started!");
            UI.print.line();
            fillField(player);
            UI.print.line();
            System.out.println("    Wait when the other players become ready ... \uD83D\uDC22");
            client.receive(); // Ready
            System.out.println("\n\n        Three ... two ... one ... FIGHT! o(>ω<)o");
            Object [][] rating = mainLoop();
            UI.print.rating.table(rating);
        } catch (Socket.DisconnectException e) {
            System.out.println("There was a problem on the server. You were disconnected w(ﾟｏﾟ)w");
        }
    }

    @Override
    public Player findPlayer() throws UI.input.CommandException {
        UI.print.line();
        System.out.println("    Let's wait for the other players ... ☕");
        Object[] data;
        do {
            data = client.receiveArray();
            System.out.printf("%-" + Player.MAX_NAME_LENGTH + "s %-15s %s left\n",
                    data[0], // name (String)
                    data[1], // connected/disconnected (String)
                    data[2] //  empty places (int)
            );
        } while (!data[2].equals(0));
        return null;
    }


    @Override
    public void fillField(Player player) throws UI.input.CommandException {
        Boat boat;
        System.out.println("\nWhat a pity! " + player.getName() + ", your field is empty, let's fill it (〜￣△￣)〜");
        Field field = player.getField();
        do {
            boat = player.getBoat();
            field.setBoat(boat);
            client.send(boat);
        } while (client.isDenied());
        System.out.println("Wow, you're The God of War, " + player.getName() + "! (✯◡✯)");
        UI.print.field.table(player);
    }

    @Override
    public Object[][] mainLoop() throws UI.input.CommandException {
        Object[] answer;
        int[] coor;
        int code;
        loop:
        while (true) {
            answer = client.receiveArray();
            switch ((int) answer[0]) {
                case 0: // Attack
                    while (true) {
                        try {
                            coor = player.getAction((String) answer[1], (Field) answer[2]);
                            break;
                        } catch (UI.input.CommandException.Chat chat) {
                            client.send("chat", chat.getMessage());
                            client.receive();
                        }
                    }
                    client.send(coor);
                    code = (int) client.receive();
                    player.retAnswer(code);
                    break;
                case 1: // Defencing
                    coor = (int[]) answer[1];
                    player.attackMe(coor);
                    break;
                case 2: // Losing
                    break loop;
                case 3: // Chat
                    System.out.println(answer[1]);
                    break;
            }
        }
        return (Object[][]) client.receive();
    }

    @Override
    public void createRoom() {
        String roomName;
        int amountPlayers;
        // Receiving available places
        int maxAmount = (int) client.receive();
        // Configuring
        while (true) {
            System.out.println("Help me to choose right settings for this room, my King (づ￣ ³￣)づ\n" +
                    ">>> Answer, how do you want to call it?");
            roomName = UI.input.roomName();
            System.out.println(">>> How many people will participate in this game?");
            amountPlayers = UI.input.amountOfPlayers(maxAmount);
            System.out.println("May be you like security. In this case you can add password to your room (ಠ‿ಠ).\n" +
                    ">>> Do you want it?\n" +
                    "   0 - Password-protected room\n" +
                    "   1 - Open room");
            boolean withPsw = (0 == UI.input.variant(2, "Your choice"));
            if (withPsw) client.send(roomName, amountPlayers, UI.input.password());
            else client.send(roomName, amountPlayers);
            if (client.isDenied()) System.out.println("Sorry, but this name is already engaged (>_<)");
            else break;
        }
        System.out.println("Congratulations, you successfully created the room (ﾉ◕ヮ◕)ﾉ*:･ﾟ✧");
    }

    @Override
    public void connectRoom() {
        String roomName;
        int answer;
        // Receiving available rooms
        HashMap<String, Object[]> rooms = (HashMap<String, Object[]>) client.receive();
        WebRoom.print(rooms);
        // Choosing room
        while (true) {
            System.out.println("\n>>> Type the name of the room to connect");
            roomName = UI.input.roomName();
            if (!rooms.containsKey(roomName)) {
                System.out.println("Unfortunately, I can't find this name in the list");
            } else {
                label:
                while (true) {
                    boolean locked = (boolean) rooms.get(roomName)[0];
                    if (locked) client.send(roomName, UI.input.password());
                    else client.send(new Object[]{roomName});
                    answer = (int) client.receive();
                    switch (answer) {
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
                if (answer == 2) break;
            }
        }
        System.out.println("Congratulations, you successfully connected to the room (ﾉ◕ヮ◕)ﾉ*:･ﾟ✧");
    }
}
