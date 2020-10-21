package seaBattle.network;

import java.io.IOException;

public interface Socket {
    void send(Object data) throws IOException;
    Object receive() throws IOException, ClassNotFoundException;
    void disconnect();
}
