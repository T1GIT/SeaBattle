package seaBattle.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private InetAddress addr;
    private int port;

    public Client() {
        try {
            addr = InetAddress.getByName(Server.getHost());
            port = Server.getPort();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void connect() {
        try {
            socket = new Socket(addr, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            while (true) {
                out.writeObject(new Scanner(System.in).nextLine());
                String serverMessage = (String) in.readObject();
                System.out.println(serverMessage);
//                out.writeObject("OK : " + new Scanner(System.in).nextLine());
            }
        } catch (IOException | ClassNotFoundException e) {
            try {
                socket.close();
                System.out.println("Server disconnected: " + socket);
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
