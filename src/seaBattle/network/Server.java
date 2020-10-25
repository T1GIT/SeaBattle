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
        Server.freeRooms.put("test1" ,new WebRoom(null, 2));
        Server.freeRooms.put("test2", new WebRoom(null, 5, WebRoom.security.hash("0000")));
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