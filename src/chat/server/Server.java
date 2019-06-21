package chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

class Server {
    private Vector<ClientHandler> clients;
    Server() {
        clients = new Vector<>();
        ServerSocket server = null;
        Socket socket = null;

        try {
            server = new ServerSocket(8189);
            System.out.println("Сервер запущен");

            while (true) {
                socket = server.accept();
                System.out.println("Клиент подключился");
                clients.add(new ClientHandler(socket, this));
            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    void broadcastMessage(String text) {
        for (ClientHandler client : clients) {
            client.sendMessage(text);
        }
    }
}