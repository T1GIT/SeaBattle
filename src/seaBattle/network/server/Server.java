package seaBattle.network.server;

import seaBattle.network.Socket;
import seaBattle.rooms.types.WebRoom;

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
    public final HashSet<Connection> connects = new HashSet<>(MAX_CONNECTS);
    public final HashMap<String, WebRoom> freeRooms = new HashMap<>(MAX_ROOMS);


    public Server() {
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
                Connection connection = new Connection(this, serverSocket.accept());
                if (connects.size() >= MAX_CONNECTS) {
                    System.out.print("    Client discarded: ");
                    try { connection.disconnect(); } catch (Socket.DisconnectException ignored) {}
                } else {
                    System.out.println("    Client connected: " + connection.getName());
                    connects.add(connection);
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    public int getAvailablePlaces() { return Server.MAX_CONNECTS - this.connects.size(); }

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}