package seaBattle.network;

import seaBattle.modes.GameMode;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connection
        extends Thread
        implements seaBattle.network.Socket
{
    private final Socket socket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;

    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(this.socket.getOutputStream());
        this.in = new ObjectInputStream(this.socket.getInputStream());
        super.start();
    }

    @Override
    public void run() {
        while (super.isAlive()) {
            System.out.println(this.getName() + "|" + receive());
        }
    }

    @Override
    public void send(Object data) {
        try {
            out.writeObject(data);
        } catch (IOException e) { disconnect(); }
    }

    @Override
    public Object receive() {
        Object res = null;
        try {
            do {
                res = in.readObject();
            } while (res == null);
        } catch (IOException e) { disconnect();
        } catch (ClassNotFoundException e) { e.printStackTrace(); }
        return res;
    }

    @Override
    public void disconnect() {
        try {
            socket.close();
            System.out.println("Client disconnected: " + this);
            interrupt();
        } catch (IOException e) { e.printStackTrace(); }
    }
}