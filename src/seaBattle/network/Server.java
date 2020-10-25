package seaBattle.network;

import seaBattle.rooms.WebRoom;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.HashSet;


public class Server extends Thread{
    private ServerSocket serverSocket;
    public static final String HOST = "localhost";
    public static final int PORT = 4444;
    public static final int MAX_CONNECTS = 50;
    public static final int MAX_ROOMS = (MAX_CONNECTS + 1) / 2;
    public static final HashSet<Connection> connects = new HashSet<>(MAX_CONNECTS);
    public static final HashMap<String, WebRoom> freeRooms = new HashMap<>(MAX_ROOMS);
    public static final HashMap<String, WebRoom> engagedRooms = new HashMap<>(MAX_ROOMS);

    public Server() {
        Server.freeRooms.put("Dima" ,new WebRoom(null, 2));
        Server.freeRooms.put("seffffddd", new WebRoom(null, 2, WebRoom.security.hash("1234")));
        Server.freeRooms.put("Difaeefasema" ,new WebRoom(null, 2));
        Server.freeRooms.put("Dffsafsdfima" ,new WebRoom(null, 2));
        Server.freeRooms.put("Ddfasdfiasfdsfafma" ,new WebRoom(null, 2));
        Server.freeRooms.put("fiejef", new WebRoom(null, 2, WebRoom.security.hash("1111")));
        Server.freeRooms.put("afsadfadfDsdfasdfima" ,new WebRoom(null, 2));
        Server.freeRooms.put("adfasdfsdfDadfima" ,new WebRoom(null, 2));
        Server.freeRooms.put("tt4tt", new WebRoom(null, 2, WebRoom.security.hash("0000")));
        try {
            InetAddress addr = InetAddress.getByName(HOST);
            serverSocket = new ServerSocket(PORT, MAX_CONNECTS, addr);
            System.out.println("Server is running: " + serverSocket.getLocalSocketAddress());
        } catch (IOException e) { e.printStackTrace(); }
    }

    @Override
    public void run() {
        try {
            while (super.isAlive()) {
                Connection connection = new Connection(serverSocket.accept());
                connects.add(connection);
                System.out.println("    Client connected: " + connection.getName());
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}