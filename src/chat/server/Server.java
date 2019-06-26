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
            AuthService.connect();

            server = new ServerSocket(8189);
            System.out.println("Server started");

            while (true) {
                socket = server.accept();
                new ClientHandler(socket, this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            AuthService.disconect();
        }
    }

    void broadcastMessage(String text) {
        for (ClientHandler client : clients) {
            if (!client.getSocket().isClosed())
                client.sendMessage(text);
        }
    }

    void subsсribe(ClientHandler client) {
        clients.add(client);
        broadcastMessage(client.getNickname() + " ворвался в чат!");
        System.out.println(client.getNickname() + " joined the channel");
    }

    void unsubsсribe(ClientHandler client) {
        clients.remove(client);
        broadcastMessage(client.getNickname() + " заскучал и ушёл...");
        System.out.println(client.getNickname() + " has left the channel");
    }
}