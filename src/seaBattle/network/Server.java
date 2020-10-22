package seaBattle.network;

import seaBattle.rooms.WebRoom;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashSet;


public class Server extends Thread{
    private ServerSocket serverSocket;
    public static final String HOST = "localhost";
    public static final int PORT = 4444;
    public static final int MAX_CONNECTS = 50;
    public static final int MAX_ROOMS = (MAX_CONNECTS + 1) / 2;
    private final HashSet<Connection> connects = new HashSet<>(MAX_CONNECTS);
    private final ArrayList<WebRoom> freeRooms = new ArrayList<>(MAX_ROOMS);
    private final ArrayList<WebRoom> engagedRooms = new ArrayList<>(MAX_ROOMS);

    public Server() {
        try {
            InetAddress addr = InetAddress.getByName(HOST);
            serverSocket = new ServerSocket(PORT, MAX_CONNECTS, addr);
            System.out.println("Server is running: " + serverSocket.getLocalSocketAddress());
        } catch (IOException e) { e.printStackTrace(); }
    }

    @Override
    public void run() {
        while (super.isAlive()) {
            try {
                Connection connection = new Connection(serverSocket.accept());
                connects.add(connection);
                System.out.println("    Client connected: " + connection);
            } catch (IOException e) { e.printStackTrace(); }
        }
    }

    public void sendMessageAll(String msg) {
        for(Connection client : connects) {
            client.send(msg);
        }
    }


    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

}