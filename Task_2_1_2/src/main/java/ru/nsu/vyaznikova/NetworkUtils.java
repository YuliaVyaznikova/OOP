package ru.nsu.vyaznikova;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility class for network operations in the distributed system.
 * Provides methods for socket creation, message sending/receiving,
 * and resource cleanup. Manages streams for socket connections.
 */
public class NetworkUtils {
    private static final int SOCKET_TIMEOUT = 30000;  // 30 seconds timeout

    private static final Map<Socket, ObjectOutputStream> outputStreams = new ConcurrentHashMap<>();
    private static final Map<Socket, ObjectInputStream> inputStreams = new ConcurrentHashMap<>();

    /**
     * Sends a message through the specified socket.
     * Uses cached output streams for better performance.
     *
     * @param socket socket to send the message through
     * @param message message to send
     * @throws IOException if an I/O error occurs
     */
    public static void sendMessage(Socket socket, Message message) throws IOException {
        ObjectOutputStream out = outputStreams.computeIfAbsent(socket, s -> {
            try {
                return new ObjectOutputStream(s.getOutputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        synchronized (out) {
            out.writeObject(message);
            out.flush();
            out.reset();
        }
    }

    /**
     * Receives a message from the specified socket.
     * Uses cached input streams for better performance.
     *
     * @param socket socket to receive the message from
     * @return received message
     * @throws IOException if an I/O error occurs
     * @throws ClassNotFoundException if received object class is not found
     */
    public static Message receiveMessage(Socket socket) throws IOException, ClassNotFoundException {
        ObjectInputStream in = inputStreams.computeIfAbsent(socket, s -> {
            try {
                return new ObjectInputStream(s.getInputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        synchronized (in) {
            return (Message) in.readObject();
        }
    }

    /**
     * Creates a client socket with configured timeout and address reuse.
     *
     * @param host host to connect to
     * @param port port to connect to
     * @return configured client socket
     * @throws IOException if socket creation fails
     */
    public static Socket createClientSocket(String host, int port) throws IOException {
        Socket socket = new Socket(host, port);
        socket.setSoTimeout(SOCKET_TIMEOUT);
        socket.setReuseAddress(true);
        return socket;
    }

    /**
     * Creates a server socket with address reuse enabled.
     *
     * @param port port to listen on
     * @return configured server socket
     * @throws IOException if socket creation fails
     */
    public static ServerSocket createServerSocket(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        serverSocket.setReuseAddress(true);
        return serverSocket;
    }

    /**
     * Quietly closes a socket and its associated streams.
     * Any exceptions during closing are suppressed.
     *
     * @param socket socket to close
     */
    public static void closeQuietly(Socket socket) {
        if (socket != null && !socket.isClosed()) {
            try {
                ObjectOutputStream out = outputStreams.remove(socket);
                if (out != null) {
                    out.close();
                }
                ObjectInputStream in = inputStreams.remove(socket);
                if (in != null) {
                    in.close();
                }
                socket.close();
            } catch (IOException ignored) {
                // Ignore close errors as this is a quiet cleanup
            }
        }
    }

    /**
     * Quietly closes a server socket.
     * Any exceptions during closing are suppressed.
     *
     * @param serverSocket server socket to close
     */
    public static void closeQuietly(ServerSocket serverSocket) {
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException ignored) {
                // Ignore close errors as this is a quiet cleanup
            }
        }
    }
}