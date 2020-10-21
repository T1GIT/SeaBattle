package seaBattle.network.server;

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
        try {
            while (super.isAlive()) {
                System.out.println(this.toString() + "|" + receive());
            }
        } catch (IOException | ClassNotFoundException e) { disconnect(); }
    }

    @Override
    public void send(Object data) throws IOException {
        out.writeObject(data);
    }

    @Override
    public Object receive() throws IOException, ClassNotFoundException {
        Object res = null;
        do {
            res = in.readObject();
        } while (res == null);
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