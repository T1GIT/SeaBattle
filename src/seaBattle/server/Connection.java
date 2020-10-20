package seaBattle.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class Connection extends Thread {

    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        in = new ObjectInputStream(this.socket.getInputStream());
        out = new ObjectOutputStream(this.socket.getOutputStream());
        System.out.println(this.socket.toString());
        start();
    }

    public void run() {
        try {
            while (true) {
                System.out.print("Client: ");
                String clientMessage = (String) in.readObject();
                out.writeObject("Answer from server");
                System.out.println(clientMessage);
            }
        } catch (ClassNotFoundException | IOException e) {
            try {
                socket.close();
                System.out.println("Client disconnected: " + this);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    public void send(Object data) {
        try {
            out.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object receive() {
        try {
            return in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}