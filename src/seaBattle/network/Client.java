package seaBattle.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Scanner;

public class Client implements seaBattle.network.Socket {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private InetAddress addr;
    private int port;

    public Client() {
        try {
            addr = InetAddress.getByName(Server.HOST);
            port = Server.PORT;
        } catch (UnknownHostException e) { e.printStackTrace(); }
    }

    public void connect() {
        try {
            socket = new Socket(addr, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) { disconnect(); }
    }

    @Override
    public void send(Object data) {
        try {
            out.writeObject(data);
        } catch (IOException e) { disconnect(); }
    }

    @Override
    public Object receive() {
        try {
            Object res;
            do {
                res = in.readObject();
            } while (res == null);
            if (((Object[]) res)[0].equals("chat")) {
                System.out.println(((Object[]) res)[1]);
                return receive();
            }
            return res;
        } catch (IOException e) { disconnect();
        } catch (ClassNotFoundException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public void disconnect() {
        try {
            socket.close();
            System.out.println("Server disconnected: " + socket);
        } catch (IOException e) { e.printStackTrace(); }
    }
}
