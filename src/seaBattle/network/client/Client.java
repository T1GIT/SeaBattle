package seaBattle.network.client;

import seaBattle.network.server.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client implements seaBattle.network.Socket {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private InetAddress addr;
    private int port;

    public Client() {
        try {
            addr = InetAddress.getByName(Server.getHost());
            port = Server.getPort();
        } catch (UnknownHostException e) { e.printStackTrace(); }
    }

    public void connect() {
        try {
            socket = new Socket(addr, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            while (true) {
                send(new Scanner(System.in).nextLine());
            }
        } catch (IOException e) { disconnect(); }
    }

    @Override
    public void send(Object data) throws IOException {
        out.writeObject(data);
    }

    @Override
    public Object receive() throws IOException, ClassNotFoundException {
        Object res;
        do {
            res = in.readObject();
        } while (res == null);
        return res;
    }

    @Override
    public void disconnect() {
        try {
            socket.close();
            System.out.println("Server disconnected: " + socket);
        } catch (IOException e) { e.printStackTrace(); }
    }
}
