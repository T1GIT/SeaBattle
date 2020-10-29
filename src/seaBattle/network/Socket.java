package seaBattle.network;

public interface Socket {

    /**
     * @param data object for sending to connected socket
     * @throws DisconnectException if another side socket
     * was disconnected
     */
    void send(Object data) throws DisconnectException;

    /**
     * @param data objects for sending to connected socket
     * packing into array of {@code Object}s
     * @throws DisconnectException if another side socket
     * was disconnected
     */
    default void send(Object ... data) throws DisconnectException { send((Object) data); }

    /**
     * Waits for answer from socket
     * @return object received from connected socket
     * @throws DisconnectException if another side socket
     * was disconnected
     */
    Object receive() throws DisconnectException;

    /**
     * Waits for answer from socket
     * @return objects array received from connected socket
     * @throws DisconnectException if another side socket
     * was disconnected
     */
    default Object[] receiveArray() throws DisconnectException { return (Object[]) receive(); }

    /**
     * Sends confirmation.
     * Messages to {@code isDenied}
     */
    default void allow() { send(false); }
    default void deny() { send(true); }

    /**
     * Checks confirmation.
     * Gets answer from {@code allow} and {@code deny}
     * @return confirmation
     */
    default boolean isDenied() { return (boolean) receive(); }

    /**
     * Closes socket and interrupt thread
     */
    void disconnect();

    /**
     * Raised if socket was closed. Suitable for safe socket closing
     */
    class DisconnectException extends RuntimeException {}
}
