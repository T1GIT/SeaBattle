package seaBattle.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Server extends Thread{
    private static final String HOST = "localhost";
    private static final int PORT = 5005;
    public static List<Connect> connects = new ArrayList<>();
    private ServerSocket serverSocket;

    static public int getPort() { return PORT; }
    static public String getHost() { return HOST; }

    public Server() {
        try {
            InetAddress address = InetAddress.getByName(HOST);
            serverSocket = new ServerSocket(PORT, 0, address);
            System.out.println("Server is running: " + serverSocket.getLocalSocketAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            try {
                Connect clientConnection = new Connect(serverSocket.accept());
                Server.connects.add(clientConnection);
                System.out.println("    Client connected: " + clientConnection);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void sendMessageAll(String msg) {
        for(Connect client : connects) {
            client.send(msg);
        }
    }


    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

}