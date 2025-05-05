package ru.nsu.vyaznikova;

import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;

public class NetworkUtils {
    private static final int SOCKET_TIMEOUT = 10000;

    public static void sendMessage(Socket socket, Message message) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
            out.writeObject(message);
            out.flush();
        }
    }

    public static Message receiveMessage(Socket socket) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
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
        serverSocket.setSoTimeout(SOCKET_TIMEOUT);
        return serverSocket;
    }

    public static void closeQuietly(Socket socket) {
        if (socket != null && !socket.isClosed()) {
            try {
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
