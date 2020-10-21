package seaBattle.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Server extends Thread{
    private static final String HOST = "localhost";
    private static final int PORT = 4444;
    private static final int MAX_CONNECTS = 50;
    public static List<Connection> connects = new ArrayList<>(MAX_CONNECTS);
    private ServerSocket serverSocket;

    static public int getPort() { return PORT; }
    static public String getHost() { return HOST; }

    public Server() {
        try {
            InetAddress addr = InetAddress.getByName(HOST);
            serverSocket = new ServerSocket(PORT, MAX_CONNECTS, addr);
            System.out.println("Server is running: " + serverSocket.getLocalSocketAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            try {
                Connection connection = new Connection(serverSocket.accept());
                sendMessageAll("added: " + connection.toString());
                Server.connects.add(connection);
                System.out.println("    Client connected: " + connection);
            } catch (IOException e) {
                e.printStackTrace();
            }
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