package seaBattle.network.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread{
    private ServerSocket serverSocket;
    private static final String HOST = "localhost";
    private static final int PORT = 4444;
    private static final int MAX_CONNECTS = 50;
    private final List<Connection> connects = new ArrayList<>(MAX_CONNECTS);
    private final List<Room> freeRooms = new ArrayList<>();
    private final List<Room> engagedRooms = new ArrayList<>();

    static public int getPort() { return PORT; }
    static public String getHost() { return HOST; }

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
            try {
                client.send(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

}