package seaBattle.network;

import java.io.IOException;

public interface Socket {
    void send(Object data) throws IOException;
    default void send(Object ... data) throws IOException { send((Object) data); }
    Object receive() throws IOException, ClassNotFoundException;
    void disconnect();
}
