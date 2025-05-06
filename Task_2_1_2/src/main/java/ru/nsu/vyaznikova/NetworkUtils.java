package ru.nsu.vyaznikova;

import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NetworkUtils {
    private static final int SOCKET_TIMEOUT = 30000;  // 30 seconds timeout

    private static final Map<Socket, ObjectOutputStream> outputStreams = new ConcurrentHashMap<>();
    private static final Map<Socket, ObjectInputStream> inputStreams = new ConcurrentHashMap<>();

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

    public static Socket createClientSocket(String host, int port) throws IOException {
        Socket socket = new Socket(host, port);
        socket.setSoTimeout(SOCKET_TIMEOUT);
        return socket;
    }

    public static ServerSocket createServerSocket(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        return serverSocket;
    }

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
            }
        }
    }

    public static void closeQuietly(ServerSocket serverSocket) {
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException ignored) {
            }
        }
    }
}