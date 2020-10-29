package seaBattle.network.client;

import seaBattle.network.server.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

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
    public void send(Object data) throws DisconnectException {
        try {
            out.writeObject(data);
        } catch (IOException e) { throw new DisconnectException(); }
    }

    @Override
    public Object receive() throws DisconnectException {
        Object data = null;
        try {
            data = in.readObject();
            try {
                Object[] objArr = (Object[]) data;
                String status = (String) objArr[0];
                switch (status) {
                    case "chat" -> {
                        System.out.println("Chat: " + objArr[1]);
                        return receive();
                    }
                    case "sys" -> {
                        System.out.println(objArr[1]);
                        return receive();
                    }
                }
            } catch (ClassCastException ignored) { }
        } catch (ClassNotFoundException e) { e.printStackTrace();
        } catch (IOException e) { throw new DisconnectException(); }
        return data;
    }

    @Override
    public void disconnect() {
        try {
            socket.close();
            System.out.println("Server disconnected: " + socket);
        } catch (IOException e) { e.printStackTrace(); }
    }
}
